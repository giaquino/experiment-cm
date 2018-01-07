package com.giaquino.cm.common.viewholder

import android.databinding.ObservableField
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.giaquino.cm.databinding.ViewListRetryBinding

class RetryViewHolder(private val binding: ViewListRetryBinding) : ViewHolder(binding.root) {

    val message = ObservableField<String>()

    companion object {
        fun create(parent: ViewGroup): RetryViewHolder {
            val binding = ViewListRetryBinding.inflate(LayoutInflater.from(parent.context), parent,
                    false)
            return RetryViewHolder(binding)
        }
    }

    init {
        binding.vh = this
    }

    fun setMessage(message: String) {
        this.message.set(message)
        binding.executePendingBindings()
    }

    fun setOnRetryListener(listener: View.OnClickListener) {
        binding.root.setOnClickListener(listener)
    }
}