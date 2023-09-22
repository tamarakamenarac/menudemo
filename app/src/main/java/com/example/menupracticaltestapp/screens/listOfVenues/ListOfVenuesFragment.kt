package com.example.menupracticaltestapp.screens.listOfVenues

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.menupracticaltestapp.R
import com.example.menupracticaltestapp.data.Venue
import com.example.menupracticaltestapp.data.VenuesListResponse
import com.example.menupracticaltestapp.databinding.FragmentListOfVenuesBinding
import com.example.menupracticaltestapp.helpers.ViewBindingHolder
import com.example.menupracticaltestapp.helpers.ViewBindingHolderImpl
import com.example.menupracticaltestapp.helpers.getLastLocation
import com.example.menupracticaltestapp.helpers.goToLocationSettings
import com.example.menupracticaltestapp.helpers.goToSettingsAppDetails
import com.example.menupracticaltestapp.helpers.hasLocationPermission
import com.example.menupracticaltestapp.helpers.isLocationServiceEnabled
import com.example.menupracticaltestapp.screens.Dialogs.showEnableLocationServicesDialog
import com.example.menupracticaltestapp.screens.Dialogs.showErrorDialog
import com.example.menupracticaltestapp.screens.Dialogs.showLocationPermissionDeniedDialog
import com.example.menupracticaltestapp.screens.Dialogs.showRequestPermissionDialog
import com.example.menupracticaltestapp.screens.venueDetails.VenueDetailsFragmentArgs
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListOfVenuesFragment: Fragment(), VenueClickedListener, ViewBindingHolder<FragmentListOfVenuesBinding> by ViewBindingHolderImpl() {

    private val listOfVenuesViewModel by viewModel<ListOfVenuesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = initBinding(
        this,
        FragmentListOfVenuesBinding.inflate(inflater, container, false)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListOfVenuesAdapter(this)
        binding.listOfVenues.layoutManager = LinearLayoutManager(requireContext())
        binding.listOfVenues.adapter = adapter

        listOfVenuesViewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is ListOfVenuesViewState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ListOfVenuesViewState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.listOfVenues.visibility = View.VISIBLE
                    adapter.updateList(viewState.listOfVenues)
                }
                is ListOfVenuesViewState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showErrorDialog(requireContext()){ binding.emptyListText.visibility = View.VISIBLE }.show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (requireContext().hasLocationPermission) {
            onPermissionGrantedAction()
        }
        else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                showRequestPermissionDialog(requireContext()) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ),
                        LOCATION_REQUEST_CODE
                    )
                }.show()
            } else {
                requestPermissions(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            }
        }
    }

    override fun venueClicked(item: VenuesListResponse) {
        findNavController().navigate(R.id.action_details, VenueDetailsFragmentArgs.Builder(item.venue!!).build().toBundle())
    }

    private fun onPermissionGrantedAction() {
        if (requireContext().isLocationServiceEnabled()) {
            getLastKnownLocation()
        } else {
            displayDisabledLocationModal()
        }
    }

    private fun displayDisabledLocationModal() {
        showEnableLocationServicesDialog(requireContext()) {
            requireActivity().goToLocationSettings()
        }.show()
    }

    private fun getLastKnownLocation() {
        val locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        requireContext().getLastLocation(locationClient, { location -> updateLocationAndFetchVenues(location) }) { exception ->
            //Log the exception
        }
    }

    private fun updateLocationAndFetchVenues(location: Location) {
        listOfVenuesViewModel.updateLocation(location.latitude, location.longitude)
        listOfVenuesViewModel.fetchListOfVenues()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted fetch last known location
                    if (requireContext().isLocationServiceEnabled()) {
                        getLastKnownLocation()
                    } else {
                        displayDisabledLocationModal()
                    }
                } else {
                    displayDeniedLocationPermissionScreen()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun displayDeniedLocationPermissionScreen() {
        showLocationPermissionDeniedDialog(requireContext()) {
            requireActivity().goToSettingsAppDetails()
        }.show()
    }

    companion object {
        const val LOCATION_REQUEST_CODE = 10
    }
}