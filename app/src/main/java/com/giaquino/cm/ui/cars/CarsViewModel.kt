package com.giaquino.cm.ui.cars

import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import com.giaquino.cm.common.util.SingleSourceLiveData
import com.giaquino.cm.model.entity.Car.SortBy
import com.giaquino.cm.model.entity.Car.SortBy.DATE_ASC
import com.giaquino.cm.model.entity.Car.SortBy.DATE_DESC
import com.giaquino.cm.model.entity.Car.SortBy.MILEAGE_ASC
import com.giaquino.cm.model.entity.Car.SortBy.MILEAGE_DESC
import com.giaquino.cm.model.entity.Car.SortBy.PRICE_ASC
import com.giaquino.cm.model.entity.Car.SortBy.PRICE_DESC
import com.giaquino.cm.model.entity.CarWithImage
import com.giaquino.cm.model.repository.CarRepository
import com.giaquino.cm.paging.PagingState
import javax.inject.Inject

class CarsViewModel @Inject constructor(private val repository: CarRepository) : ViewModel() {

    val cars = SingleSourceLiveData<PagedList<CarWithImage>>()
    val pagingState = SingleSourceLiveData<PagingState>()
    val refreshState = SingleSourceLiveData<PagingState>()

    private var resource = repository.getCars(PRICE_ASC)

    init {
        bindToResource()
    }

    fun refresh() = resource.refresh()

    fun retry() = resource.retry()

    fun sortByPriceAscending() = sortBy(PRICE_ASC)

    fun sortByPriceDescending() = sortBy(PRICE_DESC)

    fun sortByMileageAscending() = sortBy(MILEAGE_ASC)

    fun sortByMileageDescending() = sortBy(MILEAGE_DESC)

    fun sortByDateAscending() = sortBy(DATE_ASC)

    fun sortByDateDescending() = sortBy(DATE_DESC)

    private fun sortBy(sortBy: SortBy) {
        resource = repository.getCars(sortBy)
        bindToResource()
    }

    private fun bindToResource() {
        cars.removeSource()
        pagingState.removeSource()
        refreshState.removeSource()
        cars.addSource(resource.list, { cars.postValue(it) })
        pagingState.addSource(resource.pagingState, { pagingState.postValue(it) })
        refreshState.addSource(resource.refreshState, { refreshState.postValue(it) })
    }
}