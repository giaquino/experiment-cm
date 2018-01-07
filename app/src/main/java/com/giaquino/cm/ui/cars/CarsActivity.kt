package com.giaquino.cm.ui.cars

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.support.v7.widget.RecyclerView.State
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.giaquino.cm.R
import com.giaquino.cm.common.app.BaseActivity
import com.giaquino.cm.databinding.CarsActivityBinding
import com.giaquino.cm.paging.NetworkStatus
import com.giaquino.cm.paging.NetworkStatus.FAILED
import com.giaquino.cm.paging.NetworkStatus.LOADING
import com.giaquino.cm.paging.PagingState
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class CarsActivity : BaseActivity() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private val vm by lazy(NONE) {
        ViewModelProviders.of(this, factory).get(CarsViewModel::class.java)
    }

    private val binding by lazy(NONE) {
        DataBindingUtil.setContentView<CarsActivityBinding>(this, R.layout.cars_activity)
    }

    private val adapter by lazy(NONE) {
        CarsAdapter({ vm.retry() })
    }

    private val layoutManager by lazy(NONE) {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeRecyclerView()
        initializeRefreshIndicator()
        vm.cars.observe(this, Observer { adapter.setList(it) })
        vm.pagingState.observe(this, Observer { handlePagingState(it!!) })
        vm.refreshState.observe(this, Observer { handleRefreshState(it!!) })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sort, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sortby_price_asc -> vm.sortByPriceAscending()
            R.id.menu_sortby_price_desc -> vm.sortByPriceDescending()
            R.id.menu_sortby_mileage_asc -> vm.sortByMileageAscending()
            R.id.menu_sortby_mileage_desc -> vm.sortByMileageDescending()
            R.id.menu_sortby_date_asc -> vm.sortByDateAscending()
            R.id.menu_sortby_date_desc -> vm.sortByDateDescending()
            else -> return super.onOptionsItemSelected(item)
        }
        adapter.setLoadingSuccess() // hide list loading indicator if user sort
        return true
    }

    private fun initializeRecyclerView() {
        binding.carsActivityList.adapter = adapter
        binding.carsActivityList.layoutManager = layoutManager
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f,
                resources.displayMetrics).toInt()
        binding.carsActivityList.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(rect: Rect, view: View, list: RecyclerView, state: State) {
                rect.top = margin
                rect.bottom = margin
            }
        })
    }

    private fun initializeRefreshIndicator() {
        binding.carsActivityRefresh.setColorSchemeResources(R.color.primary, R.color.accent)
        binding.carsActivityRefresh.setOnRefreshListener {
            vm.refresh()
            adapter.setLoadingSuccess() // hide list loading indicator if user refresh
        }
    }

    private fun handleRefreshState(state: PagingState) {
        binding.carsActivityRefresh.isRefreshing = state.status == LOADING
        if (state.status == FAILED) {
            Snackbar.make(binding.root, state.message!!, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun handlePagingState(state: PagingState) {
        when (state.status) {
            NetworkStatus.LOADING -> adapter.setLoading()
            NetworkStatus.SUCCESS -> adapter.setLoadingSuccess()
            NetworkStatus.FAILED -> adapter.setLoadingFailed(state.message!!)
        }
    }
}