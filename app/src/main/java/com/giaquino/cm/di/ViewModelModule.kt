package com.giaquino.cm.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.giaquino.cm.di.viewmodel.ViewModelDaggerFactory
import com.giaquino.cm.di.viewmodel.ViewModelKey
import com.giaquino.cm.ui.cars.CarsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(value = CarsViewModel::class)
    abstract fun bind(vm: CarsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelDaggerFactory): ViewModelProvider.Factory
}