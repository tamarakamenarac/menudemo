package com.example.menupracticaltestapp.network

import android.content.Context
import android.provider.Settings
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequest = chain.request()

        val androidId: String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        val request = originalRequest.newBuilder()
            .header("application", "mobile-application")
            .header("Content-Type", "application/json")
            .header("Device-UUID", androidId)
            .header("Api-Version", "3.7.0")
            .method(originalRequest.method(), originalRequest.body())
            .build()

        return chain.proceed(request)
    }
}