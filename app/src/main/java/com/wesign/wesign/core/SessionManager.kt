package com.wesign.wesign.core

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.dataStore by preferencesDataStore(
        name = PREFERENCES
    )

    private val tokenPreferences = stringPreferencesKey(AUTH_TOKEN)

    suspend fun saveToken(value: String) {
        context.dataStore.edit {
            it[tokenPreferences] = value
        }
    }

    fun getToken() = context.dataStore.data.map {
        it[tokenPreferences]
    }

}