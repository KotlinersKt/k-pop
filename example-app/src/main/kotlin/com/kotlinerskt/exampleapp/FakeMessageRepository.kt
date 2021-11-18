package com.kotlinerskt.exampleapp

import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CancellationException

class FakeMessageRepository : MessageRepository {
    override suspend fun sendMessage(message: String) {

    }

    override suspend fun getMessages(): Flow<String> =
        flow {
            runBlocking {
                FakeData.messages.forEach {
                    kotlinx.coroutines.delay(1000)
                    emit("Some nothing text!")
                }
                this.cancel(CancellationException("no more messages"))
            }
        }
}

object FakeData {
    val messages = listOf(
        "Some",
        "Nothing",
        "message",
        ":",
        "D",
    )
}
