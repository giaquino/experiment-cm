package com.giaquino.cm.model.datasource

import android.arch.lifecycle.MutableLiveData
import com.giaquino.cm.model.api.CmApi
import com.giaquino.cm.model.api.ErrorHandler
import com.giaquino.cm.model.database.CarDao
import com.giaquino.cm.model.entity.Car
import com.giaquino.cm.model.entity.Car.SortBy
import com.giaquino.cm.model.entity.CarWithImage
import com.giaquino.cm.paging.PagingDataSource
import com.giaquino.cm.paging.PagingState
import io.reactivex.Single
import timber.log.Timber

class CarDataSource(
        private val api: CmApi,
        private val dao: CarDao,
        private val sortBy: SortBy,
        private val errorHandler: ErrorHandler,
        loadInitialState: MutableLiveData<PagingState>,
        loadAfterState: MutableLiveData<PagingState>
) : PagingDataSource<CarWithImage>(loadInitialState, loadAfterState) {

    override fun createCall(page: Int, size: Int): Single<List<CarWithImage>> {
        return api.getCars(page, size, sortBy.apiSortKey).map({
            if (it.success) {
                it.toCarWithImage()
            } else {
                throw Exception(it.message.error!![0])
            }
        })
    }

    override fun saveToDatabase(values: List<CarWithImage>, initial: Boolean) {
        if (initial) {
            dao.deleteAndInsertCarWithImage(values)
        } else {
            dao.insertCarWithImage(values)
        }
    }

    override fun loadFromDatabase(page: Int, pageSize: Int): List<CarWithImage> {
        val offset = (page - 1) * pageSize
        if (isDataFromNetwork()) {
            return dao.getCarWithImage(pageSize, offset) // don't sort if its from network
        }
        return when (sortBy) {
            Car.SortBy.PRICE_ASC -> dao.getCarWithImageByPriceAsc(pageSize, offset)
            Car.SortBy.PRICE_DESC -> dao.getCarWithImageByPriceDesc(pageSize, offset)
            Car.SortBy.MILEAGE_ASC -> dao.getCarWithImageByMileageAsc(page, offset)
            Car.SortBy.MILEAGE_DESC -> dao.getCarWithImageByMileageDesc(page, offset)
            Car.SortBy.DATE_ASC -> dao.getCarWithImageByDateAsc(page, offset)
            Car.SortBy.DATE_DESC -> dao.getCarWithImageByDateDesc(page, offset)
        }
    }

    override fun getErrorMessage(exception: Throwable): String {
        Timber.e(exception)
        return errorHandler.getUserFacingMessage(exception)
    }
}