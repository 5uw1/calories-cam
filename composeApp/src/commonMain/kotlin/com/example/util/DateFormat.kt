package com.example.util

import com.example.model.FoodEntry
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Multiplatform date/time formatting.
 * Replaces java.text.SimpleDateFormat / java.util.Calendar which are JVM-only.
 * Thai, English & German month/day names are hardcoded so output does not depend on OS locale data.
 */
object DateFormat {

    private val thMonths = listOf(
        "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน",
        "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"
    )
    private val thMonthsShort = listOf(
        "ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.", "พ.ค.", "มิ.ย.",
        "ก.ค.", "ส.ค.", "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค."
    )
    private val thDays = listOf(
        "วันจันทร์", "วันอังคาร", "วันพุธ", "วันพฤหัสบดี", "วันศุกร์", "วันเสาร์", "วันอาทิตย์"
    )
    private val enMonths = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    private val enMonthsShort = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )
    private val enDays = listOf(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    )
    private val deMonths = listOf(
        "Januar", "Februar", "März", "April", "Mai", "Juni",
        "Juli", "August", "September", "Oktober", "November", "Dezember"
    )
    private val deMonthsShort = listOf(
        "Jan.", "Feb.", "März", "Apr.", "Mai", "Juni",
        "Juli", "Aug.", "Sep.", "Okt.", "Nov.", "Dez."
    )
    private val deDays = listOf(
        "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"
    )

    fun nowMillis(): Long = Clock.System.now().toEpochMilliseconds()

    private fun local(ts: Long) =
        Instant.fromEpochMilliseconds(ts).toLocalDateTime(TimeZone.currentSystemDefault())

    /** e.g. TH "วันจันทร์ที่ 8 กันยายน 2026" / EN "Monday, September 8, 2026" / DE "Montag, 8. September 2026" */
    fun fullDate(ts: Long, language: AppLanguage): String {
        val dt = local(ts)
        val dayIdx = dt.dayOfWeek.ordinal
        val monIdx = dt.monthNumber - 1
        return when (language) {
            AppLanguage.TH -> "${thDays[dayIdx]}ที่ ${dt.dayOfMonth} ${thMonths[monIdx]} ${dt.year}"
            AppLanguage.DE -> "${deDays[dayIdx]}, ${dt.dayOfMonth}. ${deMonths[monIdx]} ${dt.year}"
            AppLanguage.EN -> "${enDays[dayIdx]}, ${enMonths[monIdx]} ${dt.dayOfMonth}, ${dt.year}"
        }
    }

    /** e.g. TH "08:05 น. (8 ก.ย.)" / EN "8:05 AM (Sep 8)" / DE "08:05 Uhr (8. Sep.)" */
    fun timeWithDate(ts: Long, language: AppLanguage): String {
        val dt = local(ts)
        val monIdx = dt.monthNumber - 1
        return when (language) {
            AppLanguage.TH -> {
                val hh = dt.hour.toString().padStart(2, '0')
                val mm = dt.minute.toString().padStart(2, '0')
                "$hh:$mm น. (${dt.dayOfMonth} ${thMonthsShort[monIdx]})"
            }
            AppLanguage.DE -> {
                val hh = dt.hour.toString().padStart(2, '0')
                val mm = dt.minute.toString().padStart(2, '0')
                "$hh:$mm Uhr (${dt.dayOfMonth}. ${deMonthsShort[monIdx]})"
            }
            AppLanguage.EN -> {
                val h12 = when (val h = dt.hour % 12) { 0 -> 12; else -> h }
                val ampm = if (dt.hour < 12) "AM" else "PM"
                val mm = dt.minute.toString().padStart(2, '0')
                "$h12:$mm $ampm (${enMonthsShort[monIdx]} ${dt.dayOfMonth})"
            }
        }
    }

    /** e.g. TH "วันจันทร์ที่ 8 ก.ย." / EN "Monday, Sep 8" / DE "Montag, 8. Sep." (section headers) */
    fun dayHeader(ts: Long, language: AppLanguage): String {
        val dt = local(ts)
        val dayIdx = dt.dayOfWeek.ordinal
        val monIdx = dt.monthNumber - 1
        return when (language) {
            AppLanguage.TH -> "${thDays[dayIdx]}ที่ ${dt.dayOfMonth} ${thMonthsShort[monIdx]}"
            AppLanguage.DE -> "${deDays[dayIdx]}, ${dt.dayOfMonth}. ${deMonthsShort[monIdx]}"
            AppLanguage.EN -> "${enDays[dayIdx]}, ${enMonthsShort[monIdx]} ${dt.dayOfMonth}"
        }
    }

    /** Stable day key for grouping (year + day-of-year). */
    fun dayKey(ts: Long): String {
        val dt = local(ts)
        return "${dt.year}-${dt.dayOfYear}"
    }

    fun isSameDay(a: Long, b: Long): Boolean {
        val da = local(a); val db = local(b)
        return da.year == db.year && da.dayOfYear == db.dayOfYear
    }

    fun isToday(ts: Long): Boolean = isSameDay(ts, nowMillis())
}
