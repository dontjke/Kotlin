package com.example.kotlin.repository

import android.util.Log
import com.example.kotlin.repository.citydto.GeoObjectCollection

import com.google.gson.GsonBuilder
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class CitiesRepositoryRetrofit2Impl : CitiesRepository {
    override fun getCityList() {
        val cityAPI = Retrofit.Builder().apply { //создаём инстанс Retrofit
            baseUrl("https://geocode-maps.yandex.ru/")
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(CityAPI::class.java)

        cityAPI.getCity().enqueue(object : Callback<GeoObjectCollection> { //асинхронно
            override fun onResponse(
                call: Call<GeoObjectCollection>, response: Response<GeoObjectCollection>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val cities =
                            it.response.geoObjectCollection.featureMember
                        val pos = cities[0].geoObject.point.pos.split(" ")
                        val lat = pos[0]
                        val lon = pos[1]
                        cities.forEach { member ->
                                Log.d("@@@", member.toString())
                            }
                    }
                }
            }

            override fun onFailure(call: Call<GeoObjectCollection>, t: Throwable) {

            }


        })
    }


}