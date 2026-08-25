package com.homelaunch.assist

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.homelaunch.assist.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SHOW_SELECT_PROMPT = "show_select_prompt"
    }

    private lateinit var binding: ActivitySettingsBinding
    private var adapter: AppListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.settings_title)

        if (intent.getBooleanExtra(EXTRA_SHOW_SELECT_PROMPT, false)) {
            Toast.makeText(this, R.string.please_select_app, Toast.LENGTH_LONG).show()
        }

        val apps = loadLaunchableApps()
        val currentSelection = PrefsHelper.getTargetPackage(this)

        val listAdapter = AppListAdapter(apps, currentSelection) { selectedApp ->
            PrefsHelper.setTargetPackage(this, selectedApp.packageName)
            Toast.makeText(
                this,
                getString(R.string.app_selected, selectedApp.label),
                Toast.LENGTH_SHORT
            ).show()
        }
        adapter = listAdapter

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = listAdapter
    }

    private fun loadLaunchableApps(): List<AppInfo> {
        val pm = packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val resolvedApps = pm.queryIntentActivities(mainIntent, PackageManager.MATCH_ALL)

        return resolvedApps
            .asSequence()
            .filter { it.activityInfo.packageName != packageName }
            .map { resolveInfo ->
                AppInfo(
                    label = resolveInfo.loadLabel(pm).toString(),
                    packageName = resolveInfo.activityInfo.packageName,
                    icon = resolveInfo.loadIcon(pm)
                )
            }
            .distinctBy { it.packageName }
            .sortedBy { it.label.lowercase() }
            .toList()
    }
}
