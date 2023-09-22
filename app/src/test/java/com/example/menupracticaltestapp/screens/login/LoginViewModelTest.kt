package com.example.menupracticaltestapp.screens.login

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.menupracticaltestapp.CoroutineTestRule
import com.example.menupracticaltestapp.data.LoginResponse
import com.example.menupracticaltestapp.helpers.Failure
import com.example.menupracticaltestapp.helpers.Success
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
import org.koin.core.context.GlobalContext.unloadKoinModules
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.any
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    private var viewStateCaptor = argumentCaptor<LoginViewState>()

    @Mock
    private lateinit var viewStateObserver: Observer<LoginViewState>

    private lateinit var viewModel: LoginViewModel

    private lateinit var remoteRepo: RemoteRepo
    private lateinit var sharedPrefEditor: SharedPreferences.Editor
    private lateinit var mockModule: Module

    @After
    fun tearDown() {
        viewModel.viewState.removeObserver(viewStateObserver)
        unloadKoinModules(mockModule)
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

        viewModel = LoginViewModel(
            repo = remoteRepo,
            sharedPreferencesEditor = sharedPrefEditor
        )

        viewModel.viewState.observeForever(viewStateObserver)
    }

    @Test
    fun `when user tries to login the loading state is the first to be received`() = runTest {
        // When
        val testEmail = "test@testmenu.app"
        val testPassword = "test1234"
        val testResponse = LoginResponse("token")

        doReturn(sharedPrefEditor).`when`(
            sharedPrefEditor
        ).putString(any(), any())
        doReturn(true).`when`(
            sharedPrefEditor
        ).commit()
        doReturn(Success(testResponse)).`when`(
            remoteRepo
        ).login(testEmail, testPassword)

        viewModel.login(testEmail, testPassword)
        advanceUntilIdle()

        // Then
        verify(viewStateObserver, times(2)).onChanged(viewStateCaptor.capture())

        Assert.assertEquals(
            LoginViewState.Loading, viewStateCaptor.allValues[0]
        )
    }

    @Test
    fun `when user successfully logs in the success state is received`() = runTest {
        // When
        val testEmail = "test@testmenu.app"
        val testPassword = "test1234"
        val testResponse = LoginResponse("token")

        doReturn(sharedPrefEditor).`when`(
            sharedPrefEditor
        ).putString(any(), any())
        doReturn(true).`when`(
            sharedPrefEditor
        ).commit()
        doReturn(Success(testResponse)).`when`(
            remoteRepo
        ).login(testEmail, testPassword)

        viewModel.login(testEmail, testPassword)
        advanceUntilIdle()

        // Then
        verify(viewStateObserver, times(2)).onChanged(viewStateCaptor.capture())

        Assert.assertEquals(
            LoginViewState.Success(testResponse), viewStateCaptor.allValues[1]
        )
    }

    @Test
    fun `when user login is unsuccessful the failure state is received`() = runTest {
        // When
        val testEmail = "test@testmenu.app"
        val testPassword = "test1234"

        doReturn(sharedPrefEditor).`when`(
            sharedPrefEditor
        ).putString(any(), any())
        doReturn(true).`when`(
            sharedPrefEditor
        ).commit()
        doReturn(Failure("error")).`when`(
            remoteRepo
        ).login(testEmail, testPassword)

        viewModel.login(testEmail, testPassword)
        advanceUntilIdle()

        // Then
        verify(viewStateObserver, times(2)).onChanged(viewStateCaptor.capture())

        Assert.assertEquals(
            LoginViewState.Error("error"), viewStateCaptor.allValues[1]
        )
    }
}