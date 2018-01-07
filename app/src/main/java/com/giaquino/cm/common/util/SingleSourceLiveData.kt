package com.giaquino.cm.common.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
import java.util.concurrent.atomic.AtomicReference

class SingleSourceLiveData<T> : MediatorLiveData<T>() {

    private val atomicSource = AtomicReference<LiveData<*>?>(null)

    override fun <S : Any?> addSource(source: LiveData<S>, onChanged: Observer<S>) {
        if (atomicSource.compareAndSet(null, source)) super.addSource(source, onChanged)
    }

    override fun <S : Any?> removeSource(toRemote: LiveData<S>) {
        if (atomicSource.compareAndSet(toRemote, null)) super.removeSource(toRemote)
    }

    fun removeSource() {
        val source = atomicSource.getAndSet(null)
        if (source != null) super.removeSource(source)
    }
}