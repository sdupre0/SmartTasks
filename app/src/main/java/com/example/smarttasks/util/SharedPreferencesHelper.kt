package com.example.smarttasks.util

import android.content.Context
import androidx.preference.PreferenceManager

class SharedPreferencesHelper(context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    private val statusKey = "status"
    private val commentKey = "comment"

    fun saveTaskStatus(id: String, status: Status) {
        prefs.edit().putString(id + statusKey, status.name).apply()
    }

    fun getTaskStatus(id: String): Status {
        val prefString = prefs.getString(id + statusKey, "")
        return if (prefString.isNullOrEmpty()) {
            Status.Unresolved
        } else {
            Status.valueOf(prefString)
        }
    }

    fun saveTaskComment(id: String, comment: String) {
        prefs.edit().putString(id + commentKey, comment).apply()
    }

    fun getTaskComment(id: String): String? {
        return prefs.getString(id + commentKey, "")
    }
}