package org.julianvelandia.raya.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

fun <T> Flow<T>.asResultFlow(): Flow<Result<T>> {
    return this
        .map { value ->
            Result.success(value)
        }
        .catch { exception ->
            val dataError = DataError.UnknownError(exception)
            emit(Result.failure(dataError))
        }
}

suspend fun <T> safeDBCall(
    block: suspend () -> T
): Result<T> {
    return try {
        val result = block()
        Result.success(result)
    } catch (e: Exception) {
        Result.failure(DataError.UnknownError(e))
    }
}