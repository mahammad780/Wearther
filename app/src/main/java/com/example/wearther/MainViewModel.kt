package com.example.wearther

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wearther.model.WeatherModel

class MainViewModel : ViewModel() {
    val liveDataCurrent = MutableLiveData<WeatherModel>()
    val liveDataList = MutableLiveData<List<WeatherModel>>()
}