package com.example.kotlin.utils

import android.widget.ImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.kotlin.domian.room.HistoryEntity
import com.example.kotlin.repository.City
import com.example.kotlin.repository.Weather
import com.example.kotlin.repository.dto.FactDTO
import com.example.kotlin.repository.dto.WeatherDTO
import com.example.kotlin.repository.getDefaultCity

const val KEY_BUNDLE_WEATHER = "WEATHER"
const val YANDEX_DOMAIN = "https://api.weather.yandex.ru/"
const val YANDEX_ENDPOINT = "v2/informers?"
const val YANDEX_API_KEY = "X-Yandex-API-Key"
const val KEY_BUNDLE_LAT = "lat"
const val KEY_BUNDLE_LON = "lon"
const val KEY_BUNDLE_SERVICE_BROADCAST_WEATHER = "WEATHER_S_B"
const val KEY_WAVE_SERVICE_BROADCAST = "my_action_way"
const val KEY_BUNDLE_SERVICE_MESSAGE = "key2"
const val KEY_BUNDLE_ACTIVITY_MESSAGE = "key1"
const val KEY_VIBE = "myAction"
const val KEY_SP_FILE_NAME_1 = "fileName1"
const val KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN = "is_russian"
const val REQUEST_CODE = 999


fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: FactDTO = weatherDTO.factDTO
    return Weather(getDefaultCity(), fact.temperature, fact.feelsLike, weatherDTO.factDTO.icon)
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(City(it.city, 0.0, 0.0), it.temperature, it.feelsLike, it.icon)
    }
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0, weather.city.name, weather.temperature, weather.feelsLike, weather.icon)
}

fun ImageView.loadSvg(url: String) {
    val imageLoader = ImageLoader.Builder(this.context)
        .components { add(SvgDecoder.Factory()) }
        .build()
    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .data(url)
        .target(this)
        .build()
    imageLoader.enqueue(request)
}