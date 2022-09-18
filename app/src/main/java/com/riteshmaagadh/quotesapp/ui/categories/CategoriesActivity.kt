package com.riteshmaagadh.quotesapp.ui.categories

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.riteshmaagadh.quotesapp.R
import com.riteshmaagadh.quotesapp.data.adapters.CategoriesAdapter
import com.riteshmaagadh.quotesapp.data.models.Category
import com.riteshmaagadh.quotesapp.databinding.ActivityCategoriesBinding

class CategoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list: List<Category> = listOf(
            Category("1","Category 1", R.mipmap.placeholder_1),
            Category("1","Category 2", R.mipmap.placeholder_1),
            Category("1","Category 3", R.mipmap.placeholder_1),
            Category("1","Category 4", R.mipmap.placeholder_1),
            Category("1","Category 5", R.mipmap.placeholder_1),
            Category("1","Category 6", R.mipmap.placeholder_1),
            Category("1","Category 7", R.mipmap.placeholder_1),
            Category("1","Category 8", R.mipmap.placeholder_1),
            Category("1","Category 9", R.mipmap.placeholder_1),
            Category("1","Category 10", R.mipmap.placeholder_1)
        )

        binding.categoryRecyclerView.adapter = CategoriesAdapter(list, this)

        binding.backArrow.setOnClickListener {
            finish()
        }


    }
}