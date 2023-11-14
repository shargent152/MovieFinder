package com.example.moviefinder2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.gson.GsonConverterFactory.create
import retrofit2.create
var name = "asfda"
var poster = ""
var year = ""
var rating = ""
var length = ""
var genre = ""
var imdbrating = ""
var imdburl = ""
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainScreen = MainScreen()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.holder,mainScreen)
            commit()
        }
        var viewModel: TitleViewModel = ViewModelProvider(this).get(TitleViewModel::class.java)
        viewModel.selectedTitle.observe(this){
            Log.i("bip","boop")
            val key = "59f0f0ff"
            Log.i("word",it)
            val title = it
            var interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            var client : OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            val apiRunner = retrofit.create(MovieAPI::class.java)
            val run = apiRunner.getMovie(title, key)
            Log.i("maybe",client.toString())
            run.enqueue(object : Callback<MovieDetails>{
                override fun onResponse(
                    call: Call<MovieDetails>,
                    response: Response<MovieDetails>
                ){
                    if (response.isSuccessful){
                        val movieDetails = response.body()
                        Log.i("hope","test")
                        Log.i("hope",response.body().toString())
                        movieTime(movieDetails)
                    }else{
                        Toast.makeText(this@MainActivity, "Movie Not Found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "WIFI has failed please try again later", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    fun movieTime(movieDetails: MovieDetails?){
        val selectedMovie = SelectedMovie()
        val mainScreen = MainScreen()
        movieDetails?.let {
            name = it.Title
            poster = it.Poster
            year = it.Year
            rating = it.Rated
            length = it.Runtime
            genre = it.Genre
            imdbrating = it.imdbRating
            imdburl = it.imdbID
            Log.i("hello", title as String)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.holder,selectedMovie)
                commit()
            }
        }
    }
}

