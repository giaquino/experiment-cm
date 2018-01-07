package com.giaquino.cm.di

import com.giaquino.cm.model.api.CmApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = arrayOf(
        DatabaseModule::class, NetworkModule::class,
        ActivityModule::class, ViewModelModule::class))
class ApplicationModule {

    @Singleton @Provides
    fun provideCmApi(okhttp: OkHttpClient, gson: Gson) = Retrofit.Builder().client(okhttp)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .baseUrl("https://www.carmudi.com.ph/api/")
            .build()
            .create(CmApi::class.java)
}