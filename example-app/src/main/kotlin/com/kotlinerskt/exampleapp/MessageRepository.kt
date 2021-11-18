package com.kotlinerskt.exampleapp

import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun sendMessage(message: String)

    suspend fun getMessages(): Flow<String>
}