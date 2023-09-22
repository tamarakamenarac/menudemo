package com.example.menupracticaltestapp.screens.listOfVenues

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.menupracticaltestapp.CoroutineTestRule
import com.example.menupracticaltestapp.data.ListResponse
import com.example.menupracticaltestapp.data.VenuesData
import com.example.menupracticaltestapp.data.VenuesListResponse
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
class ListOfVenuesViewModelTest {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var viewStateCaptor = argumentCaptor<ListOfVenuesViewState>()

    @Mock
    private lateinit var viewStateObserver: Observer<ListOfVenuesViewState>

    private lateinit var viewModel: ListOfVenuesViewModel

    private lateinit var remoteRepo: RemoteRepo
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


        mockModule = module {
            single { remoteRepo }
        }

        startKoin {
            modules(mockModule)
        }

        loadKoinModules(mockModule)

        viewModel = ListOfVenuesViewModel(
            repo = remoteRepo
        )

        viewModel.viewState.observeForever(viewStateObserver)
    }

    @Test
    fun `when user tries to fetch venues the loading state is the first to be received`() = runTest {
        // When
        val testLatitude = "44.001783"
        val testLongitude = "21.26907"

        doReturn(Failure("error")).`when`(
            remoteRepo
        ).fetchListOfVenues(testLatitude, testLongitude)

        viewModel.updateLocation(testLatitude.toDouble(), testLongitude.toDouble())

        viewModel.fetchListOfVenues()
        advanceUntilIdle()

        // Then
        Mockito.verify(viewStateObserver, Mockito.times(2)).onChanged(viewStateCaptor.capture())

        Assert.assertEquals(
            ListOfVenuesViewState.Loading, viewStateCaptor.allValues[0]
        )
    }

    @Test
    fun `when user successfully fetches venues in the success state is received`() = runTest {
        // When
        val testLatitude = "44.001783"
        val testLongitude = "21.26907"
        val listOfVenues = arrayListOf<VenuesListResponse>()
        val venueListResponse = ListResponse(null, null, VenuesData(listOfVenues))

        doReturn(Success(venueListResponse)).`when`(
            remoteRepo
        ).fetchListOfVenues(testLatitude, testLongitude)

        viewModel.updateLocation(testLatitude.toDouble(), testLongitude.toDouble())

        viewModel.fetchListOfVenues()
        advanceUntilIdle()

        // Then
        Mockito.verify(viewStateObserver, Mockito.times(2)).onChanged(viewStateCaptor.capture())

        Assert.assertEquals(
            ListOfVenuesViewState.Success(listOfVenues), viewStateCaptor.allValues[1]
        )
    }

    @Test
    fun `when fetching venues is unsuccessful the error state is received`() = runTest {
        // When
        val testLatitude = "44.001783"
        val testLongitude = "21.26907"

        doReturn(Failure("error")).`when`(
            remoteRepo
        ).fetchListOfVenues(testLatitude, testLongitude)

        viewModel.updateLocation(testLatitude.toDouble(), testLongitude.toDouble())

        viewModel.fetchListOfVenues()
        advanceUntilIdle()

        // Then
        Mockito.verify(viewStateObserver, Mockito.times(2)).onChanged(viewStateCaptor.capture())

        Assert.assertEquals(
            ListOfVenuesViewState.Error("error"), viewStateCaptor.allValues[1]
        )
    }
}