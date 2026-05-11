package com.ojasx.whotouchedmyphone.Screens.HomeScreen.cards

import com.ojasx.whotouchedmyphone.RoomDb.Logs.IntruderLog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object IntrusionStats {

    fun getTotalIntrusions(
        logs: List<IntruderLog>
    ): Int {

        return logs.size
    }

    fun getThisWeekIntrusions(
        logs: List<IntruderLog>
    ): Int {

        val calendar = Calendar.getInstance()

        calendar.add(Calendar.DAY_OF_YEAR, -7)

        val weekAgo = calendar.timeInMillis

        return logs.count {

            it.timestamp >= weekAgo
        }
    }

    fun getThisMonthIntrusions(
        logs: List<IntruderLog>
    ): Int {

        val calendar = Calendar.getInstance()

        calendar.add(Calendar.DAY_OF_YEAR, -30)

        val monthAgo = calendar.timeInMillis

        return logs.count {

            it.timestamp >= monthAgo
        }
    }

    fun getLastIntrusionDate(
        logs: List<IntruderLog>
    ): String {

        if (logs.isEmpty()) {

            return "No Data"
        }

        return SimpleDateFormat(
            "dd MMM",
            Locale.getDefault()
        ).format(
            Date(logs.first().timestamp)
        )
    }

    fun getLastIntrusionTime(
        logs: List<IntruderLog>
    ): String {

        if (logs.isEmpty()) {

            return "--:--"
        }

        return SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        ).format(
            Date(logs.first().timestamp)
        )
    }
}