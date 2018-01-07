package com.giaquino.cm.di

import com.giaquino.cm.ui.cars.CarsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope @ContributesAndroidInjector
    abstract fun contributeCarsActivity(): CarsActivity
}