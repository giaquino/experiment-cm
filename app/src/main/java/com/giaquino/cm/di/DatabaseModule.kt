package com.giaquino.cm.di

import com.giaquino.cm.CmApplication
import com.giaquino.cm.model.database.CmDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton @Provides
    fun provideDatabase(application: CmApplication) = CmDatabase.create(application)

    @Singleton @Provides
    fun provideCarDao(db: CmDatabase) = db.carDao()
}