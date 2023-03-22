package com.example.kotlin.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.kotlin.BuildConfig
import com.example.kotlin.databinding.FragmentDetailsBinding
import com.example.kotlin.repository.*
import com.example.kotlin.repository.dto.WeatherDTO
import com.example.kotlin.utils.*
import com.example.kotlin.viewmodel.ResponseState
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class DetailsFragment : Fragment(), OnServerResponse, OnServerResponseListener {

    private var _binding: FragmentDetailsBinding? = null //убрали утечку памяти
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(receiver) //уничтожаем ресивер
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let { intent ->
                intent.getParcelableExtra<WeatherDTO>(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER)?.let {
                    onResponse(it)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }


    lateinit var currentCityName: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LocalBroadcastManager.getInstance(requireContext())  //локальный приемник
            .registerReceiver(receiver, IntentFilter(KEY_WAVE_SERVICE_BROADCAST))
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
            // Thread{
            //WeatherLoader(this@DetailsFragment,this@DetailsFragment).loadWeather(it.city.lat, it.city.lon)
            // }.start()

            /*requireActivity().startService(Intent(requireContext(), DetailsService::class.java).apply {
                putExtra(KEY_BUNDLE_LAT,it.city.lat)
                putExtra(KEY_BUNDLE_LON,it.city.lon)
            })*/
            getWeather(it.city.lat, it.city.lon)
        }
    }

    private fun getWeather(lat: Double, lon: Double) {
        binding.loadingLayout.visibility = View.VISIBLE

        val client = OkHttpClient() //создал клиент
        val builder = Request.Builder() //создал запрос, настройка билдера
        builder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
        builder.url("$YANDEX_DOMAIN${YANDEX_ENDPOINT}lat=$lat&lon=$lon")
        val request = builder.build() //запустили билдер, записали ответ
        val callback: Callback = object : Callback { //встроенный в библиотеку колбэк
            override fun onFailure(call: Call, e: IOException) {
                // binding.loadingLayout.visibility = View.GONE
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val weatherDTO: WeatherDTO =
                        Gson().fromJson(response.body()!!.string(), WeatherDTO::class.java)
                    requireActivity().runOnUiThread {
                        renderData(weatherDTO)
                    }
                } else {
                    //TODO
                }
            }
        }
        val call = client.newCall(request)
        //val response = call.execute()//синхронный вызов
        call.enqueue(callback) //асинхронный вызов

    }

    private fun renderData(weather: WeatherDTO) {
        with(binding) {
            loadingLayout.visibility = View.GONE
            cityNameTextView.text = currentCityName
            temperatureValue.text = weather.factDTO.temperature.toString()
            feelsLikeValue.text = weather.factDTO.feelsLike.toString()
            cityCoordinates.text = "${weather.infoDTO.lat} ${weather.infoDTO.lon}"
            showSnackBar("Получилось", mainView)
        }
        // Snackbar.make(binding.mainView, "Получилось", Snackbar.LENGTH_LONG).show()
    }

    private fun showSnackBar(string: String, view: View) =
        make(view, string, Snackbar.LENGTH_LONG).show()


    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onResponse(weatherDTO: WeatherDTO) {
        renderData(weatherDTO)
    }

    override fun onError(error: ResponseState) {
        //TODO выводим ошибку
    }
}
