package com.example.kotlin.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlin.databinding.FragmentDetailsBinding
import com.example.kotlin.repository.Weather
import com.example.kotlin.utils.KEY_BUNDLE_WEATHER
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make

class DetailsFragment : Fragment() {

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            renderData(it)
        }

    }

    private fun renderData(weather: Weather) {
        with(binding) {
            loadingLayout.visibility = View.GONE
            cityNameTextView.text = weather.city.name
            temperatureValue.text = weather.temperature.toString()
            feelsLikeValue.text = weather.feelsLike.toString()
            cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
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
}
