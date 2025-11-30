package com.example.ecommerceapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.ecommerceapp.data.models.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    init {
        fetchUserDetails()
    }
    enum class PasswordChangeResult {
        SUCCESS,
        INVALID_CREDENTIAL,
        REQUIRES_RECENT_LOGIN,
        GENERIC_ERROR
    }


    fun fetchUserDetails() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            _currentUser.value = null
            return
        }

        _isLoading.value = true
        usersCollection.document(uid).get()
            .addOnSuccessListener { document ->
                _isLoading.value = false
                if (document.exists()) {
                    _currentUser.value = document.toObject(User::class.java)
                } else {
                    _currentUser.value = User(uid = uid, email = auth.currentUser?.email ?: "N/A")
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
                _currentUser.value = User(uid = uid, email = auth.currentUser?.email ?: "N/A")
            }
    }
    suspend fun updateUserDetails(user: User): Boolean {
        if (user.uid.isBlank()) return false

        _isSaving.value = true
        return try {
            val updates = hashMapOf(
                "username" to user.username,
                "phone" to user.phone,
                "address" to user.address
            )

            usersCollection.document(user.uid).update(updates as Map<String, Any>).await()

            _currentUser.value = user

            true
        } catch (e: Exception) {
            false
        } finally {
            _isSaving.value = false
        }
    }
    suspend fun changePassword(currentPassword: String, newPassword: String): PasswordChangeResult {
        val user = auth.currentUser
        val email = user?.email

        if (user == null || email.isNullOrBlank()) {
            return PasswordChangeResult.GENERIC_ERROR
        }

        // 1. Create credentials using email and current password
        val credential = EmailAuthProvider.getCredential(email, currentPassword)

        return try {
            // 2. Re-authenticate the user
            user.reauthenticate(credential).await()

            // 3. Update the password
            user.updatePassword(newPassword).await()

            PasswordChangeResult.SUCCESS
        } catch (e: Exception) {
            // 4. Handle specific Firebase exceptions
            when (e) {
                is FirebaseAuthRecentLoginRequiredException -> PasswordChangeResult.REQUIRES_RECENT_LOGIN
                is FirebaseException -> {
                    // Check for invalid credentials (wrong current password)
                    if (e.message?.contains("INVALID_CREDENTIAL") == true) {
                        PasswordChangeResult.INVALID_CREDENTIAL
                    } else {
                        PasswordChangeResult.GENERIC_ERROR
                    }
                }

                else -> PasswordChangeResult.GENERIC_ERROR
            }
        }
    }
}