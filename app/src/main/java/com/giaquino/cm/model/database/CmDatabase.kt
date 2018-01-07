package com.giaquino.cm.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.giaquino.cm.model.entity.Car
import com.giaquino.cm.model.entity.CarImage

@Database(entities = arrayOf(Car::class, CarImage::class), version = 1)
abstract class CmDatabase : RoomDatabase() {

    abstract fun carDao(): CarDao

    companion object {
        fun create(context: Context): CmDatabase {
            return Room.databaseBuilder(context, CmDatabase::class.java, "cm.db").build()
        }
    }
}