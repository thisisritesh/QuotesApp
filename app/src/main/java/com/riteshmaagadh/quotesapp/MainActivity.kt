package com.riteshmaagadh.quotesapp

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.palette.graphics.Palette
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.riteshmaagadh.quotesapp.data.callbacks.FragmentCallbacks
import com.riteshmaagadh.quotesapp.data.db.Pref
import com.riteshmaagadh.quotesapp.data.notifications.AlarmUtils
import com.riteshmaagadh.quotesapp.databinding.ActivityMainBinding
import com.riteshmaagadh.quotesapp.ui.categories.CategoriesActivity
import com.riteshmaagadh.quotesapp.ui.home.HomeFragment
import com.riteshmaagadh.quotesapp.ui.likedquotes.LikedQuotesActivity
import com.riteshmaagadh.quotesapp.ui.reminders.RemindersFragment
import com.riteshmaagadh.quotesapp.ui.utils.Utils


class MainActivity : AppCompatActivity(), FragmentCallbacks {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var paletteBuilder: Palette.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!Pref.getPrefBoolean(this, Pref.IS_NOTIFICATION_ENABLED)) {
            val alarmUtils = AlarmUtils(this)
            alarmUtils.initRepeatingAlarm()
        }

        val navView: NavigationView = binding.navView
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        navView.setupWithNavController(navController)

        switchScreen(HomeFragment())

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> switchScreen(HomeFragment())
                R.id.nav_gallery -> {
                    startActivity(Intent(this, LikedQuotesActivity::class.java))
                    Utils.overrideEnterAnimation(this)
                    binding.drawerLayout.closeDrawer(Gravity.RIGHT)
                    false
                }
                R.id.nav_slideshow -> {
                    startActivity(Intent(this, CategoriesActivity::class.java))
                    Utils.overrideEnterAnimation(this)
                    binding.drawerLayout.closeDrawer(Gravity.RIGHT)
                    false
                }
//                R.id.nav_reminder -> switchScreen(RemindersFragment())
//                R.id.nav_widgets -> switchScreen(WidgetsFragment())
                else -> {
                    false
                }
            }
        }

        navView.menu.getItem(0).isChecked = true
    }

    private fun switchScreen(fragment: Fragment): Boolean {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, fragment)
            .commitAllowingStateLoss()
        binding.drawerLayout.closeDrawer(Gravity.RIGHT)
        return true
    }

    override fun onMenuIconClicked() {
        if (!binding.drawerLayout.isOpen) {
            binding.drawerLayout.openDrawer(Gravity.RIGHT)
        }
    }
}