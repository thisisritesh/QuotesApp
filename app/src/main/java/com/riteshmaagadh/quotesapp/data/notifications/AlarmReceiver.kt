package com.riteshmaagadh.quotesapp.data.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, mIntent: Intent?) {
        val notificationUtils = NotificationUtils(context!!)
        notificationUtils.launchNotification()
    }
}