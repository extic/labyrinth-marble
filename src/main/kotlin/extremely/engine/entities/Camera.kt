package extremely.engine.entities

import org.joml.Vector3f

class Camera() {
    val position = Vector3f(0f, 0f, 10f)
    val rotation = Vector3f(0f, 0f, 0f) //pitch, yaw, roll
    val direction = Vector3f(0f, 0f, -1f)
    var speed = 0f
}
