package com.giaquino.cm.common.viewholder

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.giaquino.cm.R

class LoadingViewHolder(view: View) : ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): LoadingViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_list_loading,
                    parent, false)
            return LoadingViewHolder(view)
        }
    }
}