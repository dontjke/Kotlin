package com.example.kotlin.view.details


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.kotlin.databinding.FragmentDetailsBinding
import com.example.kotlin.repository.Weather
import com.example.kotlin.utils.KEY_BUNDLE_WEATHER
import com.example.kotlin.utils.loadSvg
import com.example.kotlin.viewmodel.DetailsState
import com.example.kotlin.viewmodel.DetailsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make

class DetailsFragment : Fragment()/*, OnServerResponse, OnServerResponseListener*/ {

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

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(
            viewLifecycleOwner,
            object : Observer<DetailsState> {   //получаю viewModel, подписался
                override fun onChanged(t: DetailsState) {
                    renderData(t)  //орисовываю в renderData при изменениях
                }
            })


        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            viewModel.getWeather(it.city)
        }
    }


    private fun renderData(detailsState: DetailsState) {
        when (detailsState) {
            is DetailsState.Error -> {
            }
            DetailsState.Loading -> {
            }
            is DetailsState.Success -> {
                val weather = detailsState.weather
                with(binding) {
                    loadingLayout.visibility = View.GONE
                    cityNameTextView.text = weather.city.name
                    temperatureValue.text = weather.temperature.toString()
                    feelsLikeValue.text = weather.feelsLike.toString()
                    cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
                    showSnackBar("Получилось", mainView)

                    headerCityIcon.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")//coil

                    icon.loadSvg("https://yastatic.net/weather/i/icons/blueye/color/svg/${weather.icon}.svg")
                }
            }
        }
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
}
