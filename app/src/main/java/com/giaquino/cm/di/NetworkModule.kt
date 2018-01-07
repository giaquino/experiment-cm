package com.giaquino.cm.di

import com.giaquino.cm.model.response.ResponseCarPrice
import com.giaquino.cm.model.response.ResponseCarPriceTypeAdapter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import timber.log.Timber
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton @Provides
    fun provideGson() = GsonBuilder()
            .registerTypeAdapter(object : TypeToken<ResponseCarPrice>() {}.type,
                    ResponseCarPriceTypeAdapter())
            .create()

    @Singleton @Provides
    fun provideOkhttp(): OkHttpClient {
        val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.d(it)
        }).apply { level = BODY }
        return OkHttpClient.Builder().addInterceptor(logger).build()
    }
}