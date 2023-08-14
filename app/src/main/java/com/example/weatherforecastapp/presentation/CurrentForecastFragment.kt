package com.example.weatherforecastapp.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecastapp.BuildConfig
import com.example.weatherforecastapp.databinding.FragmemtCurrentTimeForecastBinding
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.util.*

class CurrentForecastFragment : Fragment() {
    private var _binding: FragmemtCurrentTimeForecastBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ForecastViewModel by sharedViewModel()
    private var loadingDialog: LoadingDialog? = null
    private var tempCelsius: Double = 0.0
    private var tempFahrenheit: Double? = null
    private var isCelsius = true

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var isLocationUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmemtCurrentTimeForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.switchTextView.text = requireContext().getString(R.string.switch_unit)
        binding.switchTextView.setOnClickListener {
            if (isCelsius) {
                isCelsius = !isCelsius
                if (tempFahrenheit == null) {
                    tempFahrenheit = (tempCelsius * 1.8) + 32.0
                }
                binding.tempTextView.text =
                    requireContext().getString(R.string.temp_fahrenheit, tempFahrenheit)
            } else {
                isCelsius = !isCelsius
                binding.tempTextView.text =
                    requireContext().getString(R.string.temp_celsius, tempCelsius)
            }
        }
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getCurrentForecastFromLocation()
        }
        lifecycleScope.launch {
            loadCityListAssets().flowOn(Dispatchers.IO).collect {
                viewModel.setupCityList(it)
            }
        }
        checkPermission()
    }

    private fun observer() {
        viewModel.onShowCurrentForecast().observe(viewLifecycleOwner) { currentDayModel ->
            binding.refreshLayout.isRefreshing = false
            hideLoadingDialog()

            currentDayModel?.let {
                with(binding) {
                    tempCelsius = it.main?.temp ?: 0.0
                    tempFahrenheit = null
                    cityTextView.text = it.name
                    weatherMainTextView.text = it.weather?.firstOrNull()?.main
                    weatherDescriptionTextView.text = it.weather?.firstOrNull()?.description
                    tempTextView.text = getString(R.string.temp_celsius, tempCelsius)
                    humidityTextView.text =
                        getString(R.string.humidity_percentage, it.main?.humidity)
                    iconImageView.loadImageUrl("${BuildConfig.IMAGE_URL}/${it.weather?.firstOrNull()?.icon}")
                    switchTextView.visibility = View.VISIBLE
                    humidityImageView.visibility = View.VISIBLE
                }
            }
        }

        viewModel.onShowKeywordSuggest().observe(viewLifecycleOwner) { suggest ->
            with(binding.searchView) {
                doOnAfterTextChange { text ->
                    val query = suggest.filter { it.contains(text) }
                    if (binding.searchView.lastSuggestions.isNotEmpty()) {
                        binding.searchView.lastSuggestions = query
                    } else {
                        binding.searchView.updateLastSuggestions(query)
                    }
                }

                doOnConfirm {
                    view?.hideKeyboard()
                    viewModel.getCurrentForecastFormCityName(it)
                }
            }
        }

        viewModel.onShowCitySearch().observe(viewLifecycleOwner) { currentDayModel ->
            with(currentDayModel) {
                tempCelsius = main?.temp ?: 0.0
                tempFahrenheit = null
                binding.cityTextView.text = name
                binding.weatherMainTextView.text = weather?.first()?.main
                binding.weatherDescriptionTextView.text = weather?.first()?.description
                binding.tempTextView.text = getString(R.string.temp_celsius, tempCelsius)
                binding.humidityTextView.text =
                    getString(R.string.humidity_percentage, main?.humidity)
                binding.iconImageView.loadImageUrl(BuildConfig.IMAGE_URL + "/${weather?.first()?.icon}")
            }
        }

        viewModel.onShowCurrentForecastError().observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = false
            hideLoadingDialog()
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_error))
                .setPositiveButton(getString(R.string.ok_button)) { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
    }

    private suspend fun loadCityListAssets() = flow {
        val json: String
        try {
            val input = requireActivity().assets.open("city_list.json")
            val size = input.available()
            val buffer = ByteArray(size)
            input.read(buffer)
            input.close()
            json = String(buffer, Charset.forName("UTF-8"))
            emit(json)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun checkPermission() {
        Dexter.withActivity(requireActivity())
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    setupLocationRequest()
                    observeLocationUpdate()
                    startLocationUpdate()
                    showLoadingDialog()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                }

            }).check()
    }

    @SuppressLint("MissingPermission")
    private fun setupLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        val client = LocationServices.getSettingsClient(requireActivity())
        client.checkLocationSettings(builder)
    }

    private fun observeLocationUpdate() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                if (!isLocationUpdate) {
                    isLocationUpdate = true
                    getCurrentForecastFromLocation(locationResult.lastLocation)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentForecastFromLocation(lastLocation: Location? = null) {
        lastLocation?.let {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val address = geocoder.getFromLocation(it.latitude, it.longitude, 1)
            viewModel.getCurrentForecastFromLocation(address!!.first())
        }

    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun showLoadingDialog() {
        loadingDialog = LoadingDialog()
        loadingDialog?.isCancelable = false
        loadingDialog?.show(childFragmentManager, "Loading dialog")
    }

    private fun hideLoadingDialog() {
        loadingDialog?.dismiss()
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}