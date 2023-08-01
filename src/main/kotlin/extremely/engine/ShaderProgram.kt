package extremely.engine

import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.system.MemoryStack
import java.nio.file.Files
import kotlin.io.path.Path

abstract class ShaderProgram() {
    private var programId: Int = 0
    private var vertexShaderId: Int = 0
    private var fragmentShaderId: Int = 0

    fun init(vertexFile: String, fragmentFile: String) {
        vertexShaderId = loadShader(vertexFile, GL20.GL_VERTEX_SHADER)
        fragmentShaderId = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER)
        programId = GL20.glCreateProgram()

        GL20.glAttachShader(programId, vertexShaderId)
        GL20.glAttachShader(programId, fragmentShaderId)
        this.bindAttributes()
        GL20.glLinkProgram(programId)
        GL20.glValidateProgram(programId)

        updateAllUniformLocations()
    }

    fun start() {
        GL20.glUseProgram(programId)
    }

    fun stop() {
        GL20.glUseProgram(0)
    }

    fun cleanUp() {
        stop()

        GL20.glDetachShader(programId, vertexShaderId)
        GL20.glDetachShader(programId, fragmentShaderId)
        GL20.glDeleteShader(vertexShaderId)
        GL20.glDeleteShader(fragmentShaderId)
        GL20.glDeleteProgram(programId)
    }

    protected abstract fun bindAttributes()

    protected fun bindAttribute(attribute: Int, variableName: String) {
        GL20.glBindAttribLocation(programId, attribute, variableName)
    }

    protected abstract fun updateAllUniformLocations()

    protected fun getUniformLocation(uniformName: String): Int =
        GL20.glGetUniformLocation(programId, uniformName)

    protected fun setUniform(location: Int, value: Float) {
        GL20.glUniform1f(location, value)
    }

    protected fun setUniform(location: Int, vector: Vector3f) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z)
    }

    protected fun setUniform(location: Int, value: Boolean) {
        GL20.glUniform1f(location, if (value) 1f else 0f)
    }

    protected fun setUniform(location: Int, matrix: Matrix4f) {
        MemoryStack.stackPush().use {
            GL20.glUniformMatrix4fv(location, false, matrix.get(it.mallocFloat(16)))
        }
    }
}

private fun loadShader(file: String, type: Int): Int {
    val shaderSource = Files.readString(Path(file))
    val shaderId = GL20.glCreateShader(type)
    GL20.glShaderSource(shaderId, shaderSource)
    GL20.glCompileShader(shaderId)
    if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
        throw RuntimeException("Could not compile shader code - " + GL20.glGetShaderInfoLog(shaderId, 500))
    }
    return shaderId
}