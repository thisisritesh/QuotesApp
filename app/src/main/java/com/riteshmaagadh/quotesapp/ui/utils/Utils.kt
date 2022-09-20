package com.riteshmaagadh.quotesapp.ui.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService
import com.riteshmaagadh.quotesapp.R


object Utils {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager?.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun overrideEnterAnimation(activity: Activity){
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }

    fun overrideExitAnimation(activity: Activity){
        activity.overridePendingTransition(R.anim.left_in, R.anim.right_out)
    }

}