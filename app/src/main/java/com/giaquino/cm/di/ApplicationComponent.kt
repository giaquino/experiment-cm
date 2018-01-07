package com.giaquino.cm.di

import com.giaquino.cm.CmApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(ApplicationModule::class, AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class))
interface ApplicationComponent {

    fun inject(application: CmApplication)

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent
        @BindsInstance fun application(application: CmApplication): Builder
    }
}