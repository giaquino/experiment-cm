package com.giaquino.cm.model.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.giaquino.cm.model.entity.Car
import com.giaquino.cm.model.entity.CarImage
import com.giaquino.cm.model.entity.CarWithImage

@Dao
abstract class CarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCars(cars: List<Car>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCarImages(carImages: List<CarImage>)

    @Query("DELETE FROM ${Car.TABLE_NAME}")
    abstract fun delete()

    @Transaction
    @Query("SELECT * FROM ${Car.TABLE_NAME} WHERE ${Car.KEY_ID} = :id LIMIT 1")
    abstract fun getCarWithImage(id: String): LiveData<CarWithImage>

    @Transaction
    @Query("SELECT * FROM ${Car.TABLE_NAME} LIMIT :limit OFFSET :offset")
    abstract fun getCarWithImage(limit: Int, offset: Int): List<CarWithImage>

    @Transaction
    @Query("SELECT * FROM ${Car.TABLE_NAME} ORDER BY ${Car.KEY_PREMIUM_LISTING} DESC, ${Car.KEY_ORIGINAL_PRICE} ASC LIMIT :limit OFFSET :offset")
    abstract fun getCarWithImageByPriceAsc(limit: Int, offset: Int): List<CarWithImage>

    @Transaction
    @Query("SELECT * FROM ${Car.TABLE_NAME} ORDER BY ${Car.KEY_PREMIUM_LISTING} DESC, ${Car.KEY_ORIGINAL_PRICE} DESC LIMIT :limit OFFSET :offset")
    abstract fun getCarWithImageByPriceDesc(limit: Int, offset: Int): List<CarWithImage>


    @Transaction
    @Query("SELECT * FROM ${Car.TABLE_NAME} ORDER BY ${Car.KEY_PREMIUM_LISTING} DESC, ${Car.KEY_MILEAGE} ASC LIMIT :limit OFFSET :offset")
    abstract fun getCarWithImageByMileageAsc(limit: Int, offset: Int): List<CarWithImage>

    @Transaction
    @Query("SELECT * FROM ${Car.TABLE_NAME} ORDER BY ${Car.KEY_PREMIUM_LISTING} DESC, ${Car.KEY_MILEAGE} DESC LIMIT :limit OFFSET :offset")
    abstract fun getCarWithImageByMileageDesc(limit: Int, offset: Int): List<CarWithImage>

    @Transaction
    @Query("SELECT * FROM ${Car.TABLE_NAME} ORDER BY ${Car.KEY_PREMIUM_LISTING} DESC, ${Car.KEY_LISTING_START} ASC LIMIT :limit OFFSET :offset")
    abstract fun getCarWithImageByDateAsc(limit: Int, offset: Int): List<CarWithImage>

    @Transaction
    @Query("SELECT * FROM ${Car.TABLE_NAME} ORDER BY ${Car.KEY_PREMIUM_LISTING} DESC, ${Car.KEY_LISTING_START} DESC LIMIT :limit OFFSET :offset")
    abstract fun getCarWithImageByDateDesc(limit: Int, offset: Int): List<CarWithImage>

    /**
     * Insert both [Car] and [CarImage]
     */
    @Transaction
    open fun insertCarWithImage(cars: List<CarWithImage>) {
        insertCars(cars.map { it.car }.toList())
        insertCarImages(cars.map { it.images }.flatten())
    }

    /**
     * Delete all entries and insert both [Car] and [CarImage]
     */
    @Transaction
    open fun deleteAndInsertCarWithImage(cars: List<CarWithImage>) {
        delete()
        insertCars(cars.map { it.car }.toList())
        insertCarImages(cars.map { it.images }.flatten())
    }
}