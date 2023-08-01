package extremely.engine.utils

import extremely.engine.entities.Camera
import org.joml.Math.tan
import org.joml.Math.toRadians
import org.joml.Matrix4f
import org.joml.Vector3f

fun createTransformationMatrix(translation: Vector3f, rotation: Vector3f, scale: Float): Matrix4f =
    Matrix4f()
        .translation(translation)
//        .rotateX(toRadians(rotation.x))
//        .rotateY(toRadians(rotation.y))
//        .rotateZ(toRadians(rotation.z))
        .rotateX(rotation.x)
        .rotateY(rotation.y)
        .rotateZ(rotation.z)

        .scale(scale)

fun createProjectionMatrix(width: Int, height: Int, fov: Float, nearPlane: Float, farPlane: Float): Matrix4f {
    val aspectRatio = width.toFloat() / height.toFloat()
    val yScale = (1f / tan(toRadians(fov / 2f))) * aspectRatio
    val xScale = yScale / aspectRatio
    val frustumLength = farPlane - nearPlane

    return Matrix4f()
        .m00(xScale)
        .m11(yScale)
        .m22(-((farPlane + nearPlane) / frustumLength))
        .m23(-1f)
        .m32(-((2 * nearPlane * farPlane) / frustumLength))
        .m33(0f)
}

fun createViewMatrix(camera: Camera): Matrix4f =
    Matrix4f()
        .identity()
        .rotateX(toRadians(camera.rotation.x))
        .rotateY(toRadians(camera.rotation.y))
        .translate(-camera.position.x, -camera.position.y, -camera.position.z)

