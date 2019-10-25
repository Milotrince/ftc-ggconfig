package ftc.goldengears.ggconfig

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast

class SettingsDialog : DialogFragment() {

    private lateinit var toolbar: Toolbar
    private lateinit var settings: Settings

    private lateinit var configsPathText: EditText
    private lateinit var defaultConfigText: EditText


    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settings = Settings((activity as AppCompatActivity).applicationContext)

        val rootView = inflater.inflate(R.layout.dialog_settings, container, false)
        toolbar = rootView.findViewById(R.id.toolbar_settings)
        toolbar.title = getString(R.string.settings)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { dismiss() }

        setHasOptionsMenu(true)

        return rootView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        activity?.menuInflater?.inflate(R.menu.menu_settings, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configsPathText = view.findViewById(R.id.configsPath)
        configsPathText.setText(settings.configsPath)

        defaultConfigText = view.findViewById(R.id.defaultConfig)
        defaultConfigText.setText(settings.defaultConfig)
//        defaultConfigText.addTextChangedListener(object: TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable?) {
////                settings.configsPath = s.toString()
//                Log.d(tag, "AFTER TEXT CHANGE : ${s.toString()}")
//            }
//        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                settings.write()
                Toast.makeText(context, R.string.saved, Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}