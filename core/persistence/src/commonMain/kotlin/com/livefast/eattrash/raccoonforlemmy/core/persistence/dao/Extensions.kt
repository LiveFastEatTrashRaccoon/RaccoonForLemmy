package com.livefast.eattrash.raccoonforlemmy.core.persistence.dao

import app.cash.sqldelight.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Observe the results of a query returning a list of items.
 */
internal fun <T : Any> Query<T>.observeMany(): Flow<List<T>> = callbackFlow {
    fun fetchResult() {
        val result = executeAsList()
        trySend(result)
    }

    val resultListener = object : Query.Listener {
        override fun queryResultsChanged() {
            fetchResult()
        }
    }
    fetchResult()
    addListener(resultListener)

    awaitClose {
        removeListener(resultListener)
    }
}

/**
 * Observe the results of a query returning a single item (or null if no item satisfies the criteria).
 */
internal fun <T : Any> Query<T>.observeOne(): Flow<T?> = callbackFlow {
    fun fetchResult() {
        val result = executeAsOneOrNull()
        trySend(result)
    }

    val resultListener = object : Query.Listener {
        override fun queryResultsChanged() {
            fetchResult()
        }
    }
    fetchResult()
    addListener(resultListener)

    awaitClose {
        removeListener(resultListener)
    }
}
