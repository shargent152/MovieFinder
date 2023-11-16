package com.example.moviefinder2

import android.app.appsearch.AppSearchManager.SearchContext
import android.util.Log
import retrofit2.Call
import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.http.Path


interface MovieAPI {
    @GET("/")
    fun getMovie(@Query("s") title: String, @Query("apikey") apiKey: String):
            Call<search>
}