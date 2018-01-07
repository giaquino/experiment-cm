package com.giaquino.cm.ui.cars

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import com.giaquino.cm.databinding.CarsItemBinding
import com.giaquino.cm.model.entity.CarWithImage

class CarsViewHolder(private val binding: CarsItemBinding) : ViewHolder(binding.root) {

    val url = ObservableField<String>("")
    val name = ObservableField<String>("")
    val price = ObservableField<String>("")
    val mileage = ObservableField<String>("")
    val premium = ObservableBoolean(false)

    companion object {
        fun create(parent: ViewGroup): CarsViewHolder {
            return CarsViewHolder(
                    CarsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    init {
        binding.vh = this
    }

    fun bind(data: CarWithImage) {
        url.set(data.images[0].url)
        name.set(data.car.name)
        price.set("${data.car.originalPriceCurrency} ${String.format("%,.2f",
                data.car.originalPrice)}")
        mileage.set("Mileage ${String.format("%,d", data.car.mileage)} km")
        premium.set(data.car.premiumListing == 1)
        binding.executePendingBindings()
    }
}