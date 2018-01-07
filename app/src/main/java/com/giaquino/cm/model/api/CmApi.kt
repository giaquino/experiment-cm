package com.giaquino.cm.model.api

import com.giaquino.cm.model.response.CarResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface CmApi {

    @GET("cars/page:{page}/maxitems:{max_items}/sort:{sort}")
    fun getCars(
            @Path("page") page: Int,
            @Path("max_items") maxItems: Int,
            @Path("sort") sort: String): Single<CarResponse>
}