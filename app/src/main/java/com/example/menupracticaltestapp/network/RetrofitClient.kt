package com.example.menupracticaltestapp.network

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID

    private val BASE_URL = "https://api-qa.menu.app/api/"

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    fun provideOkHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor(headerInterceptor).build()
    }

    fun provideForecastApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)