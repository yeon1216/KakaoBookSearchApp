package com.example.searchbookapp.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class JobResult<out R> {

    data class Success<out T>(val data: T) : JobResult<T>()
    data class Error(val exception: Exception) : JobResult<Nothing>()
    object Loading : JobResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val JobResult<*>.succeeded
    get() = this is JobResult.Success && data != null

inline fun <T> JobResult<T>.onSuccess(action: (T) -> Unit): JobResult<T> {
    if (this is JobResult.Success) action(data)
    return this
}

inline fun <T> JobResult<T>.onError(action: (Exception) -> Unit): JobResult<T> {
    if (this is JobResult.Error) action(exception)
    return this
}

