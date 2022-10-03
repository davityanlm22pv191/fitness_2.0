package com.example.fitness20

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.fitness20.databinding.ActivityMainBinding
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding
    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        bind.btOpenCalendar.setOnClickListener{
            CalendarBottomSheet().show(supportFragmentManager, "thisIsCalendar")
        }

        calendarViewModel.firstDate.observe(this){
            bind.tvFirstDate.text = String.format("%s", it)
        }

        calendarViewModel.lastDate.observe(this){
            bind.tvLastDate.text = String.format("%s", it)
        }

    }


}