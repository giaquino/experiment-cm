package com.giaquino.cm.model.response

import com.giaquino.cm.model.entity.Car
import com.giaquino.cm.model.entity.CarImage
import com.giaquino.cm.model.entity.CarWithImage
import com.giaquino.cm.model.entity.Message
import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException

/**
 * Model to match api response
 */
data class CarResponse(
        @SerializedName("success") val success: Boolean,
        @SerializedName("messages") val message: Message,
        @SerializedName("metadata") val metadata: CarResponseMetaData?) {

    fun toCarWithImage(): List<CarWithImage> {
        return metadata?.results?.map { result: ResponseCar ->
            val car = Car(id = result.id,
                    name = result.data.name,
                    brand = result.data.brand,
                    brandModel = result.data.brandModel,
                    originalPrice = result.data.price.price,
                    originalPriceCurrency = result.data.price.currency,
                    mileage = result.data.mileage,
                    premiumListing = result.data.premiumListing,
                    listingStart = result.data.listingStart)
            val images = result.images.map { image: ResponseCarImage ->
                CarImage(image.url, result.id)
            }
            CarWithImage(car).apply { this.images = images }
        } ?: emptyList()
    }
}

data class CarResponseMetaData(
        @SerializedName("results") val results: List<ResponseCar>)

data class ResponseCar(
        @SerializedName("id") val id: String,
        @SerializedName("data") val data: ResponseCarData,
        @SerializedName("images") val images: List<ResponseCarImage>)

data class ResponseCarData(
        @SerializedName("name") val name: String,
        @SerializedName("brand") val brand: String,
        @SerializedName("brand_model") val brandModel: String,
        @SerializedName("simples") val price: ResponseCarPrice,
        @SerializedName("mileage") val mileage: Long,
        @SerializedName("premium_listing") val premiumListing : Int,
        @SerializedName("listing_start") val listingStart: String)

data class ResponseCarImage(
        @SerializedName("url") val url: String)

data class ResponseCarPrice(
        val currency: String,
        val price: Double)

class ResponseCarPriceTypeAdapter : TypeAdapter<ResponseCarPrice>() {

    override fun read(reader: JsonReader): ResponseCarPrice {
        /* store values once found */
        var price: Double? = null
        var currency: String? = null

        /* counter if we're already at the end of the first object */
        var begin = 1
        var end = 0

        /* sku object */
        reader.beginObject()

        while (begin != end) {
            when (reader.peek()!!) {
                JsonToken.BEGIN_ARRAY -> throw IOException()
                JsonToken.END_ARRAY -> throw IOException()
                JsonToken.STRING -> reader.nextString()
                JsonToken.NUMBER -> reader.nextInt()
                JsonToken.BOOLEAN -> reader.nextBoolean()
                JsonToken.NULL -> reader.nextNull()
                JsonToken.END_DOCUMENT -> throw IOException()
                JsonToken.BEGIN_OBJECT -> {
                    begin += 1
                    reader.beginObject()
                }
                JsonToken.END_OBJECT -> {
                    end += 1
                    reader.endObject()
                }
                JsonToken.NAME -> {
                    when (reader.nextName()) {
                        "original_price_currency" -> currency = reader.nextString()
                        "original_price" -> price = reader.nextDouble()
                    }
                }
            }
        }
        return ResponseCarPrice(currency!!, price!!)
    }

    override fun write(writer: JsonWriter, value: ResponseCarPrice) {
    }
}