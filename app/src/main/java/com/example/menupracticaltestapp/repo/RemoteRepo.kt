package com.example.menupracticaltestapp.repo

import com.example.menupracticaltestapp.data.ListOfVenuesRequest
import com.example.menupracticaltestapp.data.ListResponse
import com.example.menupracticaltestapp.data.LoginRequest
import com.example.menupracticaltestapp.data.LoginResponse
import com.example.menupracticaltestapp.data.Venue
import com.example.menupracticaltestapp.data.VenuesListResponse
import com.example.menupracticaltestapp.helpers.Failure
import com.example.menupracticaltestapp.helpers.Result
import com.example.menupracticaltestapp.helpers.Success
import com.example.menupracticaltestapp.network.Api
import org.koin.dsl.module

class RemoteRepo(private val api: Api) {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val data = api.login(
                LoginRequest(
                    email = email,
                    password = password
                )
            )
            Success(data)
        } catch (e: Exception) {
            //log error
            Failure(e.localizedMessage)
        }
    }

    suspend fun fetchListOfVenues(latitude: String, longitude: String): Result<ListResponse> {
        return try {
            val data = api.fetchListOfVenues(
                ListOfVenuesRequest(
                    latitude = latitude,
                    longitude = longitude
                )
            )
            Success(data)
        } catch (e: Exception) {
            //log error
            Failure(e.localizedMessage)
        }
    }
}