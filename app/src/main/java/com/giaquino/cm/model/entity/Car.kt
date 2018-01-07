package com.giaquino.cm.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = Car.TABLE_NAME)
data class Car(
        @PrimaryKey @ColumnInfo(name = KEY_ID) val id: String,
        @ColumnInfo(name = KEY_NAME) val name: String,
        @ColumnInfo(name = KEY_BRAND) val brand: String,
        @ColumnInfo(name = KEY_BRAND_MODEL) val brandModel: String,
        @ColumnInfo(name = KEY_ORIGINAL_PRICE) val originalPrice: Double,
        @ColumnInfo(name = KEY_ORIGINAL_PRICE_CURRENCY) val originalPriceCurrency: String,
        @ColumnInfo(name = KEY_MILEAGE) val mileage: Long,
        @ColumnInfo(name = KEY_PREMIUM_LISTING) val premiumListing: Int,
        @ColumnInfo(name = KEY_LISTING_START) val listingStart: String) {

    enum class SortBy(val apiSortKey: String) {
        PRICE_ASC("price-low"),
        PRICE_DESC("price-high"),
        MILEAGE_ASC("mileage-low"),
        MILEAGE_DESC("mileage-high"),
        DATE_ASC("oldest"),
        DATE_DESC("newest")
    }

    companion object {
        const val TABLE_NAME = "cars"
        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_BRAND = "brand"
        const val KEY_BRAND_MODEL = "brand_model"
        const val KEY_ORIGINAL_PRICE = "original_price"
        const val KEY_ORIGINAL_PRICE_CURRENCY = "original_price_currency"
        const val KEY_MILEAGE = "mileage"
        const val KEY_PREMIUM_LISTING = "premium_listing"
        const val KEY_LISTING_START = "listing_start"
    }
}