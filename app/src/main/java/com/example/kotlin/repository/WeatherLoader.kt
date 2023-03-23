package com.example.kotlin.repository


import android.os.Handler
import android.os.Looper
import com.example.kotlin.BuildConfig
import com.example.kotlin.repository.dto.WeatherDTO
import com.example.kotlin.utils.YANDEX_API_KEY
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onServerResponseListener: OnServerResponse, private val onErrorListener: OnServerResponseListener) {
    fun loadWeather(lat: Double, lon: Double) {

        val urlText =
            "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon" // получили адрес
        val uri = URL(urlText) // создал uri
        val urlConnection: HttpsURLConnection =  // создание потока
            (uri.openConnection() as HttpsURLConnection).apply {
                connectTimeout = 1000 // время на подключение
                readTimeout = 1000 // ожидание ответа
                addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY) //BuildConfig.WEATHER_API_KEY
            }
        Thread {//открыли вспомогательный поток
            try {

                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.responseMessage

                //onErrorListener.onError(AppError.Error1)

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

            // поток закрыт
        }.start()
    }


}