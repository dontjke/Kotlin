package com.example.kotlin.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlin.databinding.FragmentDetailsBinding
import com.example.kotlin.repository.OnServerResponse
import com.example.kotlin.repository.Weather
import com.example.kotlin.repository.WeatherDTO
import com.example.kotlin.repository.WeatherLoader
import com.example.kotlin.utils.KEY_BUNDLE_WEATHER
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make

class DetailsFragment : Fragment(), OnServerResponse {

    private var _binding: FragmentDetailsBinding? = null //убрали утечку памяти
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
            // Thread{
            WeatherLoader(this@DetailsFragment).loadWeather(it.city.lat, it.city.lon)
            // }.start()
        }
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
}
