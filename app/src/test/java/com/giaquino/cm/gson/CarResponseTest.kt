package com.giaquino.cm.gson

import com.giaquino.cm.TestUtil
import com.giaquino.cm.model.response.CarResponse
import com.giaquino.cm.model.response.ResponseCarPrice
import com.giaquino.cm.model.response.ResponseCarPriceTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test

class CarResponseTest {

    private lateinit var jsonResponseSuccess: String
    private lateinit var jsonResponseFailure: String
    private lateinit var gson: Gson

    @Before
    fun setup() {
        jsonResponseSuccess = TestUtil.loadFileFromResource("response_car_success.txt").readText()
        jsonResponseFailure = TestUtil.loadFileFromResource("response_car_failure.txt").readText()
        gson = GsonBuilder().registerTypeAdapter(object : TypeToken<ResponseCarPrice>() {}.type,
                ResponseCarPriceTypeAdapter()).create()
    }

    @Test
    fun parsedSuccessfulResponse() {
        val response = gson.fromJson(jsonResponseSuccess, CarResponse::class.java)
        assert(response.success)
        assertThat(response.message.success, Matchers.notNullValue())
        assertThat(response.metadata, Matchers.notNullValue())
        System.out.println(response.metadata)
        val data = response.toCarWithImage()
        System.out.println(data)
        assertThat(data, Matchers.notNullValue())
        assertThat(data, Matchers.hasSize(10))
    }

    @Test
    fun parsedFailureResponse() {
        val response = gson.fromJson(jsonResponseFailure, CarResponse::class.java)
        assert(!response.success)
        assertThat(response.message.error, Matchers.notNullValue())
        assertThat(response.metadata, Matchers.nullValue())
    }
}