package com.example.farmercropexample.presentation

import com.example.farmercropexample.MainCoroutineRule
import com.example.farmercropexample.data.ApiService
import com.example.farmercropexample.data.model.PullRequest
import com.example.farmercropexample.data.util.AppDispatchers
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response


class MainViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val hashMap = HashMap<String, Any>()

    private val testDispatcher = AppDispatchers(
        IO = TestCoroutineDispatcher()
    )

    private val userServices = mock<ApiService>()

    private lateinit var viewModel: MainViewModel

    @Test
    fun `Success state works`() = runBlocking {
        whenever(userServices.getPullRequests(hashMap)).thenReturn(
            Response.success(
                PullRequest(
                    emptyList()
                )
            )
        )
        viewModel = MainViewModel(userServices, testDispatcher)
        Assert.assertEquals(State.SUCCESS(PullRequest(emptyList())), viewModel.state.value)
    }

    @Test
    fun `Failure state works`() = runBlocking {
        whenever(userServices.getPullRequests(hashMap)).thenThrow(RuntimeException("Error"))
        viewModel = MainViewModel(userServices, testDispatcher)
        Assert.assertEquals(State.FAILURE("Error"), viewModel.state.value)
    }

    @Test
    fun `Loading state works`() = runBlocking {
        whenever(userServices.getPullRequests(hashMap)).doSuspendableAnswer {
            withContext(Dispatchers.IO) { delay(5000) }
            Response.success(PullRequest(emptyList()))
        }

        viewModel = MainViewModel(userServices, testDispatcher)
        Assert.assertEquals(State.LOADING, viewModel.state.value)

    }

    @Test
    fun `Search state works`() = runBlocking {
        viewModel = MainViewModel(userServices, testDispatcher)
        whenever(userServices.getPullRequests(hashMap)).doSuspendableAnswer {
            withContext(Dispatchers.IO) { delay(5000) }
            Response.success(PullRequest(emptyList()))
        }
        viewModel.setFilter("")
        Assert.assertEquals(false,viewModel.showSearch.value)
    }

    @Test
    fun `Search state visible works`() = runBlocking {
        viewModel = MainViewModel(userServices, testDispatcher)
        whenever(userServices.getPullRequests(hashMap)).doSuspendableAnswer {
            withContext(Dispatchers.IO) { delay(5000) }
            Response.success(PullRequest(emptyList()))
        }
        viewModel.setFilter("Testt")
        Assert.assertEquals(true,viewModel.showSearch.value)
    }
}