#include <Wire.h>
#include <ESP8266WiFi.h>

#define ADXL345_DEVICE (0x53)    // Device Address for ADXL345
#define ADXL345_TO_READ (6)      // Number of Bytes Read - Two Bytes Per Axis

#define ADXL345_DEVID			0x00		// Device ID
#define ADXL345_DATAX0			0x32		// X-Axis Data 0
#define ADXL345_POWER_CTL		0x2D		// Power-Saving Features Control
#define ADXL345_DATA_FORMAT 0x31

#define WIFI_SSID "LevinsonNet"
#define WIFI_PASSWORD "DHARMAPROJECT"
#define GAME_HOST "192.168.1.21"
#define GAME_PORT 9999

byte _buff[6] ;
const char* ssid = WIFI_SSID;
const char* password = WIFI_PASSWORD;
const char* host = GAME_HOST;
const uint16_t port = GAME_PORT;
WiFiClient client;

void setup() {
  Serial.begin(9600);

  //Initialize the I2C communication. This will set the Arduino up as the 'Master' device.
  Wire.begin(D1, D2);

  //Read the WHO_AM_I register and print the result
  byte id = 0; 
  
  readFromI2C(ADXL345_DEVID, 1, &id);  
  Serial.println();
  Serial.print("ADXL345 ID: ");
  Serial.println(id, HEX);

  writeToI2C(ADXL345_DATA_FORMAT, 8);
	writeToI2C(ADXL345_POWER_CTL, 0);	// Wakeup
	// writeToI2C(ADXL345_POWER_CTL, 16);	// Auto_Sleep
	writeToI2C(ADXL345_POWER_CTL, 8);	// Measure

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void loop() {
  int x,y,z;  
  readAccel(&x, &y, &z); 

  // //Output Results to Serial
  // Serial.print(x);
  // Serial.print(",\t");
  // Serial.print(y);
  // Serial.print(",\t");
  // Serial.println(z); 

  if (client.connected()) { 
    client.print(x);
    client.print(", ");
    client.print(y);
    client.print(", ");
    client.println(z); 
  } else {
    Serial.println("Trying to connect to game server");
    if (client.connect(host, port)) {
      Serial.println("Success!");
    } else {
      Serial.println("connection failed");
      delay(5000);
    }
  }

  // //Create variables to hold the output rates.
  // int xRate, yRate, zRate;
  // //Read the x,y and z output rates from the gyroscope.
  // xRate = readX();
  // yRate = readY();
  // zRate = readZ();
  // //Print the output rates to the terminal, seperated by a TAB character.
  // Serial.print(xRate);
  // Serial.print('\t');
  // Serial.print(yRate);
  // Serial.print('\t');
  // Serial.println(zRate);  

  // //Wait 10ms before reading the values again. (Remember, the output rate was set to 100hz and 1reading per 10ms = 100hz.)
  // delay(10);

}

/*********************** READING ACCELERATION ***********************/
/*    Reads Acceleration into Three Variables:  x, y and z          */

void readAccel(int *xyz){
	readAccel(xyz, xyz + 1, xyz + 2);
}

void readAccel(int *x, int *y, int *z) {
	readFromI2C(ADXL345_DATAX0, ADXL345_TO_READ, _buff);	// Read Accel Data from ADXL345

	// Each Axis @ All g Ranges: 10 Bit Resolution (2 Bytes)
	*x = (int16_t)((((int)_buff[1]) << 8) | _buff[0]);
	*y = (int16_t)((((int)_buff[3]) << 8) | _buff[2]);
	*z = (int16_t)((((int)_buff[5]) << 8) | _buff[4]);
}




/*************************** WRITE TO I2C ***************************/
/*      Start; Send Register Address; Send Value To Write; End      */
void writeToI2C(byte _address, byte _val) {
	Wire.beginTransmission(ADXL345_DEVICE);
	Wire.write(_address);
	Wire.write(_val);
	Wire.endTransmission();
}

/*************************** READ FROM I2C **************************/
/*                Start; Send Address To Read; End                  */
void readFromI2C(byte address, int num, byte _buff[]) {
	Wire.beginTransmission(ADXL345_DEVICE);
	Wire.write(address);
	Wire.endTransmission();

//	Wire.beginTransmission(ADXL345_DEVICE);
// Wire.reqeustFrom contains the beginTransmission and endTransmission in it. 
	Wire.requestFrom(ADXL345_DEVICE, num);  // Request 6 Bytes

	int i = 0;
	while(Wire.available())
	{
		_buff[i] = Wire.read();				// Receive Byte
		i++;
	}
}