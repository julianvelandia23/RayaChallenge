package org.julianvelandia.raya.data


sealed class DataError(message: String? = null, cause: Throwable? = null) :
    Throwable(message, cause) {
    data object NetworkError : DataError("Problema de conexión a internet.")
    data class ServerError(val code: Int, val serverMessage: String? = null) :
        DataError("Error del servidor (Código: $code). $serverMessage")

    data class ClientError(val code: Int, val clientMessage: String? = null) :
        DataError("Error en la solicitud (Código: $code). $clientMessage")

    data class ApiError(val apiMessage: String) : DataError(apiMessage)
    data object SerializationError : DataError("Error al procesar la respuesta del servidor.")
    data class UnknownError(val throwable: Throwable) :
        DataError("Ocurrió un error inesperado.", throwable)

    data class DatabaseError(val throwable: Throwable) :
        DataError("Error interactuando con la base de datos.", throwable)
}