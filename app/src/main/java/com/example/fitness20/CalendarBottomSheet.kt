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
    private var fst = LocalDate.now()
    private var snd = LocalDate.now()

    enum class GenitiveMonth(private val title: String){
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
        bind.tvToday.setOnClickListener { onClick(LocalDate.now(), LocalDate.now()) }


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bind = FragmentCalendarBottomSheetBinding.inflate(inflater, container, false)
        return bind.root
    }

    private fun saveAction(){

        calendarViewModel.firstDate.value = this.fst.toString()
        calendarViewModel.secondDate.value = this.snd.toString()

        dismiss()
    }

    override fun onClick(firstDate: LocalDate, secondDate: LocalDate) {

        val firstMonthOrdinal = firstDate.monthValue - 1
        val firstDayPlusMonth = "${firstDate.dayOfMonth} ${GenitiveMonth.JANUARY.getTitleByOrdinal(firstMonthOrdinal)}"
        var bShowTitle = resources.getString(R.string.show)

        /* Если пользователь 2 раза нажал на одну и ту же дату */
        if (firstDate == secondDate){
            bShowTitle += " $firstDayPlusMonth"
            bind.bShow.text = bShowTitle
            return
        }

        /* Случаи, если даты разные */
        val secondMonthOrdinal = secondDate.monthValue - 1
        val secondDayPlusMonth = "${secondDate.dayOfMonth} ${GenitiveMonth.JANUARY.getTitleByOrdinal(secondMonthOrdinal)}"

        /* Случай, если пользователь выбрал сначала бОльшую дату */
        if (firstDate > secondDate){
            bShowTitle += " $secondDayPlusMonth - $firstDayPlusMonth"
            bind.bShow.text = bShowTitle
            return
        }
        /* Случай, если выбрал сначала меньшую дату */
        bShowTitle += " $firstDayPlusMonth - $secondDayPlusMonth"
        bind.bShow.text = bShowTitle

        calendarViewModel.firstDate = MutableLiveData(firstDate.toString())
        calendarViewModel.secondDate = MutableLiveData(secondDate.toString())

    }
}