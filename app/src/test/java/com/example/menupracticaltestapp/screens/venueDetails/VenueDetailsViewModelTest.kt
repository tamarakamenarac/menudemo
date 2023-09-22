package com.example.menupracticaltestapp.screens.venueDetails

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.menupracticaltestapp.CoroutineTestRule
import com.example.menupracticaltestapp.repo.RemoteRepo
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doReturn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class VenueDetailsViewModelTest {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var viewStateCaptor = argumentCaptor<VenueDetailsState>()

    @Mock
    private lateinit var viewStateObserver: Observer<VenueDetailsState>

    private lateinit var viewModel: VenueDetailsViewModel

    private lateinit var remoteRepo: RemoteRepo
    private lateinit var sharedPrefEditor: SharedPreferences.Editor
    private lateinit var mockModule: Module

    @After
    fun tearDown() {
        viewModel.viewState.removeObserver(viewStateObserver)
        GlobalContext.unloadKoinModules(mockModule)
        stopKoin()
    }

    @Before
    fun initialize() {
        MockitoAnnotations.openMocks(this)

        remoteRepo = Mockito.mock(RemoteRepo::class.java)
        sharedPrefEditor = Mockito.mock(SharedPreferences.Editor::class.java)


        mockModule = module {
            single { remoteRepo }
            single { sharedPrefEditor }
        }

        startKoin {
            modules(mockModule)
        }

        loadKoinModules(mockModule)

        viewModel = VenueDetailsViewModel(
            sharedPreferencesEditor = sharedPrefEditor
        )

        viewModel.viewState.observeForever(viewStateObserver)
    }

    @Test
    fun `when user tries to logout the logout state is received`() = runTest {
        // When
        doReturn(sharedPrefEditor).`when`(
            sharedPrefEditor
        ).remove(Mockito.any())
        doReturn(true).`when`(
            sharedPrefEditor
        ).commit()

        viewModel.logout()
        advanceUntilIdle()

        // Then
        Mockito.verify(viewStateObserver, Mockito.times(1)).onChanged(viewStateCaptor.capture())

        Assert.assertEquals(
            VenueDetailsState.Logout, viewStateCaptor.allValues[0]
        )
    }
}