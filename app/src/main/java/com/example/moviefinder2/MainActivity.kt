package com.example.moviefinder2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var recycle : RecyclerView
    private lateinit var manager : RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainScreen = MainScreen()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.holder,mainScreen)
            commit()
        }
        manager = LinearLayoutManager(this)
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
            run.enqueue(object : Callback<search>{
                override fun onResponse(
                    call: Call<search>,
                    response: Response<search>
                ){
                    if (response.isSuccessful){
                        val movieDetails = response.body()
                        Log.i("hope","test")
                        Log.i("hope",response.body().toString())
                        recycle = findViewById<RecyclerView>(R.id.recycle).apply {
                            myAdapter = SelectedMovie(response.body()!!)
                            layoutManager = manager
                            adapter = myAdapter
                        }

                    }else{
                        Toast.makeText(this@MainActivity, "Movie Not Found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<search>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "WIFI has failed please try again later", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

