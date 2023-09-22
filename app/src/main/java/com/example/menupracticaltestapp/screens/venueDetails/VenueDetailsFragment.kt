package com.example.menupracticaltestapp.screens.venueDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.menupracticaltestapp.R
import com.example.menupracticaltestapp.data.Venue
import com.example.menupracticaltestapp.databinding.FragmentVenueDetailsBinding
import com.example.menupracticaltestapp.helpers.ViewBindingHolder
import com.example.menupracticaltestapp.helpers.ViewBindingHolderImpl
import org.koin.androidx.viewmodel.ext.android.viewModel

class VenueDetailsFragment: Fragment(), ViewBindingHolder<FragmentVenueDetailsBinding> by ViewBindingHolderImpl() {

    private val venueDetailsViewModel by viewModel<VenueDetailsViewModel>()

    private val venue: Venue by lazy {
        navArgs<VenueDetailsFragmentArgs>().value.venue
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = initBinding(
        this,
        FragmentVenueDetailsBinding.inflate(inflater, container, false)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.venueName.text = venue.name
        venue.translations?.welcomeMessage?.let {
            binding.venueWelcome.visibility = View.VISIBLE
            binding.venueWelcome.text = it
        }
        binding.venueDescription.text = venue.translations?.description

        binding.logoutButton.setOnClickListener {
            venueDetailsViewModel.logout()
        }

        if (!venue.isOpen) {
            binding.venueAvailability.visibility = View.VISIBLE
        }

        venueDetailsViewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is VenueDetailsState.Logout -> {
                    findNavController().navigate(R.id.action_logout)
                }
            }
        }
    }
}