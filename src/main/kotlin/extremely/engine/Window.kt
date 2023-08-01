package extremely.engine

import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryUtil

class Window(private val settings: Settings) {
    private var windowHandle = MemoryUtil.NULL

    fun init() {
        if (!GLFW.glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }

        GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2)
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE)

        val winSettings = settings.window
        var maximized = false;
        if (winSettings.width == 0 || winSettings.height == 0) {
            winSettings.width = 100
            winSettings.height = 100
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE)
            maximized = true;
        }

        windowHandle = GLFW.glfwCreateWindow(winSettings.width, winSettings.height, winSettings.title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (windowHandle == MemoryUtil.NULL) {
            throw RuntimeException("Failed to create GLFW window")
        }

        GLFW.glfwSetFramebufferSizeCallback(windowHandle) { _, width, height ->
            winSettings.width = width
            winSettings.height = height
        }

        GLFW.glfwSetKeyCallback(windowHandle) { window, key, _, action, _ ->
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(window, true)
            }
        }

        if (maximized) {
            GLFW.glfwMaximizeWindow(windowHandle)
        } else {
            val vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())
                ?: throw RuntimeException("Cannot get current video mode")

            GLFW.glfwSetWindowPos(windowHandle, (vidMode.width() - winSettings.width) / 2, (vidMode.height() - winSettings.height) / 2)
        }

        GLFW.glfwMakeContextCurrent(windowHandle)

        if (winSettings.vSync) {
            GLFW.glfwSwapInterval(1)
        }

        GLFW.glfwShowWindow(windowHandle)

        GL.createCapabilities()

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glEnable(GL11.GL_STENCIL_TEST)
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glCullFace(GL11.GL_BACK)

        GLFW.glfwSetWindowTitle(windowHandle, winSettings.title)
    }

    fun update() {
        GLFW.glfwSwapBuffers(windowHandle)
        GLFW.glfwPollEvents()
    }

    fun cleanup() {
        GLFW.glfwDestroyWindow(windowHandle)
    }

    fun isKeyPressed(keycode: Int): Boolean =
        GLFW.glfwGetKey(windowHandle, keycode) == GLFW.GLFW_PRESS;

    fun windowShouldClose(): Boolean =
        GLFW.glfwWindowShouldClose(windowHandle)

}