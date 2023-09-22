package com.example.menupracticaltestapp

import android.app.Application
import com.example.menupracticaltestapp.helpers.preferencesModule
import com.example.menupracticaltestapp.network.HeaderInterceptor
import com.example.menupracticaltestapp.network.provideForecastApi
import com.example.menupracticaltestapp.network.provideOkHttpClient
import com.example.menupracticaltestapp.network.provideRetrofit
import com.example.menupracticaltestapp.repo.RemoteRepo
import com.example.menupracticaltestapp.screens.listOfVenues.ListOfVenuesViewModel
import com.example.menupracticaltestapp.screens.login.LoginViewModel
import com.example.menupracticaltestapp.screens.venueDetails.VenueDetailsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import java.util.UUID

class App: Application() {

    private val viewModelModule = module {
        factory { LoginViewModel(get(), get()) }
        factory { VenueDetailsViewModel(get()) }
        factory { ListOfVenuesViewModel(get()) }
    }

    private val repoModule = module {
        factory { RemoteRepo(get()) }
    }

    private val networkModule = module {
        factory { HeaderInterceptor(this@App) }
        factory { provideOkHttpClient(get()) }
        factory { provideForecastApi(get()) }
        single { provideRetrofit(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(viewModelModule, repoModule, networkModule, preferencesModule))
        }

    }
}