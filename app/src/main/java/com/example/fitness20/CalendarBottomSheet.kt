package com.example.fitness20

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitness20.databinding.FragmentCalendarBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate


class CalendarBottomSheet : BottomSheetDialogFragment() {

    private lateinit var bind: FragmentCalendarBottomSheetBinding
    private lateinit var calendarViewModel: CalendarViewModel
    private val monthAdapter = MonthAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        calendarViewModel = ViewModelProvider(activity).get(CalendarViewModel::class.java)

        /* Работает с RecyclerView MonthAdapter */
        bind.rcCalendar.adapter = monthAdapter
        bind.rcCalendar.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        var today = LocalDate.now()
        monthAdapter.add(DateItem(today.minusMonths(2)))
        monthAdapter.add(DateItem(today.minusMonths(1)))
        monthAdapter.add(DateItem(today))
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

}