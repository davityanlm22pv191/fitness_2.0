package com.example.fitness20

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalendarViewModel: ViewModel() {
    var firstDate = MutableLiveData<String>()
    var lastDate = MutableLiveData<String>()
}