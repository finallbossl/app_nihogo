package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nihongomaster.data.repository.AuthRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _loginSucceeded = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSucceeded

    private val _registerSucceeded = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSucceeded

    fun login(email: String, password: String) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val result = authRepository.login(email, password)
                result.onSuccess {
                    _loginSucceeded.value = true
                }.onFailure { exception ->
                    _error.value = exception.message ?: "Đăng nhập thất bại"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi kết nối mạng"
            } finally {
                _loading.value = false
            }
        }
    }

    fun register(email: String, password: String, displayName: String?) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val result = authRepository.register(email, password, displayName)
                result.onSuccess {
                    _registerSucceeded.value = true
                }.onFailure { exception ->
                    _error.value = exception.message ?: "Đăng ký thất bại"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi kết nối mạng"
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
            } catch (e: Exception) {
                // Silent logout - don't show error to user
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}