package com.giaquino.cm.paging

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import io.reactivex.Single

/**
 * Extension of [PageKeyedDataSource] to manage and request data from network
 * while handling saving and loading to database.
 */
abstract class PagingDataSource<VALUE>(
        private val loadInitialState: MutableLiveData<PagingState>,
        private val loadAfterState: MutableLiveData<PagingState>
) : PageKeyedDataSource<Int, VALUE>() {

    private var initialLoadFailed: Boolean = false

    private var lastFailingLoad: (() -> Any)? = null

    abstract fun createCall(page: Int, size: Int): Single<List<VALUE>>

    abstract fun saveToDatabase(values: List<VALUE>, initial: Boolean)

    abstract fun loadFromDatabase(page: Int, pageSize: Int): List<VALUE>

    abstract fun getErrorMessage(exception: Throwable): String

    fun refresh() = invalidate()

    fun retry() {
        lastFailingLoad?.invoke()
        lastFailingLoad = null
    }

    /**
     * we don't how carmudi sort their items so we wont sort items coming from network
     */
    fun isDataFromNetwork() = !initialLoadFailed

    override fun loadInitial(params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, VALUE>) {
        loadInitialState.postValue(PagingState.loading)

        createCall(1, params.requestedLoadSize).subscribe({
            initialLoadFailed = false
            if (it.isEmpty()) {
                callback.onResult(it, null, null)
                loadInitialState.postValue(PagingState.success)
            } else {
                saveToDatabase(it, true)
                callback.onResult(loadFromDatabase(1, params.requestedLoadSize), null, 2)
                loadInitialState.postValue(PagingState.success)
            }
        }, {
            /* return cache on failure instead */
            initialLoadFailed = true
            lastFailingLoad = { refresh() }
            val cache = loadFromDatabase(1, params.requestedLoadSize)
            callback.onResult(cache, null, if (cache.isEmpty()) null else 2)
            loadInitialState.postValue(PagingState.failed(getErrorMessage(it)))
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, VALUE>) {
        /* if initial load failed, just return cache until the user refresh */
        if (initialLoadFailed) {
            val cache = loadFromDatabase(params.key, params.requestedLoadSize)
            callback.onResult(cache, if (cache.isEmpty()) null else params.key + 1)
        } else {
            loadAfterState.postValue(PagingState.loading)
            createCall(params.key, params.requestedLoadSize).subscribe({
                if (it.isEmpty()) {
                    callback.onResult(it, null)
                    loadAfterState.postValue(PagingState.success)
                } else {
                    saveToDatabase(it, false)
                    callback.onResult(loadFromDatabase(params.key, params.requestedLoadSize),
                            params.key + 1)
                    loadAfterState.postValue(PagingState.success)
                }
            }, {
                lastFailingLoad = { loadAfter(params, callback) }
                loadAfterState.postValue(PagingState.failed(getErrorMessage(it)))
            })
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, VALUE>) {
    }
}
