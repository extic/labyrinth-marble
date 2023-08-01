package extremely.marble.server

import extremely.engine.Engine
import extremely.game.logic.GameLogic
import org.joml.Math
import org.joml.Vector3f
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread

class Server(private val engine: Engine) {
    fun run() {
        val server = ServerSocket(9999)
        println("Server is running on port ${server.localPort}")

        while (true) {
            val client = server.accept()
            println("Client connected: ${client.inetAddress.hostAddress}")

            // Run client in it's own thread.
            thread { ClientHandler(client, engine).run() }
        }
    }
}

class ClientHandler(client: Socket, private val engine: Engine) {
    private val client: Socket = client
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()
    private var running: Boolean = false
    private val vectorList = ArrayList<Vector3f>()
    private val averageSize = 130

    fun run() {
        running = true
        while (running) {
            try {
                val text = reader.nextLine()
                val values = text.split(",")

                val inVec = Vector3f(values[0].toFloat(), values[1].toFloat(), values[2].toFloat()).normalize()

                vectorList.add(inVec)
                if (vectorList.size > averageSize) {
                    vectorList.removeAt(0)
                }
                var reduce = vectorList
                    .reduce { v1, v2 -> Vector3f(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z) }
                reduce = Vector3f(reduce.x / averageSize.toFloat(), reduce.y / averageSize.toFloat(), reduce.z / averageSize.toFloat())
                reduce.normalize()



                val roll = Math.atan2(reduce.y, reduce.z) * 57.3f
                val pitch = Math.atan2((-reduce.x) , Math.sqrt(reduce.y * reduce.y + reduce.z * reduce.z)) * 57.3f
                val yaw = 0f

//
//                val alpha = org.joml.Math.acos((vecA.x * vecB.x + vecA.y * vecB.y) / (org.joml.Math.sqrt((vecA.x * vecA.x + vecA.y * vecA.y) * org.joml.Math.sqrt((vecB.x * vecB.x + vecB.y * vecB.y)))))
//                val beta = org.joml.Math.acos((vecA.x * vecB.x + vecA.z * vecB.z) / (org.joml.Math.sqrt((vecA.x * vecA.x + vecA.z * vecA.z) * org.joml.Math.sqrt((vecB.x * vecB.x + vecB.z * vecB.z)))))
//                val gamma = org.joml.Math.acos((vecA.z * vecB.z + vecA.y * vecB.y) / (org.joml.Math.sqrt((vecA.z * vecA.z + vecA.y * vecA.y) * org.joml.Math.sqrt((vecB.z * vecB.z + vecB.y * vecB.y)))))

//                (ax*bx + ay*by + az*bz)
//                --------------------------------------------------------
//                sqrt(ax*ax + ay*ay + az*az) * sqrt(bx*bx + by*by + bz*bz)

//                println(alpha.toString() + ", " + beta.toString() + ", " + gamma.toString())





//                val angle = acos(vecA.dot(vecB).toDouble()).toFloat()
//                println("Angle between the two: " + angle + "(" + Math.toDegrees(angle.toDouble()) + "°)")
//
//                val rotationAxis = Vector3f()
//                vecA.cross(vecB, rotationAxis).normalize()
//
//                val rotatedVector = Vector3f()
//                vecA.rotateAxis(angle, rotationAxis.x, rotationAxis.y, rotationAxis.z, rotatedVector).normalize()
//
//                println("Rotated Vector: " + rotatedVector.toString(FORMAT))

//                Thread.sleep(1000);

//                val newZ = Vector3f(values[0].toFloat(), values[1].toFloat(), values[2].toFloat()).normalize()
//                val newX = Vector3f()
//                newZ.cross(Vector3f(0f, 0f, 1f), newX)
//                val newY = Vector3f()
//                newX.cross(newZ, newY)
//
//                val alpha = org.joml.
//
//                val up = Vector3f(0f, 0f, 1f)
//                var normalize = rotation.cross(up).normalize()




//                vectorList.add(rotation)
//                if (vectorList.size > averageSize) {
//                    vectorList.removeAt(0)
//                }
//                var reduce = vectorList
//                    .reduce { v1, v2 -> Vector3f(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z) }
//                reduce = Vector3f(reduce.x / averageSize.toFloat(), reduce.y / averageSize.toFloat(), reduce.z / averageSize.toFloat())
//                //reduce.normalize()
//
//                val factor = 1000f
//                reduce = Vector3f(Math.round(reduce.x * factor) / factor, Math.round(reduce.y * factor) / factor, Math.round(reduce.z * factor) / factor)
//
//
//                reduce.x = values[0].toFloat() / 256f
//                reduce.y = values[1].toFloat() / 256f
//                reduce.z = 1f
//                reduce.normalize()
//                print(reduce.x)
//                print("\t")
//                print(reduce.y)
//                print("\t")
//                println(reduce.z)


                val gameLogic = engine.logic as GameLogic
//                gameLogic.entity.rotation.x = rotatedVector.x
//                gameLogic.entity.rotation.y = rotatedVector.y
//                gameLogic.entity.rotation.z = rotatedVector.z
                gameLogic.entity.rotation.x = Math.toRadians(-pitch)
                gameLogic.entity.rotation.y = Math.toRadians(roll)
                gameLogic.entity.rotation.z = Math.toRadians(yaw)

//                println(text);
//                if (text == "EXIT"){
//                    shutdown()
//                    continue
//                }
//
//                val values = text.split(' ')
//                val result = calculator.calculate(values[0].toInt(), values[1].toInt(), values[2])
//                write(result)
            } catch (ex: Exception) {
                shutdown()
            } finally {

            }
        }
    }

    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

    private fun shutdown() {
        running = false
        client.close()
        println("${client.inetAddress.hostAddress} closed the connection")
    }

}
