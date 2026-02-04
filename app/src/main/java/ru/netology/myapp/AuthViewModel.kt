package ru.netology.myapp.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val _authorized = MutableStateFlow(false)
    val authorized: StateFlow<Boolean> = _authorized.asStateFlow()

    fun login() {
        _authorized.value = true
    }

    fun logout() {
        _authorized.value = false
    }

    fun toggle() {
        _authorized.value = !_authorized.value
    }
}
