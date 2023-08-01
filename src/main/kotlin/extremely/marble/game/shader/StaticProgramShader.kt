package extremely.game.shader

import extremely.engine.ShaderProgram
import extremely.engine.entities.Camera
import extremely.engine.utils.createViewMatrix
import org.joml.Matrix4f

class StaticProgramShader() : ShaderProgram() {

    private var transformationMatrixLocation = 0
    private var projectionMatrixLocation = 0
    private var viewMatrixLocation = 0

    init {
        init(VERTEX_FILE, FRAGMENT_FILE)
    }

    override fun bindAttributes() {
        super.bindAttribute(0, "position")
        super.bindAttribute(1, "textureCoords")
    }

    override fun updateAllUniformLocations() {
        transformationMatrixLocation = super.getUniformLocation("transformationMatrix")
        projectionMatrixLocation = super.getUniformLocation("projectionMatrix")
        viewMatrixLocation = super.getUniformLocation("viewMatrix")
    }

    fun loadTransformationMatrix(matrix: Matrix4f) {
        setUniform(transformationMatrixLocation, matrix)
    }

    fun loadProjectionMatrix(matrix: Matrix4f) {
        setUniform(projectionMatrixLocation, matrix)
    }

    fun loadViewMatrix(camera: Camera) {
        setUniform(viewMatrixLocation, createViewMatrix(camera))
    }

    companion object {
        const val VERTEX_FILE = "../src/main/kotlin/extremely/marble/game/shader/shader.vert"
        const val FRAGMENT_FILE = "../src/main/kotlin/extremely/marble/game/shader/shader.frag"
    }
}
