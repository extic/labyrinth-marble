package extremely.game.logic

import extremely.core.models.TexturedModel
import extremely.engine.*
import extremely.engine.entities.Camera
import extremely.engine.entities.Entity
import extremely.engine.textures.Texture
import extremely.engine.utils.createProjectionMatrix
import extremely.game.shader.StaticProgramShader
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import kotlin.math.max
import kotlin.math.min


class GameLogicFactory : LogicFactory {
    override fun create(engine: Engine): Logic =
        GameLogic(engine)
}

class GameLogic(private val engine: Engine) : Logic {

    private val loader = Loader()

    private lateinit var window: Window
    private lateinit var shader: StaticProgramShader
    lateinit var entity: Entity
    private lateinit var camera: Camera

    override fun init(window: Window, settings: Settings, renderer: Renderer) {
        this.window = window

        val vertices = floatArrayOf(
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f,
        )
        val indices = intArrayOf(0, 1, 3, 3, 1, 2)

        val textureCoordinates = floatArrayOf(
            0f, 0f,
            0f, 1f,
            1f, 1f,
            1f, 0f,
        )

//        val model = loader.loadToVao(vertices, textureCoordinates, indices)
        val objLoader = ObjLoader()
        val model = objLoader.loadObjModel("C:\\programming\\labyrinth-marble\\assets\\board3.obj", loader)
//        val texture = Texture(loader.loadTexture("C:\\programming\\test_lwjgl\\run\\textures\\sample.png"))
        val texture = Texture(loader.loadTexture("C:\\programming\\labyrinth-marble\\assets\\board.png"))
        val texturedModel = TexturedModel(model, texture)
        entity = Entity(texturedModel, Vector3f(0f, 0f, -12f), Vector3f(0f, 0f, 0f), 1f);

        val projectionMatrix =
            createProjectionMatrix(settings.window.width, settings.window.height, FOV, NEAR_PLANE, FAR_PLANE)
        shader = StaticProgramShader()
        shader.start()
        shader.loadProjectionMatrix(projectionMatrix)
        shader.stop()

        camera = Camera()
        renderer.init(entity, shader, projectionMatrix)
    }

    override fun input() {
        if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            camera.speed -= 0.002f
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            camera.speed += 0.0002f
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            camera.position.x -= 0.02f
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            camera.position.x += 0.02f
        }
    }

    override fun update() {
        camera.speed += -camera.speed / 10000f
        camera.speed = max(min(camera.speed, 0.0002f), -0.0002f);


        val vec = Vector3f()


        camera.direction.mul(camera.speed, vec)
        camera.position.add(vec)
//
//        camera.position.mulAdd(camera.speed, camera.direction)
//        camera.direction.mulAdd((camera.speed)
//
//        camera.position.add(camera.direction)
    }

    override fun render(renderer: Renderer) {
        renderer.prepare()
        shader.start()
        shader.loadViewMatrix(camera)
        renderer.render()
        shader.stop()
    }

    override fun cleanup() {

    }

    companion object {
        val FOV = 70f
        val NEAR_PLANE = 0.1f;
        val FAR_PLANE = 1000f
    }
}

