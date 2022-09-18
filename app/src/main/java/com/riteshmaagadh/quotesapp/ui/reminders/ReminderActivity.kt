package com.riteshmaagadh.quotesapp.ui.reminders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.riteshmaagadh.quotesapp.databinding.ActivityReminderBinding

class ReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReminderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }
}