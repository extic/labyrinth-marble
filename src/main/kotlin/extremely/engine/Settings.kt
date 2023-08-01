package extremely.engine

class Settings {
    val window: WindowSettings = defaultWindowSettings()
}

data class WindowSettings(
    var width: Int,
    var height: Int,
    var title: String,
    var vSync: Boolean
)

fun defaultSettings(): Settings
    = Settings()

private fun defaultWindowSettings() =
    WindowSettings(1600, 900, "Game Title", false)