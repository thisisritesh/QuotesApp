package com.riteshmaagadh.quotesapp.ui.utils

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import com.riteshmaagadh.quotesapp.R

object AnimUtils {

    fun bounceView(view: View, context: Context){
        val anim = AnimationUtils.loadAnimation(context, R.anim.bounce_anim)
        view.startAnimation(anim)
    }

}