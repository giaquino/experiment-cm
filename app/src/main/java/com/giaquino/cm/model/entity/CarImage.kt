package com.giaquino.cm.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = CarImage.TABLE_NAME,
        foreignKeys = arrayOf(
                ForeignKey(entity = Car::class,
                        parentColumns = arrayOf(Car.KEY_ID),
                        childColumns = arrayOf(CarImage.KEY_PRODUCT_ID),
                        onDelete = ForeignKey.CASCADE)))
data class CarImage(
        @PrimaryKey @ColumnInfo(name = KEY_URL) val url: String,
        @ColumnInfo(name = KEY_PRODUCT_ID) val carId: String) {

    companion object {
        const val TABLE_NAME = "car_images"
        const val KEY_PRODUCT_ID = "car_id"
        const val KEY_URL = "url"
    }
}