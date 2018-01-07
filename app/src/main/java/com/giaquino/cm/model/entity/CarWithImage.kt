package com.giaquino.cm.model.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class CarWithImage(@Embedded val car: Car) {
    @Relation(parentColumn = Car.KEY_ID, entityColumn = CarImage.KEY_PRODUCT_ID)
    var images: List<CarImage> = emptyList()
}