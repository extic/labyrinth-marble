package extremely.game

import extremely.engine.Engine
import extremely.engine.defaultSettings
import extremely.game.logic.GameLogicFactory
import org.lwjgl.Version

fun main(args: Array<String>) {
    println(Version.getVersion())

    val settings = defaultSettings()

    val engine = Engine(settings, GameLogicFactory())
    engine.init()
    try {
        engine.run()
    } finally {
        engine.cleanup()
    }
//
//
//
//
//    val window = WindowManager(Consts.TITLE, 1600, 900, false)
//    val renderer = RenderManager(window)
//    val logic = GameLogic(window, renderer)
//    val engine = EngineManager(window, logic)
//
//    try {
//        engine.start()
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
}