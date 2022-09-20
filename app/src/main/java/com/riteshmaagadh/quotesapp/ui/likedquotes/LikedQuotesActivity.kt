package com.riteshmaagadh.quotesapp.ui.likedquotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.riteshmaagadh.quotesapp.R
import com.riteshmaagadh.quotesapp.data.adapters.LikedQuotesAdapter
import com.riteshmaagadh.quotesapp.data.db.LikedQuotesDb
import com.riteshmaagadh.quotesapp.databinding.ActivityLikedQuotesBinding
import com.riteshmaagadh.quotesapp.ui.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LikedQuotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLikedQuotesBinding
    private lateinit var adapter: LikedQuotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLikedQuotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch(Dispatchers.IO){

            val l = LikedQuotesDb.getInstance(this@LikedQuotesActivity)
                .likedQuotesDao()
                .getAllLikedQuotes()

            adapter = LikedQuotesAdapter(l)
            binding.likedRecyclerView.adapter = adapter

            val list = LikedQuotesDb.getInstance(this@LikedQuotesActivity)
                .likedQuotesDao()
                .getAllLikedQuotesLiveData()

            withContext(Dispatchers.Main){
                list.observe(this@LikedQuotesActivity){
                    adapter.notifyDataSetChanged()
                }
            }

        }


        binding.backArrow.setOnClickListener {
            finish()
        }


    }

    override fun finish() {
        super.finish()
        Utils.overrideExitAnimation(this)
    }


}