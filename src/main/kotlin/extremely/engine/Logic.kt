package extremely.engine

interface Logic {
    fun init(window: Window, settings: Settings, renderer: Renderer)

    fun input()

    fun update()

    fun render(renderer: Renderer)

    fun cleanup()
}

interface LogicFactory {
    fun create(engine: Engine): Logic
}