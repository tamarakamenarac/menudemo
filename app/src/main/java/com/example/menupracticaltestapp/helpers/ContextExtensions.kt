package com.example.menupracticaltestapp.helpers

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

val Context.hasLocationPermission: Boolean
    get() = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

fun Context.isLocationServiceEnabled(): Boolean {
    var locationMode = Settings.Secure.LOCATION_MODE_OFF
    try {
        locationMode = Settings.Secure.getInt(contentResolver, "location_mode")
    } catch (settingException: Settings.SettingNotFoundException) {}
    return locationMode != Settings.Secure.LOCATION_MODE_OFF
}

/**
 * Fetches the last known location
 * Should be called only after location permission has been granted
 */
@SuppressLint("MissingPermission")
fun Context.getLastLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onSuccessListener: OnSuccessListener<Location>,
    onFailureListener: OnFailureListener
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        if (location != null) {
            onSuccessListener.onSuccess(location)
        } else {
            fusedLocationClient.requestLocationUpdates(LocationRequest().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
                interval = 0
                fastestInterval = 0
                numUpdates = 1
            }, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let {
                        onSuccessListener.onSuccess(it)
                    } ?: run {
                        onFailureListener.onFailure(Exception("Location update failure"))
                    }
                    fusedLocationClient.removeLocationUpdates(this)
                }

                override fun onLocationAvailability(p0: LocationAvailability) {}
            }, Looper.myLooper())
        }
    }
        .addOnFailureListener { exception ->
            onFailureListener.onFailure(exception)
        }
}

fun Activity.goToLocationSettings() {
    val intent = Intent()
    intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    startActivity(intent)
}

fun Activity.goToSettingsAppDetails() {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.data = Uri.parse("package:" + baseContext?.packageName)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    startActivity(intent)
}