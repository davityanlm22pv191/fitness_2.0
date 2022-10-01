package com.example.fitness20

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.FragmentContainer
import androidx.lifecycle.ViewModelProvider
import com.example.fitness20.databinding.FragmentCalendarBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CalendarBottomSheet : BottomSheetDialogFragment() {

    private lateinit var bind: FragmentCalendarBottomSheetBinding
    private lateinit var calendarViewModel: CalendarViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        calendarViewModel = ViewModelProvider(activity).get(CalendarViewModel::class.java)

        bind.bClose.setOnClickListener{ dismiss()}

        bind.bShow.setOnClickListener{ saveAction() }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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