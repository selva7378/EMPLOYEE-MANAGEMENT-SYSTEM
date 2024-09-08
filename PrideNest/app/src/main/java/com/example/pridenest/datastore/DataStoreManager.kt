package com.example.pridenest.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")


class DataStoreManager(private val context: Context) {

    companion object {
        val LAST_LOGGED_IN_USER_ID_KEY = stringPreferencesKey("last_logged_in_user_id")
        val LAST_LOGGED_IN_ROLE_KEY = stringPreferencesKey("last_logged_in_role")
    }

    suspend fun saveLoginDetails(userId: String, role: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_LOGGED_IN_ROLE_KEY] = role

            preferences[LAST_LOGGED_IN_USER_ID_KEY] = userId
        }
        val loggedInUserId = lastLoggedInUserId.first()
        val loggedInRole = lastLoggedInRole.first()

        Log.i("datastore", "Last logged-in userId: $loggedInUserId")
        Log.i("datastore", "Last logged-in role: $loggedInRole")
    }

    val lastLoggedInUserId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[LAST_LOGGED_IN_USER_ID_KEY]
        }

    val lastLoggedInRole: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[LAST_LOGGED_IN_ROLE_KEY]
        }

    suspend fun clearLoginDetails() {
        context.dataStore.edit { preferences ->
            preferences.remove(LAST_LOGGED_IN_USER_ID_KEY)
            preferences.remove(LAST_LOGGED_IN_ROLE_KEY)
        }
    }
}
