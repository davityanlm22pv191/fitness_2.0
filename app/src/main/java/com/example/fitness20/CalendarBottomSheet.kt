package com.example.fitness20

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitness20.databinding.FragmentCalendarBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate


class CalendarBottomSheet() :
                    BottomSheetDialogFragment(), MonthAdapter.CalendarListener {

    private lateinit var bind: FragmentCalendarBottomSheetBinding
    private lateinit var calendarViewModel: CalendarViewModel
    private val monthAdapter = MonthAdapter(this)

    enum class GenitiveMonth(val title: String){
        JANUARY("января"),
        FEBRUARY( "февраля"),
        MARCH( "марта"),
        APRIL( "апреля"),
        MAY( "мая"),
        JUNE( "июня"),
        JULY("июля"),
        AUGUST( "августа"),
        SEPTEMBER( "сентября"),
        OCTOBER("октября"),
        NOVEMBER( "ноября"),
        DECEMBER( "декабря");

        fun getTitleByOrdinal(ordinal: Int): String{
            return values()[ordinal].title
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        calendarViewModel = ViewModelProvider(activity).get(CalendarViewModel::class.java)

        /* Работаем с RecyclerView MonthAdapter */
        bind.rcCalendar.adapter = monthAdapter
        bind.rcCalendar.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val today = LocalDate.now()

        monthAdapter.addMonth(MonthItem(today.minusMonths(2)))
        monthAdapter.addMonth(MonthItem(today.minusMonths(1)))
        monthAdapter.addMonth(MonthItem(today))
        bind.rcCalendar.smoothScrollToPosition(monthAdapter.itemCount - 1)

        bind.bClose.setOnClickListener{ dismiss()}
        bind.bShow.setOnClickListener{ saveAction() }


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bind = FragmentCalendarBottomSheetBinding.inflate(inflater, container, false)
        return bind.root
    }

    private fun saveAction(){

//        calendarViewModel.name.value = bind.name.text.toString()
//        calendarViewModel.desc.value = bind.desc.text.toString()
//        bind.name.setText("")
//        bind.desc.setText("")

        dismiss()
    }

    override fun onClick(firstDate: LocalDate, secondDate: LocalDate) {
        val firstMonthNumber = firstDate.monthValue
        val secondMonthNumber = secondDate.monthValue
        val firstDayPlusMonth = "${firstDate.dayOfMonth} ${GenitiveMonth.JANUARY.getTitleByOrdinal(firstMonthNumber)}"
        val secondDayPlusMonth = "${secondDate.dayOfMonth} ${GenitiveMonth.JANUARY.getTitleByOrdinal(secondMonthNumber)}"
        var bShowTitle = resources.getString(R.string.show)
        bShowTitle += " $firstDayPlusMonth - $secondDayPlusMonth"
        bind.bShow.text = bShowTitle

//        calendarViewModel.firstDate = MutableLiveData(firstDate.toString())
//        calendarViewModel.lastDate = MutableLiveData(lastDate.toString())

    }




}