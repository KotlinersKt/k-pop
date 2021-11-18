package com.kotlinerskt.exampleapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository by lazy {
        FakeMessageRepository()
    }

    private val mutableMessage: MutableStateFlow<String> = MutableStateFlow("")
    val message: StateFlow<String>
        get() = mutableMessage

    fun fetchNewMessages() {
        viewModelScope.launch {
            repository.getMessages()
                .catch { it.printStackTrace() }
                .collect { mutableMessage.emit(it) }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            repository.sendMessage(message = message)
        }
    }
}