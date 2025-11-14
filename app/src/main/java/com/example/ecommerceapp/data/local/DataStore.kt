import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Extension property for Context to get the DataStore instance
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "on_boarding_pref")

class DataStoreRepository(context: Context) {

    // Define the preference key inside the companion object for clarity
    companion object {
        private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("on_boarding_completed")
    }

    private val dataStore = context.dataStore

    // Save the onboarding state (completed or not) in the DataStore
    suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED_KEY] = completed
        }
    }

    // Read and return the onboarding completion state as a Flow
    fun readOnBoardingState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                // Emit an empty preferences if there's an IOException
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                // Return false if the key isn't present, indicating onboarding is not completed
                preferences[ONBOARDING_COMPLETED_KEY] ?: false
            }
    }
}