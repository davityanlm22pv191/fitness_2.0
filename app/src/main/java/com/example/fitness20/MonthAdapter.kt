package com.example.fitness20

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness20.databinding.MonthItemBinding
import java.time.LocalDate

/* Как хранятся месяцы календаря
* "Июнь 2022", "", "", "1", "2", "3", ...
* Делаем вывод, что Июнь 2022 начался со среды*/
class MonthAdapter(private val calendarListener: CalendarListener): RecyclerView.Adapter<MonthAdapter.MonthHolder>() {

    /* monthList - хранит все месяцы, которые следует отобразить (adapter.addMonth()) */
    /* dateList - хранит все даты, которые следует отобразить (заполняется в BindViewHolder) */
    private val monthList = ArrayList<MonthItem>()
    private val dateList = ArrayList<Date>()

    private val maxCalendarElement = 99999
    private var firstSelected = maxCalendarElement
    private var isRangeSelected = false

    /* Класс, который хранит все даты виде TextView, а так же флаг выбран он или нет */
    data class Date(val date: TextView,
                    var isSelected: Boolean = false)

    /* Так как месяцев всего 12, то использую перечисление для обращения */
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

    inner class MonthHolder(item: View): RecyclerView.ViewHolder(item){

        private val binding = MonthItemBinding.bind(item)

        fun bind(dateList: ArrayList<Date>, position: Int) = with(binding) {
            /* Здесь заполняется заголовок месяца */
            if (isMonth(dateList[position].date.text.toString())) {
                tvMonthPlusYear.text = dateList[position].date.text
                tvMonthPlusYear.textSize = 16F
            } else { /* Здесь заполняются непосредственно даты месяца */
                setAction(position)
                if (dateList[position].isSelected){
                    dateList[position].date.setBackgroundResource(R.drawable.empty_ellipse)
                    firstSelected = position
                }
                glDatesOfMonth.addView(dateList[position].date)
                val textColor = ContextCompat.getColor(itemView.context, R.color.white)
                dateList[position].date.setTextColor(textColor)
                dateList[position].date.gravity = Gravity.CENTER
                dateList[position].date.textSize = 16F
                val mParams = dateList[position].date.layoutParams as GridLayout.LayoutParams
                mParams.apply {
                    width = 132
                    height = 180

                }
                dateList[position].date.layoutParams = mParams
            }
        }

        /* Возвращает true - string хранит в себе месяц
        *  Используется для определения является ли строка месяцем
        *  Это заголовок месяца или даты*/
        private fun isMonth(string: String): Boolean{
            if ((string[0] >= '0') and (string[0] <= '9') or (string[0] == ' ')){
                return false
            }
            return true
        }

        /* Возвращает первое слово строки
        * Используется для взятия названия месяца из строки "Октябрь 2022"
        * Функция вернет "Октябрь" */
        private fun getFirstWord(string: String): String{
            var i = 0
            var word = String()
            while(string[i] != ' '){
                word += string[i]
                i++
            }
            return word
        }

        /* Возвращает последее слово строки
        * Используется для взятия года из строки "Октябрь 2022"
        * Функция вернет "2022" */
        private fun getLastWord(string: String): String{
            var i = string.length - 1
            var word = String()
            while(string[i] != ' '){
                word += string[i]
                i--
            }
            return word.reversed()
        }

        /* Функция считает дату по позиции элемента в dateList */
        private fun calcDate(dateList: ArrayList<Date>, position: Int?): LocalDate{
            if (position == null) return LocalDate.now()
            for (i in position downTo 0){
                if(isMonth(dateList[i].date.text.toString())){
                    val month = getFirstWord(dateList[i].date.text.toString())
                    val monthOrdinal = Month.JANUARY.getNumberByTitle(month) + 1
                    val year = getLastWord(dateList[i].date.text.toString()).toInt()
                    return LocalDate.of(year, monthOrdinal, dateList[position].date.text.toString().toInt())
                }
            }
            return LocalDate.now()
        }

        private fun setAction(position: Int){
            dateList[position].date.setOnClickListener {

                /* Случай, если нажали на пустую ячейку */
                if (dateList[position].date.text == " ") return@setOnClickListener

                if (position == firstSelected){
                    clearSelected()
                    dateList[position].date.setBackgroundResource(R.drawable.fill_ellipse)
                    calendarListener.onClick(calcDate(dateList, position), calcDate(dateList, firstSelected))
                    isRangeSelected = false
                    firstSelected = position
                    return@setOnClickListener
                }

                if (isRangeSelected){
                    clearSelected()
                    dateList[position].date.setBackgroundResource(R.drawable.fill_ellipse)
                    isRangeSelected = false
                    calendarListener.onClick(calcDate(dateList, position), calcDate(dateList, position))
                    firstSelected = position
                    return@setOnClickListener
                }

                if (position > firstSelected){
                    for (i in 1 until dateList.size){
                        if (dateList[i].date.text == " ") { continue }
                        else if (i < firstSelected || i > position){
                            dateList[i].date.background = null
                            continue
                        }
                        else if(i == firstSelected){
                            dateList[i].date.setBackgroundResource(R.drawable.left_side_ellipse)
                            continue
                        }
                        else if(i == position){
                            dateList[i].date.setBackgroundResource(R.drawable.right_side_ellipse)
                            continue
                        }
                        else if(i in firstSelected + 1 until position){
                            dateList[i].date.setBackgroundResource(R.drawable.intermediate)
                            continue
                        }
                    }
                }
                else if (position < firstSelected){
                    for (i in 1 until dateList.size){
                        if (dateList[i].date.text == " ") { continue }
                        else if(i < position || i > firstSelected){
                            dateList[i].date.background = null
                            continue
                        }
                        else if (i == position){
                            dateList[i].date.setBackgroundResource(R.drawable.left_side_ellipse)
                            continue
                        }
                        else if (i == firstSelected){
                            dateList[i].date.setBackgroundResource(R.drawable.right_side_ellipse)
                            continue
                        }
                        else if (i in position + 1 until firstSelected){
                            dateList[i].date.setBackgroundResource(R.drawable.intermediate)
                            continue
                        }
                    }
                }
                isRangeSelected = true

                calendarListener.onClick(calcDate(dateList, position), calcDate(dateList, firstSelected))
                firstSelected = position

            }
        }

        private fun clearSelected(){
            for (i in 1 until dateList.size){
                dateList[i].date.background = null
            }
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

        /* Заголовок календаря */
        var monthPlusYear = Month.values()[monthList[position].date.monthValue - 1].title
        monthPlusYear += " "
        monthPlusYear += monthList[position].date.year.toString()
        val tvMonthPlusYear = TextView(holder.itemView.context)
        tvMonthPlusYear.text = monthPlusYear
        dateList.add(Date(tvMonthPlusYear))
        holder.bind(dateList, listPosition)

        /* Пустые даты календаря */
        for (i in 1 until dayOfWeekNumber){
            val tvEmpty = TextView(holder.itemView.context)
            tvEmpty.text = " "
            val date = Date(tvEmpty)
            dateList.add(date)
            listPosition++
            holder.bind(dateList, listPosition)
        }

        /* Числовые значения календаря */
        for (i in 1..daysInMonthQty){

            val tvDate = TextView(holder.itemView.context)
            tvDate.text = i.toString()
            val date = Date(tvDate)
            val currentDay = LocalDate.of(monthList[position].date.year,
                monthList[position].date.monthValue, i)
            if (LocalDate.now() == currentDay){
                date.isSelected = true
            }
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

    interface CalendarListener{
        fun onClick(firstDate: LocalDate, secondDate: LocalDate)
    }

}