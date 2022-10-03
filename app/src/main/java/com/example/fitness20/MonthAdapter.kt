package com.example.fitness20

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness20.databinding.MonthItemBinding
import java.time.LocalDate

class MonthAdapter: RecyclerView.Adapter<MonthAdapter.MonthHolder>() {
    private val monthList = ArrayList<MonthItem>()
    private val dateList = ArrayList<Date>()

    data class Date(val date: String, val isSelected: Boolean = false)

    enum class Month(val title: String) {
        JANUARY("Январь"),
        FEBRUARY( "Февраль"),
        MARCH( "Март"),
        APRIL( "Апрель"),
        MAY( "Май"),
        JUNE( "Июнь"),
        JULY("Июль"),
        AUGUST( "Август"),
        SEPTEMBER( "Сентябрь"),
        OCTOBER("Октябрь"),
        NOVEMBER( "Ноябрь"),
        DECEMBER( "Декабрь");

        fun getNumberByTitle(value: String): Int{
            for (i in 0..11){
                if(values()[i].title == value)
                    return i
            }
            return 1
        }
    }

    inner class MonthHolder(item: View): RecyclerView.ViewHolder(item) {

        private val binding = MonthItemBinding.bind(item)


        fun bind(dateList: ArrayList<Date>, position: Int) = with(binding){

            if(!isDigit(dateList[position].date)){

                tvMonthPlusYear.text = dateList[position].date
                tvMonthPlusYear.textSize = 16F
            }
            else{

                var tvDate = TextView(itemView.context)


                if (dateList[position].date.toInt() == 0){
                    tvDate.text = ""

                } else{
                    val textColor = ContextCompat.getColor(itemView.context, R.color.white)
                    tvDate.setTextColor(textColor)
                    tvDate.text = dateList[position].date
                }
                glDatesOfMonth.addView(tvDate)


                tvDate.gravity = Gravity.CENTER
                tvDate.textSize = 16F
                val mParams = tvDate.layoutParams as GridLayout.LayoutParams
//                mParams.apply {
//                    width = 200
//                    height = 220
//
//                }
                mParams.topMargin = 20
                tvDate.layoutParams = mParams
            }
        }

        private fun isDigit(string: String): Boolean{
            if ((string[0] >= '0') and (string[0] <= '9')){
                return true
            }
            return false
        }

        private fun getFirstWord(string: String): String{
            var i = 0
            var word = String()
            while(string[i] != ' '){
                word += string[i]
                i++
            }
            return word
        }

        private fun getLastWord(string: String): String{
            var i = string.length - 1
            var word = String()
            while(string[i] != ' '){
                word += string[i]
                i--
            }
            return word.reversed()
        }

        private fun calcDate(dateList: ArrayList<Date>, position: Int): LocalDate{
            for (i in position downTo 0){
                if(!isDigit(dateList[i].date)){
                    val month = getFirstWord(dateList[i].date)
                    val monthOrdinal = Month.JANUARY.getNumberByTitle(month)
                    val year = getLastWord(dateList[i].date).toInt()
                    return LocalDate.of(year, monthOrdinal, dateList[position].date.toInt())
                }
            }
            return LocalDate.now()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.month_item, parent, false)
        return MonthHolder(view)
    }

    override fun onBindViewHolder(holder: MonthHolder, position: Int) {

        /* Подготовка переменных для заполнения календаря
        * monthFirstDay - первое число месяца из monthList[position]
        * daysInMonthQty - количество дней в  месяца из monthList[position]
        * firstDayOfWeek - номер первого дня недели месяца*/
        val monthFirstDay = LocalDate.of(monthList[position].date.year,
            monthList[position].date.month, 1)
        val daysInMonthQty = monthFirstDay.lengthOfMonth()
        val dayOfWeekNumber = monthFirstDay.dayOfWeek.value
        var listPosition = dateList.size

        var monthPlusYear = Month.values()[monthList[position].date.monthValue - 1].title
        monthPlusYear += " "
        monthPlusYear += monthList[position].date.year.toString()
        dateList.add(Date(monthPlusYear))
        holder.bind(dateList, listPosition)

        /* Пустые даты календаря */
        for (i in 1 until dayOfWeekNumber){
            val date = Date("0")
            dateList.add(date)
            listPosition++
            holder.bind(dateList, listPosition)
        }

        /* Числовые значения календаря */
        for (i in 1..daysInMonthQty){
            val date = Date(i.toString())
            dateList.add(date)
            listPosition++
            holder.bind(dateList, listPosition)
        }

    }

    override fun getItemCount(): Int {
        return monthList.size
    }

    fun addMonth(monthItem: MonthItem){
        monthList.add(monthItem)
    }

}