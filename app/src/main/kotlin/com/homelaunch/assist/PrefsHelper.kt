package com.homelaunch.assist

import android.content.Context

/**
 * Small helper around SharedPreferences that stores which app package
 * should be launched when the user invokes the assistant (Home button).
 */
object PrefsHelper {

    private const val PREFS_NAME = "home_launch_prefs"
    private const val KEY_TARGET_PACKAGE = "target_package"

    fun getTargetPackage(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_TARGET_PACKAGE, null)
    }

    fun setTargetPackage(context: Context, packageName: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_TARGET_PACKAGE, packageName).apply()
    }
}
