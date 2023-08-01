package extremely.marble



import extremely.engine.Engine
import extremely.engine.defaultSettings
import extremely.game.logic.GameLogicFactory
import extremely.marble.server.Server
import org.lwjgl.Version
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    println(Version.getVersion())

    val settings = defaultSettings()
    val engine = Engine(settings, GameLogicFactory())
    engine.init()

    thread { Server(engine).run() }

    try {
        engine.run()
    } finally {
        engine.cleanup()
    }
}