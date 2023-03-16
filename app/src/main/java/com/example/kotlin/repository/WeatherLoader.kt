package com.example.kotlin.repository


import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


import javax.net.ssl.HttpsURLConnection

class WeatherLoader(val onServerResponseListener: OnServerResponse) {
    fun loadWeather(lat: Double, lon: Double) {

        val urlText =
            "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon" // получили адрес
        val uri = URL(urlText) // создал uri
        val urlConnection: HttpsURLConnection =  // создание потока
            (uri.openConnection() as HttpsURLConnection).apply {
                connectTimeout = 1000 // время на подключение
                readTimeout = 1000 // ожидание ответа
                addRequestProperty("X-Yandex-API-Key", "c9563b06-f14b-49db-a5ef-7ee1910db6ad")
            }
        Thread {//открыли вспомогательный поток
            try {

                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val buffer =
                    BufferedReader(InputStreamReader(urlConnection.inputStream)) //открываем соединение и забуферизировали
                //val result = (buffer)
                val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                Handler(Looper.getMainLooper()).post {
                    onServerResponseListener.onResponse(weatherDTO)
                }
            } catch (e: Exception) {
                //TODO что-то пошло не так
            } finally {
                urlConnection.disconnect()
            }


        }.start()
    }


}