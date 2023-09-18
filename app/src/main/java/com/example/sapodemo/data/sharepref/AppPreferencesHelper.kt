package com.example.sapodemo.data.sharepref

import android.content.Context
import android.content.SharedPreferences

class AppPreferencesHelper(context: Context) : PreferencesHelper {

    companion object {
        const val PREF_KEY_SELECTION_TYPE = "PREF_KEY_SELECTION_TYPE"
        const val PREF_FILE_NAME = "app_setting"
    }

    private val sharedPreferences: SharedPreferences? = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)


    override fun getSelectionType(): Boolean {
        return sharedPreferences?.getBoolean(PREF_KEY_SELECTION_TYPE, false) ?: false
    }

    override fun setSelectionType(type: Boolean) {
        sharedPreferences?.edit()?.putBoolean(PREF_KEY_SELECTION_TYPE, type)?.apply()
    }
}