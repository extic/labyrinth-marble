package extremely.engine

import extremely.engine.entities.Entity
import extremely.engine.utils.createTransformationMatrix
import extremely.game.shader.StaticProgramShader
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

class Renderer(private val window: Window) {

    lateinit var entity: Entity
    lateinit var shader: StaticProgramShader

    fun init(entity: Entity, shader: StaticProgramShader, projectionMatrix: Matrix4f) {
        this.entity = entity
        this.shader = shader
    }

    fun prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glClearColor(0f, 0.3f, 0f, 1f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
    }

    fun render() {
        val model = entity.model.rawModel

        GL30.glBindVertexArray(model.vaoId)
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)

        val transformationMatrix = createTransformationMatrix(entity.position, entity.rotation, entity.scale)
        shader.loadTransformationMatrix(transformationMatrix)

        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.model.texture.textureId)
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.vertexCount, GL11.GL_UNSIGNED_INT, 0)
        GL20.glDisableVertexAttribArray(0)
        GL20.glDisableVertexAttribArray(1)
        GL30.glBindVertexArray(0)
    }

    fun cleanup() {

    }
}