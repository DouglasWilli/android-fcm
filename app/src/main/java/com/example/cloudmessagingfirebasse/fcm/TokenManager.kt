package com.example.cloudmessagingfirebasse.fcm

import android.content.Context
import androidx.preference.PreferenceManager

class TokenManager(private val context: Context) {

    fun updateToken(refreshedToken: String) {
        setRegistrationId(refreshedToken)
        setSentToServer(false)
    }

    private fun setRegistrationId(value: String?) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(REGISTRATION_ID, value)
                .apply()
    }

    private fun setSentToServer(value: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(SENT_TO_SERVER, value)
                .apply()
    }

    companion object {
        private const val REGISTRATION_ID = "registrationId"
        private const val SENT_TO_SERVER = "hasSentToServer"
    }
}