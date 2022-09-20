package com.riteshmaagadh.quotesapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.riteshmaagadh.quotesapp.MainActivity
import com.riteshmaagadh.quotesapp.R
import com.riteshmaagadh.quotesapp.data.db.Pref
import com.riteshmaagadh.quotesapp.ui.themes.ThemesActivity
import com.riteshmaagadh.quotesapp.ui.utils.Constants
import com.riteshmaagadh.quotesapp.ui.utils.Utils

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Pref.getPrefInt(this, Pref.SELECTED_QUOTE_FONT_ID) == 0){
            val intent = Intent(this, ThemesActivity::class.java)
            intent.putExtra(Constants.IS_FIRST_TIME_USER, true)
            startActivity(intent)
            Utils.overrideEnterAnimation(this)
            finish()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            Utils.overrideEnterAnimation(this)
            finish()
        }


    }

    override fun finish() {
        super.finish()
        Utils.overrideExitAnimation(this)
    }

}