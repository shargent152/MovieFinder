package com.example.moviefinder2

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SelectedMovie(private val data: search) : RecyclerView.Adapter<SelectedMovie.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movieDetail: MovieDetails) {
            val key = "59f0f0ff"
            val title = movieDetail.Title
            var interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            var client : OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            val apiRunner = retrofit.create(MovieinfoAPI::class.java)
            val run = apiRunner.getMovie(title, key)
            run.enqueue(object : Callback<MovieDetails>{
                override fun onResponse(
                    call: Call<MovieDetails>,
                    response: Response<MovieDetails>
                ){
                    if (response.isSuccessful){
                        Log.i("hope","test")
                        Log.i("hope",response.body().toString())
                        viewChanger(response.body()!!)
                    }else{
                        Toast.makeText(view.context, "Movie Not Found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                    Toast.makeText(view.context, "WIFI has failed please try again later", Toast.LENGTH_SHORT).show()
                }
            })

        }
        fun viewChanger(movieDetails: MovieDetails){
            val titleView = view.findViewById<TextView>(R.id.name)
            val posterView = view.findViewById<ImageView>(R.id.poster)
            val releaseDateView = view.findViewById<TextView>(R.id.releasedate)
            val ratingView = view.findViewById<TextView>(R.id.rating)
            val lengthView = view.findViewById<TextView>(R.id.length)
            val genreView = view.findViewById<TextView>(R.id.Genre)
            val imdbratingView = view.findViewById<TextView>(R.id.IMDB_rating)
            val imdbURLView = view.findViewById<TextView>(R.id.IMDB_url)
            val shareBTN = view.findViewById<Button>(R.id.share)
            titleView.setText(movieDetails.Title)
            releaseDateView.setText(movieDetails.Year)
            ratingView.setText(movieDetails.Rated)
            Log.i("rate",movieDetails.Rated)
            Log.i("run",movieDetails.Runtime)
            Log.i("genes",movieDetails.Genre)
            Log.i("imdb",movieDetails.imdbRating)
            lengthView.setText(movieDetails.Runtime)
            genreView.setText(movieDetails.Genre)
            imdbratingView.setText(movieDetails.imdbRating)
            Glide.with(view.context).load(movieDetails.Poster).centerCrop().into(posterView)
            var imdburl = "https://www.imdb.com/title/" + movieDetails.imdbID
            imdbURLView.setText(imdburl)
            imdbURLView.setOnClickListener {
                val intent: Intent = Intent(Intent.ACTION_VIEW)
                intent.setData(Uri.parse(imdburl))
                view.context.startActivity(intent)
            }
            shareBTN.setOnClickListener{
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "This is the Title" + movieDetails.Title +
                    " and this the imdb link " + imdburl)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                view.context.startActivity(shareIntent)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedMovie.MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_selected_movie,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectedMovie.MyViewHolder, position: Int) {
        holder.bind(data.Search[position])
    }

    override fun getItemCount(): Int {
        return data.Search.size
    }
    fun intentRunner(){

    }
}