package com.example.fitness20

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness20.databinding.MonthItemBinding
import java.time.LocalDate

/* Это RecyclerView, который будет отображать месяц полностью
* Сначала идет название месяца и год, ниже будут даты согласно
* https://www.figma.com/file/sosLpZVaVP0cQiJbOB7GgP/Sport-v3-Practice?node-id=401%3A22615 */
class MonthAdapter: RecyclerView.Adapter<MonthAdapter.MonthHolder>() {

    /* В этой переменной будут хранится месяца, которые следует отобразить
    * в окончательной форме, см. class DateItem */
    private val monthList = ArrayList<DateItem>()

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
    }

    class MonthHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = MonthItemBinding.bind(item)

        fun bind(dateItem: DateItem, daysInMonthQty: Int, dayOfWeekNumber: Int) = with(binding){
            /* В этом учатке кода мы работаем с заголовком месяца */
            val chooseMonth = Month.values()[dateItem.date.month.value - 1].title
            tvMonthPlusYear.text = chooseMonth + " " + dateItem.date.year.toString()

            /* В этом участке мы работаем с заполнением дат календаря */
            for (i in 1 until dayOfWeekNumber){
                var date = TextView(itemView.context)
                date.text = ""
                date = dateAction(dateItem, date)
                glDatesOfMonth.addView(date)
            }

            for (i in 1..daysInMonthQty){
                var date = TextView(itemView.context)
                date.text = i.toString()
                date = dateAction(dateItem, date)
                glDatesOfMonth.addView(date)
            }

        }

        private fun dateAction(dateItem: DateItem, date: TextView): TextView{
            date.setPadding(37)
            date.textSize = 16F
            if(date.text == "") return date


            /* Остановился здесь, нужно сделать так, чтобы сегодняшний день
            * отображался пустым кружочком. Проблема: в dateItem не меняется дата
            * Поиграться с инкрементом LocalDate в creteHolder */
//            val today = LocalDate.now()
//            if (dateItem.date == today) {
//                date.setBackgroundResource(R.drawable.empty_ellipse)
//            }

            date.setOnClickListener{
                if(!dateItem.isSelected){
                    date.setBackgroundResource(R.drawable.fill_ellipse)
                    dateItem.isSelected = true
                } else{
                    date.background = null
                    dateItem.isSelected = false
                }
            }
            return date
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

        holder.bind(monthList[position], daysInMonthQty, dayOfWeekNumber)


    }

    override fun getItemCount(): Int {
        return monthList.size
    }

    fun add(dateItem: DateItem){
        monthList.add(dateItem)
    }
}

