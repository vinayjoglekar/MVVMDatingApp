package com.jovinz.datingapp.network

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

open class BaseRepository {
    suspend inline fun <reified T> safeApiCallWithErrorData(
        dispatcher: CoroutineDispatcher,
        crossinline apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(dispatcher) {
            try {
                Resource.success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> Resource.error(throwable, null)
                    is HttpException -> {
                        val code = throwable.code()
                        val error: T? = convertErrorBody(throwable)
                        Resource.error(throwable, error)
                    }
                    is Exception -> Resource.error(throwable, null)
                    else -> Resource.error(throwable, null)
                }
            }
        }
    }

    inline fun <reified T> convertErrorBody(throwable: HttpException): T? {
        return try {
            throwable.response()?.errorBody()?.let {
                val errorJsonString = throwable.response()?.errorBody()?.string()
                return Gson().fromJson(errorJsonString, T::class.java)
            }
        } catch (exception: Exception) {
            null
        }
    }

}