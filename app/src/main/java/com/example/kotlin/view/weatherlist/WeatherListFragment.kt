package com.example.kotlin.view.weatherlist

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin.R
import com.example.kotlin.databinding.FragmentWeatherListBinding
import com.example.kotlin.repository.City
import com.example.kotlin.repository.Weather
import com.example.kotlin.utils.KEY_BUNDLE_WEATHER
import com.example.kotlin.view.details.DetailsFragment
import com.example.kotlin.viewmodel.AppState
import com.example.kotlin.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class WeatherListFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentWeatherListBinding? = null //убрали утечку памяти
    private val binding: FragmentWeatherListBinding
        get() {
            return _binding!!
        }

    private val adapter = WeatherListAdapter(this)


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var isRussian = true
    private val viewModel: MainViewModel by lazy {  //вызывается по требованию, ленивое свойство, отложенная реализация
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
        val observer = { data: AppState -> renderData(data) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        setupFabCities()
        setupFabLocation()
        viewModel.getWeatherRussia()
    }

    private fun setupFabLocation() {
        binding.mainFragmentFABLocation.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        //есть ли разрешение? проверка делается каждый раз т.к. пользователь может отобрать разрешение
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED  //если выдано
        ) {
            getLocation()  //получаем
            //если пользователь первый раз отклонил
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            //важно написать убедительную просьбу
            explainAccessFineLocation()
        } else { //если разрешения нет или это первый запрос
            mRequestPermissionFineLocation()
        }
    }


    @SuppressLint("MissingPermission") //делаю проверку прямо перед вызовом функции
    private fun getLocation() {
        context?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val providerGPS =
                    locationManager.getProvider(LocationManager.GPS_PROVIDER)  //можно использовать BestProvider
                /*providerGPS?.let {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        10000L,  //срабатывает через заданное время
                        0f,
                        locationListenerTime
                    )
                }*/
                providerGPS?.let {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0L,
                        100f, //срабатывает при смене позиции в метрах
                        locationListenerDistance
                    )
                }
            }
        }
    }

    fun getAddressByLocation(location: Location) {
        val geocoder = Geocoder(requireContext())
        val timeStump = System.currentTimeMillis()
        Thread {
            val addressText =
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1000000
                )!![0].getAddressLine(0)
            requireActivity().runOnUiThread{ //перенес в главный поток
                showAddressDialog(addressText, location)
            }

        }.start()

        Log.d("@@@", "прошло ${System.currentTimeMillis() - timeStump}")
    }


    private val locationListenerTime = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("@@@", location.toString())
            getAddressByLocation(location)
        }

        override fun onProviderDisabled(provider: String) {
            Snackbar.make(binding.recyclerView, "gps disable", Snackbar.LENGTH_LONG).show()
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }
    }
    private val locationListenerDistance = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("@@@", location.toString())
            getAddressByLocation(location)
        }

        override fun onProviderDisabled(provider: String) {
            Snackbar.make(binding.recyclerView, "gps disable", Snackbar.LENGTH_LONG).show()
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }
    }

    private fun mRequestPermissionFineLocation() {
        locationResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private val locationResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            // Handle Permission granted/rejected
            if (isGranted) {
                getLocation()
            } else {
                explainAccessFineLocation()
            }
        }

    private fun explainAccessFineLocation() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_rationale_title))
            .setMessage(getString(R.string.dialog_rationale_message))
            .setPositiveButton(getString(R.string.dialog_rationale_give_access)) { _, _ ->
                mRequestPermissionFineLocation()
            }
            .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() } //в случае отказа можно перекинуть на страничку с текстом почему это важно
            .create()
            .show()
    }

    private fun setupFabCities() {
        binding.floatingActionButton.setOnClickListener {
            isRussian = !isRussian
            if (isRussian) {
                viewModel.getWeatherRussia()
                binding.floatingActionButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_russia
                    )
                )
            } else {
                viewModel.getWeatherWorld()
                binding.floatingActionButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_earth
                    )
                )
            }
        }
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.root, "Не получилось", Snackbar.LENGTH_LONG).show()
            }

            is AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherList)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherListFragment()
    }

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                putParcelable(KEY_BUNDLE_WEATHER, weather)
            }))
            .addToBackStack("")
            .commit()
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    onItemClick(
                        Weather(
                            City(
                                address,
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

}