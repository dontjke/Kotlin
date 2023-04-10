package com.example.kotlin.repository

import com.example.kotlin.BuildConfig
import com.example.kotlin.repository.dto.WeatherDTO
import com.example.kotlin.utils.YANDEX_API_KEY
import com.example.kotlin.utils.YANDEX_DOMAIN
import com.example.kotlin.utils.YANDEX_ENDPOINT
import com.example.kotlin.utils.convertDtoToModel
import com.example.kotlin.viewmodel.DetailsViewModel
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request


class DetailsRepositoryOneOkhttpImpl : DetailsRepositoryOne {
    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {
        val client = OkHttpClient() //создал клиент
        val builder = Request.Builder() //создал запрос, настройка билдера
        builder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
        builder.url("$YANDEX_DOMAIN${YANDEX_ENDPOINT}lat=${city.lat}&lon=${city.lon}")
        val request = builder.build() //запустили билдер, записали ответ
        val call = client.newCall(request)
        Thread {
            val response = call.execute()
            if (response.isSuccessful) {
                val serverResponse = response.body!!.string()
                val weatherDTO: WeatherDTO = Gson().fromJson(serverResponse, WeatherDTO::class.java)
                val weather = convertDtoToModel(weatherDTO)
                weather.city = city
                callback.onResponse(weather)
            } else {
            }
        }.start()


    }
}