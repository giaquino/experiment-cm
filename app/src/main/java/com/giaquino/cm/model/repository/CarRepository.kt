package com.giaquino.cm.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.giaquino.cm.model.api.CmApi
import com.giaquino.cm.model.api.ErrorHandler
import com.giaquino.cm.model.database.CarDao
import com.giaquino.cm.model.datasource.CarDataSource
import com.giaquino.cm.model.entity.Car.SortBy
import com.giaquino.cm.model.entity.CarWithImage
import com.giaquino.cm.paging.PagingResource
import com.giaquino.cm.paging.PagingState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarRepository @Inject constructor(
        private val api: CmApi,
        private val dao: CarDao,
        private val errorHandler: ErrorHandler) {


    fun getCarWithImage(id: String): LiveData<CarWithImage> {
        return dao.getCarWithImage(id)
    }

    fun getCars(sortBy: SortBy): PagingResource<CarWithImage> {
        val refreshState = MutableLiveData<PagingState>()
        val pagingState = MutableLiveData<PagingState>()

        val dataSourceFactory = object : DataSource.Factory<Int, CarWithImage> {
            var dataSource: CarDataSource? = null
            override fun create(): DataSource<Int, CarWithImage> {
                dataSource = CarDataSource(
                        api = api,
                        dao = dao,
                        sortBy = sortBy,
                        errorHandler = errorHandler,
                        loadInitialState = refreshState,
                        loadAfterState = pagingState)
                return dataSource!!
            }
        }

        val config = PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(10)
                .setEnablePlaceholders(false)
                .build()
        val list = LivePagedListBuilder<Int, CarWithImage>(dataSourceFactory, config).build()

        return PagingResource<CarWithImage>(
                list = list,
                refreshState = refreshState,
                pagingState = pagingState,
                onRefresh = {
                    dataSourceFactory.dataSource?.refresh()
                },
                onRetry = {
                    dataSourceFactory.dataSource?.retry()
                })
    }
}