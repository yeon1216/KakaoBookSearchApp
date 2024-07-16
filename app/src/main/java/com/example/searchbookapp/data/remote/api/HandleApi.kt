package com.example.searchbookapp.data.remote.api

import com.example.searchbookapp.data.JobResult
import java.io.IOException

internal inline fun <T> handleApi(transform: () -> T): JobResult<T> = try {
    JobResult.Success(transform.invoke())
} catch (e: Exception) {
    when (e) {
        is InvalidJwtTokenException -> JobResult.Error(InvalidJwtTokenException(e.cause, e.url))
        is AccountNotFoundException -> JobResult.Error(AccountNotFoundException(e.cause, e.url))
        is BadRequestException -> JobResult.Error(BadRequestException(e.cause, e.url))
        is InternalServerErrorException -> JobResult.Error(InternalServerErrorException(e.cause, e.url))
        is ServerNotFoundException -> JobResult.Error(ServerNotFoundException(e.cause, e.url))
        else -> JobResult.Error(e)
    }
}

class InvalidJwtTokenException(e: Throwable?, val url: String? = null) : IOException(e)
class AccountNotFoundException(e: Throwable?, val url: String? = null) : IOException(e)
class ServerNotFoundException(e: Throwable?, val url: String? = null) : IOException(e)
class InternalServerErrorException(e: Throwable?, val url: String? = null) : IOException(e)
class BadRequestException(e: Throwable?, val url: String? = null) : IOException(e)
