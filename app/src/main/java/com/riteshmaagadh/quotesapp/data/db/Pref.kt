package com.riteshmaagadh.quotesapp.data.db

import android.app.Application
import android.content.Context

object Pref {

    const val PREF_NAME = "quotes_pref"
    const val SELECTED_QUOTE_FONT_ID = "selected_quote_font_id"
    const val SELECTED_WRITER_FONT_ID = "selected_writer_font_id"
    const val SELECTED_THEME_URL = "selected_theme_url"

    const val THEME_IMAGE_URL = "theme_image_url"

    fun putPref(context: Context, key: String, value: String){
        val sharedPref = context.getSharedPreferences(Pref.PREF_NAME, Application.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun putPref(context: Context, key: String, value: Int){
        val sharedPref = context.getSharedPreferences(Pref.PREF_NAME, Application.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun putPref(context: Context, key: String, value: Boolean){
        val sharedPref = context.getSharedPreferences(Pref.PREF_NAME, Application.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getPrefString(context: Context, key: String) : String {
        return context.getSharedPreferences(Pref.PREF_NAME, Context.MODE_PRIVATE).getString(key, "")!!
    }

    fun getPrefInt(context: Context, key: String) : Int {
        return context.getSharedPreferences(Pref.PREF_NAME, Context.MODE_PRIVATE).getInt(key, 0)
    }

    fun getPrefBoolean(context: Context, key: String) : Boolean {
        return context.getSharedPreferences(Pref.PREF_NAME, Context.MODE_PRIVATE).getBoolean(key, false)
    }

}