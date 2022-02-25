package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit
    .Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiServices{

    @GET("planetary/apod")
    suspend fun getPictureOfDay(@Query("api_key") api_key: String = API_KEY): PictureOfDay

    @GET("neo/rest/v1/feed")
    suspend fun getAstreoids(@Query("api_key") api_key: String = API_KEY): String

}
object AsteroidApi {
    val RETROFIT_SERVICE : ApiServices by lazy { retrofit.create(ApiServices::class.java) }
}
