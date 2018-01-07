package com.giaquino.cm.paging

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

class PagingResource<T>(
        val list: LiveData<PagedList<T>>,
        val pagingState: LiveData<PagingState>,
        val refreshState: LiveData<PagingState>,
        private val onRefresh: () -> Unit,
        private val onRetry: () -> Unit) {

    fun refresh() = onRefresh.invoke()
    fun retry() = onRetry.invoke()
}
