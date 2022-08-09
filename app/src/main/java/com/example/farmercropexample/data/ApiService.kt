package com.example.farmercropexample.data

import com.example.farmercropexample.data.model.PullRequest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    @JvmSuppressWildcards
    @GET("resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd00000174f859915f094b104c137ec8aea26728&format=json")
    suspend fun getPullRequests(
        @QueryMap(encoded = false) options: Map<String, Any>

    ): Response<PullRequest>

}