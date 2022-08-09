package com.example.farmercropexample.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmercropexample.data.ApiService
import com.example.farmercropexample.data.model.PullRequest
import com.example.farmercropexample.data.util.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiService: ApiService,
    private val appDispatchers: AppDispatchers,
) : ViewModel() {

    val state = MutableStateFlow<State>(State.START)

    //    private val offset = 0
//    private val limit = 20
    private var filter: String = ""
    private var searchValue: String = ""
    val showSearch = MutableStateFlow(false)

    init {
        fetchGithubRepository()
    }

    private fun fetchGithubRepository() =
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            state.value = State.FAILURE(throwable.message.toString())
            Log.e("Error", throwable.message.toString())
        }) {

            state.value = State.LOADING
            val users = withContext(appDispatchers.IO) { apiService.getPullRequests(queryMap()) }

            state.value = State.SUCCESS(users.body() ?: PullRequest(emptyList()))
        }

    fun loadData() {
        fetchGithubRepository()
    }


    private fun queryMap(): HashMap<String, Any> {
        return HashMap<String, Any>().apply {
//            put("offset", offset)
//            put("limit", limit)
            if (filter.isNotEmpty()) put("filters[$filter]", searchValue)
        }
    }

    fun setFilter(filterSelected: String) {
        showSearch.value = filterSelected.isNotEmpty()
        filter = filterSelected
        fetchGithubRepository()
    }

    fun searchValue(searchText: String) {
        searchValue = searchText
        fetchGithubRepository()
    }

}

sealed class State {
    object START : State()
    object LOADING : State()
    data class SUCCESS(val users: PullRequest) : State()
    data class FAILURE(val message: String) : State()
}