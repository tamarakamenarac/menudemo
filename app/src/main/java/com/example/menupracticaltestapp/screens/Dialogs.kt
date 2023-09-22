package com.example.menupracticaltestapp.screens

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.menupracticaltestapp.R

object Dialogs {

    fun setUpOfflineDialog(context: Context): AlertDialog {
        val builder = AlertDialog.Builder(context).apply {
            setTitle(context.resources.getString(R.string.network_connection_lost))
            setMessage(context.resources.getString(R.string.check_network_connection))
            setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false)
        }
        return builder.create()
    }

    fun setupLoginErrorDialog(context: Context): AlertDialog {
        val builder = AlertDialog.Builder(context).apply {
            setTitle(context.resources.getString(R.string.sign_up_error))
            setMessage(context.resources.getString(R.string.an_error_occurred))

            setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
        }
        return builder.create()
    }

    fun showErrorDialog(context: Context, onClick: () -> Unit): AlertDialog {
        val builder = AlertDialog.Builder(context).apply {
            setTitle(context.resources.getString(R.string.error))
            setMessage(context.resources.getString(R.string.an_error_occurred))

            setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                onClick()
            }
        }
        return builder.create()
    }

    fun showRequestPermissionDialog(context: Context, onClick: () -> Unit): AlertDialog {
        val builder = AlertDialog.Builder(context).apply {
            setTitle(context.resources.getString(R.string.location_access_required))
            setMessage(context.resources.getString(R.string.app_requires_your_location))

            setPositiveButton(R.string.request_permission) { dialog, _ ->
                dialog.dismiss()
                onClick()
            }
        }
        return builder.create()
    }

    fun showEnableLocationServicesDialog(context: Context, onClick: () -> Unit): AlertDialog {
        val builder = AlertDialog.Builder(context).apply {
            setTitle(context.resources.getString(R.string.enable_location_service))
            setMessage(context.resources.getString(R.string.enable_location_desc))

            setPositiveButton(R.string.enable) { dialog, _ ->
                dialog.dismiss()
                onClick()
            }
        }
        return builder.create()
    }

    fun showLocationPermissionDeniedDialog(context: Context, onClick: () -> Unit): AlertDialog {
        val builder = AlertDialog.Builder(context).apply {
            setTitle(context.resources.getString(R.string.location_access_required))
            setMessage(context.resources.getString(R.string.app_requires_your_location))

            setPositiveButton(R.string.open_phone_settings) { dialog, _ ->
                dialog.dismiss()
                onClick()
            }
        }
        return builder.create()
    }
}