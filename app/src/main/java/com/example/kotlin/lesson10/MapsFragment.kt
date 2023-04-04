package com.example.kotlin.lesson10

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kotlin.R
import com.example.kotlin.databinding.FragmentMapsMainBinding
import com.example.kotlin.repository.City
import com.example.kotlin.repository.Weather
import com.example.kotlin.utils.KEY_BUNDLE_WEATHER
import com.example.kotlin.view.details.DetailsFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_maps_main.*
import java.util.*

class MapsFragment : Fragment() {
    private var mapFragment: SupportMapFragment? = null
    private lateinit var map: GoogleMap
    private val markers: ArrayList<Marker> =
        arrayListOf()  //список маркеров (встроенный в карты класс)


    private var _binding: FragmentMapsMainBinding? = null //убрали утечку памяти
    private val binding: FragmentMapsMainBinding
        get() {
            return _binding!!
        }

    private fun addMarkerToArray(location: LatLng) {    //добавляем маркер в список
        val marker = setMarker(
            location, markers.size.toString(),
            R.drawable.ic_map_pin
        )
        markers.add(marker)
    }

    private fun drawLine() {
        var previousBefore: Marker? = null
        markers.forEach { current ->
            previousBefore?.let { previous ->
                map.addPolyline(
                    PolylineOptions().add(previous.position, current.position)
                        .color(Color.RED)
                        .width(5f)
                )
            }
            previousBefore = current
        }
    }

    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int
    ): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(location)  //где располагается
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourceId))
        )!!
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        map = googleMap
        val moscow = LatLng(55.0, 37.0)
        map.addMarker(MarkerOptions().position(moscow).title("Marker in Moscow"))
        map.moveCamera(CameraUpdateFactory.newLatLng(moscow))

        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isZoomControlsEnabled = true

        try {
            map.isMyLocationEnabled = true
        } catch (ignored: SecurityException) {
        }

        map.setOnMapLongClickListener {
            addMarkerToArray(it)
            drawLine()
        }
        map.setOnMapClickListener {
            getAddressByLocation(it)
        }

    }

    private fun getAddressByLocation(location: LatLng) {
        val geocoder = Geocoder(requireContext())  // , Locale.getDefault() дефолтный язык на устройстве

        Thread {
            val addressText =
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1000000
                )!![0].getAddressLine(0) //можно что-то другое округ страна город
            requireActivity().runOnUiThread{ //перенес в главный поток
                showAddressDialog(addressText, location)
            }
        }.start()
    }

    private fun showAddressDialog(address: String, location: LatLng) {
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
     private fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                putParcelable(KEY_BUNDLE_WEATHER, weather)
            }))
            .addToBackStack("")
            .commit()
    }


    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            Handler().postDelayed({
                mapFragment?.getMapAsync(callback)
            }, 500)

        } else {
            mRequestPermissionFineLocation()
            /*  ActivityCompat.requestPermissions(
                  requireActivity(),
                  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                  REQUEST_LOCATION_PERMISSION
              )*/
        }
    }

    private fun mRequestPermissionFineLocation() {
        locationResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private val locationResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            mapFragment?.getMapAsync(callback)

        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableMyLocation()
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?


        initView()
    }

    private fun initView() {
        binding.buttonSearch.setOnClickListener {
            val searchText = binding.searchAddress.text.toString()
            if (searchText.isEmpty() && searchText.isBlank()) {
                Snackbar.make(binding.root, R.string.address_hint, Snackbar.LENGTH_LONG).show()
            } else {

                try {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())

                    val results = geocoder.getFromLocationName(searchText, 1)

                    if (results == null) {
                        Log.d("@@@", "results null")
                    } else {

                        val location = LatLng(
                            results[0].latitude,
                            results[0].longitude
                        )

                        map.addMarker(
                            MarkerOptions()
                                .position(location)
                                .title(searchText)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
                        )
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))

                    }

                } catch (e: IndexOutOfBoundsException) {
                    Snackbar.make(binding.root, "адресс не найден", Snackbar.LENGTH_LONG).show()
                }

            }
        }
    }
}




