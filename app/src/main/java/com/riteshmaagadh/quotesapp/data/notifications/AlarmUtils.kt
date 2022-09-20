package com.riteshmaagadh.quotesapp.data.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.riteshmaagadh.quotesapp.data.db.Pref
import java.util.*

class AlarmUtils(context: Context) {
    private var mContext = context
    private var alarmMgr: AlarmManager? = null
    private var alarmIntent: PendingIntent

    init {
        alarmMgr = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(mContext, AlarmReceiver::class.java).let { mIntent ->
        // if you want more than one notification use different requestCode
        // every notification need different requestCode
            PendingIntent.getBroadcast(mContext, 100, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    fun initRepeatingAlarm(){
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        alarmMgr?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_HOUR,
            alarmIntent
        )
        Pref.putPref(mContext, Pref.IS_NOTIFICATION_ENABLED,true)
    }

}