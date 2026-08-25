package com.homelaunch.assist

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.service.voice.VoiceInteractionSession
import android.view.View
import android.view.ViewGroup

/**
 * This is what actually runs when the user long-presses Home (or uses
 * whatever gesture is bound to the Digital Assistant).
 *
 * There is intentionally no visible UI: we show a 0-size transparent
 * view (the framework requires *some* content view to exist), and in
 * onShow() we immediately either:
 *   - launch the app the user picked in Settings, or
 *   - if none is picked / it was uninstalled, open our own Settings
 *     screen and ask the user to pick one.
 * Either way, the session then hides and finishes itself so nothing
 * lingers on the back stack.
 */
class HomeLaunchSession(context: Context) : VoiceInteractionSession(context) {

    override fun onCreateContentView(): View {
        return View(context).apply {
            setBackgroundColor(Color.TRANSPARENT)
            layoutParams = ViewGroup.LayoutParams(0, 0)
        }
    }

    override fun onShow(args: Bundle?, showFlags: Int) {
        super.onShow(args, showFlags)
        launchTargetOrSettings()
    }

    private fun launchTargetOrSettings() {
        val packageName = PrefsHelper.getTargetPackage(context)
        val launchIntent = packageName?.let { pkg ->
            context.packageManager.getLaunchIntentForPackage(pkg)
        }

        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(launchIntent)
        } else {
            // Nothing selected yet, or the previously selected app is gone.
            val settingsIntent = Intent(context, SettingsActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(SettingsActivity.EXTRA_SHOW_SELECT_PROMPT, true)
            }
            context.startActivity(settingsIntent)
        }

        hide()
        finish()
    }
}
