package com.example.menupracticaltestapp.helpers

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val preferencesModule = module {
    single { provideSettingsPreferences(androidApplication()) }
    single<SharedPreferences.Editor> {
        provideSettingsPreferences(androidApplication()).edit()
    }
}

private const val PREFERENCES_FILE_KEY = "shared_preferences"
const val PREFERENCES_ACCESS_TOKEN = "access_token"

private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)