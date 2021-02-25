package com.cpen391.moduledemo.model.apiService

import com.cpen391.moduledemo.model.response.GithubRepo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {
    @GET("users/{users}/starred")
    fun getStarredRepositories(@Path("users") username: String): Observable<List<GithubRepo>>
}