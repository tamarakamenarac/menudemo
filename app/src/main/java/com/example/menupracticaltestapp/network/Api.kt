package com.example.menupracticaltestapp.network

import com.example.menupracticaltestapp.data.ListOfVenuesRequest
import com.example.menupracticaltestapp.data.ListResponse
import com.example.menupracticaltestapp.data.LoginRequest
import com.example.menupracticaltestapp.data.LoginResponse
import com.example.menupracticaltestapp.data.Venue
import com.example.menupracticaltestapp.data.VenuesListResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("customers/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @POST("directory/search?view=search")
    suspend fun fetchListOfVenues(@Body body: ListOfVenuesRequest): ListResponse
}