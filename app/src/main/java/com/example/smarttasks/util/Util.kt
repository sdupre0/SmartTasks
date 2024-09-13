package com.example.smarttasks.util

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


enum class Status { Unresolved, Resolved, CantResolve }

val today: LocalDate = LocalDate.now()

fun readableDate(date: String): String {
    return LocalDate.parse(date).format(DateTimeFormatter.ofPattern("LLL dd yyyy"))
}

fun LocalDate.simpleDate(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

fun TextView.setColor(context: Context, id: Int) {
    this.setTextColor(ContextCompat.getColor(context, id))
}

@BindingAdapter("bindTitle")
fun bindTitle(textView: TextView, date: String) {
    textView.text = if (date == today.simpleDate()) "Today" else readableDate(date)
}

@BindingAdapter("bindDueDate")
fun bindDueDate(textView: TextView, date: String) {
    textView.text = readableDate(date)
}

@BindingAdapter("bindDaysLeft")
fun bindDaysLeft(textView: TextView, dueDate: String) {
    val daysLeft = ChronoUnit.DAYS.between(
        LocalDate.now(),
        LocalDate.parse(dueDate)
    )
    textView.text = daysLeft.toString()
}

@BindingAdapter("bindTaskStatus")
fun bindTaskStatus(textView: TextView, status: Status) {
    textView.text = if (status == Status.CantResolve) Status.Unresolved.name else status.name
}