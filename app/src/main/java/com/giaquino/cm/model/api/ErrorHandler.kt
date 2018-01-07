package com.giaquino.cm.model.api

import com.giaquino.cm.CmApplication
import com.giaquino.cm.R
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor(val application: CmApplication) {

    fun getUserFacingMessage(exception: Throwable): String {
        return when (exception) {
            is UnknownHostException -> application.getString(R.string.error_message_no_internet)
            is SocketTimeoutException -> application.getString(R.string.error_message_no_internet)
            is HttpException -> exception.message()
            else -> application.getString(R.string.error_message_default)
        }
    }
}