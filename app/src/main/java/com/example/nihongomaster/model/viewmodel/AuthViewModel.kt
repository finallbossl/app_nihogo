package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    init {
        checkCurrentUser()
    }
    
    private fun checkCurrentUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _authState.value = AuthState.Authenticated(currentUser)
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }
    
    fun signInWithGoogle(idToken: String) {
        _isLoading.value = true
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        
        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                _isLoading.value = false
                val user = result.user
                if (user != null) {
                    _authState.value = AuthState.Authenticated(user)
                    // Create profile in background
                    createOrUpdateUserProfile(user)
                } else {
                    _authState.value = AuthState.Error("Không thể lấy thông tin người dùng")
                }
            }
            .addOnFailureListener { exception ->
                _isLoading.value = false
                val errorMsg = when (exception) {
                    is com.google.firebase.auth.FirebaseAuthException -> 
                        when (exception.errorCode) {
                            "ERROR_NETWORK_REQUEST_FAILED" -> "Lỗi kết nối mạng"
                            "ERROR_USER_DISABLED" -> "Tài khoản đã bị vô hiệu hóa"
                            else -> exception.localizedMessage ?: "Đăng nhập thất bại"
                        }
                    else -> exception.localizedMessage ?: "Đăng nhập thất bại"
                }
                _errorMessage.value = errorMsg
                _authState.value = AuthState.Error(errorMsg)
            }
    }
    
    private fun createOrUpdateUserProfile(user: FirebaseUser) {
        val userProfile = hashMapOf(
            "uid" to user.uid,
            "email" to user.email,
            "displayName" to user.displayName,
            "photoUrl" to user.photoUrl?.toString(),
            "lastLoginAt" to System.currentTimeMillis()
        )
        
        // Use merge to avoid overwriting existing data
        firestore.collection("users").document(user.uid)
            .set(userProfile, com.google.firebase.firestore.SetOptions.merge())
            .addOnFailureListener { e ->
                // Don't show error for profile creation failure
                android.util.Log.w("AuthViewModel", "Profile update failed: ${e.message}")
            }
    }
    
    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}