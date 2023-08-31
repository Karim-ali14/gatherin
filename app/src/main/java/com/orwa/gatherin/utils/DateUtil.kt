package com.orwa.gatherin.utils

import java.text.SimpleDateFormat
import java.util.*


class DateUtil {
    companion object {

        //Types for time units
        private const val TIME_UNIT_SECOND = "second"
        private const val TIME_UNIT_MINUTE = "minute"
        private const val TIME_UNIT_HOUR = "hour"
        private const val TIME_UNIT_DAY = "day"
        private const val TIME_UNIT_MONTH = "month"
        private const val TIME_UNIT_YEAR = "year"

        fun getFormattedDate(date: Long): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd  HH:mm",Locale.getDefault())
//            dateFormat.timeZone= TimeZone.getTimeZone("UTC")
            return dateFormat.format(Date(date))
        }

//        fun getMessageFormattedDate(date: Long): String {
//            val dateFormat = SimpleDateFormat("MM-dd  HH:mm", Locale.getDefault())
//            return dateFormat.format(Date(date))
//        }


//        fun getFOCMonthAndYearNum(time: Long): String {
//            val calendar = Calendar.getInstance()
//            val date = Date(time)
//            calendar.time = date
//            val yearNum = calendar.get(Calendar.YEAR)
//            val last2Digits = yearNum % 100
//
//            val dateFormat = SimpleDateFormat("MMM", Locale.getDefault())
//            val dateShowValue = dateFormat.format(time)
//            val showValue = String.format(Locale.getDefault(), "%s %d", dateShowValue, last2Digits)
//
//            return showValue
//        }

//        fun getMonthNumberFromTime(time: Long): Int {
//            val calendar = Calendar.getInstance()
//            val date = Date(time)
//            calendar.time = date
//            val monthNumber = calendar.get(Calendar.MONTH) + 1
//            return monthNumber
//        }
//
//        fun getYearNumberFromTime(time: Long): Int {
//            val calendar = Calendar.getInstance()
//            val date = Date(time)
//            calendar.time = date
//            val yearNumber = calendar.get(Calendar.YEAR)
//            return yearNumber
//        }
//
//        fun getTextTimeFromNumbers(yearNumber: Int, monthNumber: Int): String {
//            val calendar = Calendar.getInstance()
//            //Retrieve from DB as month number+1 when count from 0 index
//            calendar.set(Calendar.MONTH, monthNumber - 1)
//            calendar.set(Calendar.YEAR, yearNumber)
//            val monthFormatter = SimpleDateFormat("MMM", Locale.getDefault())
//            val monthName = monthFormatter.format(calendar.time)
//            val result = String.format("%s %s",monthName, yearNumber % 100)
//            return result
//        }

//        fun designFromTime(context: Context, date: Long, current_timestamp: Long): String {
//            Log.i("TIME","date=${getFormattedDate(date)}")
//            Log.i("TIME","current=${getFormattedDate(current_timestamp)}")
//
//            val newDate = getNewDate(date)
////            val newDate=date
//            var designed = ""
//            try {
//                val calendar = Calendar.getInstance()
////                Log.i("date","zone=$zone")
//                val d = Date(newDate)
//                calendar.time = d
//                val dif=current_timestamp - calendar.timeInMillis
//                Log.i("TIME_DIF","time_dif=$dif")
//
//                var difference = (current_timestamp - calendar.timeInMillis) / 1000
//                if (difference >= 0) {
//                    if (difference == 0L)
//                        designed = context.resources.getString(R.string.just_now)
//                    else if (difference == 1L)
//                        designed = context.resources.getString(R.string.from_one_second)
//                    else if (difference == 2L)
//                        designed = context.resources.getString(R.string.from_two_second)
//                    else if (difference <= 10)
//                        designed =
//                            context.resources.getString(R.string.from_date) + " " + difference + " " + context.resources.getString(
//                                R.string.seconds
//                            )
//                    else if (difference < 60)
//                        designed =
//                            context.resources.getString(R.string.from_date) + " " + difference + " " + getPluralName(
//                                TIME_UNIT_SECOND, context
//                            )
//                    else {
//                        difference = difference / 60
//                        if (difference == 1L)
//                            designed = context.resources.getString(R.string.from_one_minute)
//                        else if (difference == 2L)
//                            designed = context.resources.getString(R.string.from_two_minutes)
//                        else if (difference <= 10)
//                            designed =
//                                context.resources.getString(R.string.from_date) + " " + difference + " " + context.resources.getString(
//                                    R.string.minutes
//                                )
//                        else if (difference < 60)
//                            designed =
//                                context.resources.getString(R.string.from_date) + " " + difference + " " + getPluralName(
//                                    TIME_UNIT_MINUTE, context
//                                )
//                        else {
//                            difference = difference / 60
//                            if (difference == 1L)
//                                designed = context.resources.getString(R.string.from_one_hour)
//                            else if (difference == 2L)
//                                designed = context.resources.getString(R.string.from_two_hours)
//                            else if (difference <= 10)
//                                designed =
//                                    context.resources.getString(R.string.from_date) + " " + difference + " " + context.resources.getString(
//                                        R.string.hours
//                                    )
//                            else if (difference < 24)
//                                designed =
//                                    context.resources.getString(R.string.from_date) + " " + difference + " " + getPluralName(
//                                        TIME_UNIT_HOUR, context
//                                    )
//                            else {
//                                difference = difference / 24
//                                if (difference == 1L)
//                                    designed = context.resources.getString(R.string.from_one_day)
//                                else if (difference == 2L)
//                                    designed = context.resources.getString(R.string.from_two_days)
//                                else if (difference <= 10)
//                                    designed =
//                                        context.resources.getString(R.string.from_date) + " " + difference + " " + context.resources.getString(
//                                            R.string.days
//                                        )
//                                else if (difference < 30)
//                                    designed =
//                                        context.resources.getString(R.string.from_date) + " " + difference + " " + getPluralName(
//                                            TIME_UNIT_DAY, context
//                                        )
//                                else {
//                                    difference = difference / 30
//                                    if (difference == 1L)
//                                        designed =
//                                            context.resources.getString(R.string.from_one_month)
//                                    else if (difference == 2L)
//                                        designed =
//                                            context.resources.getString(R.string.from_two_months)
//                                    else if (difference <= 10)
//                                        designed =
//                                            context.resources.getString(R.string.from_date) + " " + difference + " " + context.resources.getString(
//                                                R.string.months
//                                            )
//                                    else if (difference < 12)
//                                        designed =
//                                            context.resources.getString(R.string.from_date) + " " + difference + " " + getPluralName(
//                                                TIME_UNIT_MONTH, context
//                                            )
//                                    else {
//                                        difference = difference / 12
//                                        if (difference == 1L)
//                                            designed =
//                                                context.resources.getString(R.string.from_one_year)
//                                        else if (difference == 2L)
//                                            designed =
//                                                context.resources.getString(R.string.from_two_years)
//                                        else if (difference <= 10)
//                                            designed =
//                                                context.resources.getString(R.string.from_date) + " " + difference + " " + context.resources.getString(
//                                                    R.string.years
//                                                )
//                                        else
//                                            designed =
//                                                context.resources.getString(R.string.from_date) + " " + difference + " " + getPluralName(
//                                                    TIME_UNIT_YEAR, context
//                                                )
//                                    }
//                                }
//                            }
//                        }
//                    }
//                } else if (difference > -240)
//                    designed = context.resources.getString(R.string.now)
//                else {
//                    designed = context.resources.getString(R.string.future)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//            return designed
//        }

//        fun designFromTime(context: Context, date: Long): String {
//            return designFromTime(context, date, System.currentTimeMillis())
//        }

//        fun getPluralName(timeUnit: String, ctx: Context): String {
//            val plural = !Locale.getDefault().language.equals("ar")
//            Log.i("util","plural=$plural")
//            return when (timeUnit) {
//                TIME_UNIT_SECOND -> {
//                    if (plural) {
//                        ctx.getString(R.string.seconds)
//                    } else {
//                        ctx.getString(R.string.second)
//                    }
//                }
//                TIME_UNIT_MINUTE -> {
//                    if (plural) {
//                        ctx.getString(R.string.minutes)
//                    } else {
//                        ctx.getString(R.string.minute)
//                    }
//                }
//                TIME_UNIT_HOUR -> {
//                    if (plural) {
//                        ctx.getString(R.string.hours)
//                    } else {
//                        ctx.getString(R.string.hour)
//                    }
//                }
//                TIME_UNIT_DAY -> {
//                    if (plural) {
//                        ctx.getString(R.string.days)
//                    } else {
//                        ctx.getString(R.string.day)
//                    }
//                }
//                TIME_UNIT_MONTH -> {
//                    if (plural) {
//                        ctx.getString(R.string.months)
//                    } else {
//                        ctx.getString(R.string.month)
//                    }
//                }
//                TIME_UNIT_YEAR -> {
//                    if (plural) {
//                        ctx.getString(R.string.years)
//                    } else {
//                        ctx.getString(R.string.year)
//                    }
//                }
//                else -> ""
//            }
//        }

        /**
         * Get the correct date from server after subtracting 2 hours
         */
//        fun getNewDate(date:Long):Long{
//            return (date - (2 * 60 * 60 * 1000))
//        }

        /**
         * Get the correct date from server
         */
//        fun getCorrectDate(date:Long):Long{
//            val tz=TimeZone.getDefault()
//            val HOUR_TIME:Long=60*60*1000
//            val newDate = date + (-1 * HOUR_TIME)
//            Log.i("TIME_TEST","correct_Time=$newDate")
//            return newDate
//        }

        fun getDateFromUTC(utcDateString:String):Date? {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.getDefault())
            parser.setTimeZone(TimeZone.getTimeZone("UTC"))
            return parser.parse(utcDateString)
        }

        fun getUTCFromDate(date:Date):String{
            val calendar = Calendar.getInstance()
            calendar.timeZone = TimeZone.getTimeZone("UTC")
            calendar.time = date
            val time = calendar.time
            val outputFmt =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.getDefault())
            val dateAsString = outputFmt.format(time)
            return dateAsString
            //MMM dd, yyy h:mm a zz
        }

        fun getDateToDisplayFromStringDate(utc:String):String{
            val date = getDateFromUTC(utc)
            val v = date?.let { displayDate(it) }
            return v.toString()
        }

        fun getDateAsString(year:Int, month:Int, day:Int):String{
            val calendar = Calendar.getInstance()
            calendar[Calendar.YEAR] = year
            calendar[Calendar.MONTH] = month
            calendar[Calendar.DAY_OF_MONTH] = day
            return getUTCFromDate(calendar.time)
        }

        fun displayDate(date:Date):String{
            val calendar = Calendar.getInstance()
            calendar.timeZone = TimeZone.getTimeZone("UTC")
            calendar.time = date
            val time = calendar.time
            val outputFmt =
                SimpleDateFormat("yyyy-MMM-dd  HH:mm",Locale.getDefault())
            val dateAsString = outputFmt.format(time)
            return dateAsString
        }

        fun getCorrectMonthNumberToDisplay(month:Int):Int{
            if(month==11){
                return 12
            }else return month+1%12

        }
    }



}