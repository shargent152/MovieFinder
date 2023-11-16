package com.example.moviefinder2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieinfoAPI {
    @GET("/")
    fun getMovie(@Query("t") title: String, @Query("apikey") apiKey: String):
            Call<MovieDetails>
}