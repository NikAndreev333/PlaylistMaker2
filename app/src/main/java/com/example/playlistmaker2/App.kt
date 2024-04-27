package com.example.playlistmaker2

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
const val SHARED_PREFERENCES = "app_shared_preferences"
const val DARK_THEME_KEY = "dark_theme_key"
class App: Application() {
    var darkTheme = false
    lateinit var appPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        appPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        darkTheme = appPrefs.getBoolean(DARK_THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        appPrefs.edit()
            .putBoolean(DARK_THEME_KEY, darkTheme)
            .apply()
    }
}