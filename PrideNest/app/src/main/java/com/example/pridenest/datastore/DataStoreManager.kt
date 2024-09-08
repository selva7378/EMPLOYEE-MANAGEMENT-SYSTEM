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

enum class UserRole {
    ADMIN, MANAGER, EMPLOYEE
}

class DataStoreManager(private val context: Context) {

    companion object {
        val LAST_LOGGED_IN_USER_ID_KEY = stringPreferencesKey("last_logged_in_user_id")
        val LAST_LOGGED_IN_ROLE_KEY = stringPreferencesKey("last_logged_in_role")
    }

    // Save login details with userId
    suspend fun saveLoginDetails(userId: String, role: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_LOGGED_IN_ROLE_KEY] = role

            preferences[LAST_LOGGED_IN_USER_ID_KEY] = userId
        }
        // Collecting the values from the Flow to log them
        val loggedInUserId = lastLoggedInUserId.first() // first() collects the latest value of the Flow
        val loggedInRole = lastLoggedInRole.first()

        // Log the values after saving
        Log.i("datastore", "Last logged-in userId: $loggedInUserId")
        Log.i("datastore", "Last logged-in role: $loggedInRole")
    }

    // Retrieve the last logged-in userId
    val lastLoggedInUserId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[LAST_LOGGED_IN_USER_ID_KEY]
        }

    // Retrieve the last logged-in role
    val lastLoggedInRole: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[LAST_LOGGED_IN_ROLE_KEY]
        }

    // Clear login details (on logout)
    suspend fun clearLoginDetails() {
        context.dataStore.edit { preferences ->
            preferences.remove(LAST_LOGGED_IN_USER_ID_KEY)
            preferences.remove(LAST_LOGGED_IN_ROLE_KEY)
        }
    }
}
