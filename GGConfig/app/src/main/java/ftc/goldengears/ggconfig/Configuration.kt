package ftc.goldengears.ggconfig

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class Configuration {

    private val TAG = "GG_Configuration"
    private lateinit var configuration: JSONObject
    lateinit var path: String
    lateinit var name: String
    lateinit var commands: JSONArray
    lateinit var properties: JSONObject

    constructor() {
        name = "NewConfig.json"
        commands = JSONArray()
        properties = JSONObject()
    }

    constructor(settings: Settings) {
        Configuration(settings, settings.defaultConfig)
    }

    constructor(settings: Settings, name: String) {
        Configuration(File(settings.configsPath, name))
    }

    constructor(file: File) {
        path = file.path.substringBeforeLast('/')
        name = file.path.substringAfterLast('/')
        try {
            if (!file.exists()) {
                file.parentFile.mkdirs()
            }
            if (file.createNewFile()) {
                Log.d(TAG, "New file created")
                configuration = JSONObject()
                    .put("commands", JSONArray())
                    .put("properties", JSONObject())
                write()
            }

            val fileContents = file.readText()
            Log.d(TAG, fileContents)
            configuration = JSONObject(fileContents)
            commands = configuration.getJSONArray("commands")
            properties = configuration.getJSONObject("properties")

        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.e(TAG, ex.toString())
        }
    }

    fun write() {
        File(path, name).writeText(configuration.toString(2))
    }
}