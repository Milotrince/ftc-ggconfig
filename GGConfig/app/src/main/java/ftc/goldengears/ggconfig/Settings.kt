package ftc.goldengears.ggconfig

import android.content.Context
import android.os.Environment
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class Settings {

    val externalDirectory: String = Environment.getExternalStorageDirectory().absolutePath

    private val tag = "GG_Settings"
    private var path: File
    private lateinit var settings: JSONObject

    private val defaultConfigsPath = "${externalDirectory}/Robot/"

    var configsPath: String
    var defaultConfig: String

    constructor (context: Context) {
        path = context.applicationContext.filesDir
        val file = File(path, "settings.json")

       try {
            if (!file.exists()) {
                file.parentFile.mkdirs()
            }
           if (file.createNewFile()) {
                Log.d(tag, "New file created")
                var defaultSettingsJson = JSONObject()
               defaultSettingsJson.put("configsPath", defaultConfigsPath)
                file.writeText(defaultSettingsJson.toString(2))
            }
            val fileContents = file.readText()
            Log.d(tag, fileContents)
            settings = JSONObject(fileContents)
        } catch (ex: Exception) {
            Log.d(tag, "Could not read settings.json")
        }

        configsPath = try { settings.getString("configsPath") } catch (e: JSONException) { defaultConfigsPath }
        defaultConfig = try { settings.getString("defaultConfig") } catch (e: JSONException) { "" }

        write()
    }

    fun write() {
        File(path, "settings.json").writeText(settings.toString(2))
    }

}