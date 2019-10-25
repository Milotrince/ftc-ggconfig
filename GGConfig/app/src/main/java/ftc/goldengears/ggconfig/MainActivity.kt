package ftc.goldengears.ggconfig

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.net.Uri
import android.util.Log
import android.widget.*
import org.json.JSONObject
import java.io.File


class MainActivity : AppCompatActivity() {
    private val READ_WRITE_PERMISSION_CODE = 1
    private val FILE_EXPLORER_REQUEST_CODE = 2
    private lateinit var fieldView: FieldView
    private lateinit var settings: Settings
    private lateinit var configuration: Configuration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar_main))

        fieldView = findViewById(R.id.field)
        checkPermissions()

        supportActionBar?.title = configuration.name
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_open -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                val uri = Uri.parse(settings.configsPath)
                intent.data = uri
                intent.type = "application/json"
                try {
                    startActivityForResult(intent, FILE_EXPLORER_REQUEST_CODE)
                } catch (ex: ActivityNotFoundException){
                    Toast.makeText(this, "Please install a File Manager", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.action_new_config -> {
                // simple dialog with file name
            }
            R.id.action_settings -> {
                SettingsDialog().show(supportFragmentManager, "SettingsDialog")

            }
            R.id.action_about -> {
                // simple dialog with GG logo and info
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun addCommandButton(view: View) {
        // Dialog for adding new command
    }

    fun initSettingsAndConfiguration() {
        settings = Settings(this)
        configuration = if (settings.defaultConfig != "") Configuration(settings) else Configuration()
        loadConfiguration()
    }

    fun loadConfiguration() {
        val tableLayout = findViewById<TableLayout>(R.id.commands_table)

        for (i in 0 until configuration.commands.length()) {
            val command = configuration.commands[i] as JSONObject

            val tableRow = TableRow(this)

            val stepTextView = TextView(this)
            stepTextView.text = i.toString()

            val nameTextView = TextView(this)
            nameTextView.text = command.getString("command")

            val propertiesTextView = TextView(this)
            var properties = ""
            val keys = command.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = command.getString(key)
                properties += "${key}: ${value}, "
            }
            propertiesTextView.text = properties

            tableRow.addView(stepTextView)
            tableRow.addView(nameTextView)
            tableRow.addView(propertiesTextView)

            tableLayout.addView(tableRow)
        }

//        tableLayout.layoutParams =
//            TableRow.LayoutParams(
//                TableRow.LayoutParams.MATCH_PARENT,
//                TableRow.LayoutParams.WRAP_CONTENT
//            )

    }

    fun checkPermissions() {
        var read = Manifest.permission.READ_EXTERNAL_STORAGE
        var write = Manifest.permission.WRITE_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(this, read) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, write) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, read) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, write)) {
                AlertDialog.Builder(this)
                    .setMessage("Permission is needed to read and write files for editing config JSON files on the phone.")
                    .setPositiveButton(android.R.string.yes) { dialog, which ->
                        ActivityCompat.requestPermissions(this, arrayOf(read, write), READ_WRITE_PERMISSION_CODE)
                    }.show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(read, write), READ_WRITE_PERMISSION_CODE)
            }
        } else {
            initSettingsAndConfiguration()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_WRITE_PERMISSION_CODE  -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initSettingsAndConfiguration()
                } else {
                    checkPermissions()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
         when (requestCode) {
            FILE_EXPLORER_REQUEST_CODE-> {
                if (resultCode == RESULT_OK) {
                    var path = data?.data?.path

//                    val file = File(path)
//                    Log.d("%%D", file.readText())
//                    val newConfiguration = Configuration(file)
//                    Log.d("%%D", newConfiguration.name)

                    Toast.makeText(this, path, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Could not open file", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
