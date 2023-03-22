package com.example.kotlin.view.details

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.kotlin.repository.dto.WeatherDTO
import com.example.kotlin.utils.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailsService(val name: String = "") : IntentService(name) {
    override fun onHandleIntent(p0: Intent?) {
        Log.d("@@@", "work DetailsService")
        p0?.let {
            val lat = it.getDoubleExtra(KEY_BUNDLE_LAT, 0.0) //получаю от фрагмента в сервис
            val lon = it.getDoubleExtra(KEY_BUNDLE_LON, 0.0) //получаю от фрагмента в сервис
            Log.d("@@@", "work DetailsService$lat $lon")


            val urlText = "$YANDEX_DOMAIN${YANDEX_PATH}lat=$lat&lon=$lon" // получили адрес
            val uri = URL(urlText) // создал uri
            val urlConnection: HttpsURLConnection =  // создание потока
                (uri.openConnection() as HttpsURLConnection).apply {
                    connectTimeout = 1000 // время на подключение
                    readTimeout = 1000 // ожидание ответа
                    addRequestProperty(
                        YANDEX_API_KEY,
                        "c9563b06-f14b-49db-a5ef-7ee1910db6ad"
                    ) //BuildConfig.WEATHER_API_KEY
                }


            try {

                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.responseMessage


                val serverside = 500..599
                val clientside = 400..499
                val responseOk = 200..299

                when (responseCode) {
                    in serverside -> {
                    }
                    in clientside -> {
                    }
                    in responseOk -> {
                        val buffer =
                            BufferedReader(InputStreamReader(urlConnection.inputStream)) //открываем соединение и забуферизировали
                        //val result = (buffer)
                        val weatherDTO: WeatherDTO =
                            Gson().fromJson(buffer, WeatherDTO::class.java)


                        val message =
                            Intent(KEY_WAVE_SERVICE_BROADCAST)  //создаю ответ от сервиса в активити
                        message.putExtra(
                            KEY_BUNDLE_SERVICE_BROADCAST_WEATHER, weatherDTO
                        )  //сообщение в фрагмент
                        LocalBroadcastManager.getInstance(this).sendBroadcast(message) //отправляю
                    }

                    else -> {}
                }
            } catch (e: JsonSyntaxException) {
                //TODO что-то пошло не так
            } finally {
                urlConnection.disconnect()
            }
        }
    }
}