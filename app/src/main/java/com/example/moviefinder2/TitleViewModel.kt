package com.example.moviefinder2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TitleViewModel : ViewModel(){
    val title = MutableLiveData<String>()
    val selectedTitle: LiveData<String> get() = title
    fun selectTitle(item: String){
        title.value = item
    }
}