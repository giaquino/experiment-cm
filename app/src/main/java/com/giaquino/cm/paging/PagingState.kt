package com.giaquino.cm.paging

import com.giaquino.cm.paging.NetworkStatus.FAILED
import com.giaquino.cm.paging.NetworkStatus.LOADING
import com.giaquino.cm.paging.NetworkStatus.SUCCESS


@Suppress("DataClassPrivateConstructor")
data class PagingState private constructor(val status: NetworkStatus, val message: String? = null) {

    companion object {
        val success = PagingState(SUCCESS)
        val loading = PagingState(LOADING)
        fun failed(message: String) = PagingState(FAILED, message)
    }
}