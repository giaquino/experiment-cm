package com.giaquino.cm.ui.cars

import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.DiffCallback
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup
import com.giaquino.cm.R
import com.giaquino.cm.common.viewholder.LoadingViewHolder
import com.giaquino.cm.common.viewholder.RetryViewHolder
import com.giaquino.cm.model.entity.CarWithImage


@Suppress("ConvertSecondaryConstructorToPrimary")
class CarsAdapter(
        private val retryCallback: () -> Unit
) : PagedListAdapter<CarWithImage, ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffCallback<CarWithImage>() {
            override fun areItemsTheSame(oldItem: CarWithImage, newItem: CarWithImage): Boolean {
                return oldItem.car.id == newItem.car.id
            }

            override fun areContentsTheSame(oldItem: CarWithImage, newItem: CarWithImage): Boolean {
                return oldItem.car.id == newItem.car.id
            }
        }
    }

    private var loading = false
    private var retry = false
    private var retryMessage = ""

    override fun getItemViewType(position: Int): Int {
        return when {
            loading && position == itemCount - 1 -> R.layout.view_list_loading
            retry && position == itemCount - 1 -> R.layout.view_list_retry
            else -> R.layout.cars_item
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (loading || retry) 1 else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is CarsViewHolder) {
            getItem(position)?.apply { holder.bind(this) }
        } else if (holder is RetryViewHolder) {
            holder.setMessage(retryMessage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == R.layout.view_list_loading) {
            return LoadingViewHolder.create(parent)
        } else if (viewType == R.layout.view_list_retry) {
            return RetryViewHolder.create(parent).apply {
                setOnRetryListener(View.OnClickListener { retryCallback.invoke() })
            }
        }
        return CarsViewHolder.create(parent)
    }

    fun setLoading() {
        loading = true
        if (retry) {
            retry = false
            notifyItemChanged(itemCount - 1)
        } else {
            notifyItemInserted(itemCount - 1)
        }
    }

    fun setLoadingFailed(message: String) {
        retryMessage = message
        retry = true
        if (loading) {
            loading = false
            notifyItemChanged(itemCount - 1)
        } else {
            notifyItemInserted(itemCount - 1)
        }
    }

    fun setLoadingSuccess() {
        if (loading || retry) {
            loading = false
            retry = false
            notifyItemRemoved(itemCount)
        }
    }
}