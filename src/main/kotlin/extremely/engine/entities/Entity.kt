package extremely.engine.entities

import extremely.core.models.TexturedModel
import org.joml.Vector3f

class Entity(val model: TexturedModel, val position: Vector3f, val rotation: Vector3f, var scale: Float) {

    fun translate(by: Vector3f) {
        position.add(by)
    }

    fun rotate(by: Vector3f) {
        rotation.add(by)
    }

    fun scale(by: Float) {
        scale += by
    }
}