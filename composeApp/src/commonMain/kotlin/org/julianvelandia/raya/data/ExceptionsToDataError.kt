package org.julianvelandia.raya.data

import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException

suspend fun <T> safeCall(
    block: suspend () -> T
): Result<T> {
    return try {
        val result = block()
        Result.success(result)
    } catch (e: IOException) {
        Result.failure(DataError.NetworkError)
    } catch (e: ClientRequestException) {
        val code = e.response.status.value
        Result.failure(DataError.ClientError(code, e.message))
    } catch (e: ServerResponseException) {
        val code = e.response.status.value
        Result.failure(DataError.ServerError(code, e.message))
    } catch (e: SerializationException) {
        Result.failure(DataError.SerializationError)
    } catch (e: Exception) {
        Result.failure(DataError.UnknownError(e))
    }
}