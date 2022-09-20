package com.riteshmaagadh.quotesapp.data.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.riteshmaagadh.quotesapp.R
import com.riteshmaagadh.quotesapp.data.models.Category
import com.riteshmaagadh.quotesapp.databinding.CategoryItemBinding
import com.riteshmaagadh.quotesapp.ui.categoryexplore.CategoryExploreActivity
import com.riteshmaagadh.quotesapp.ui.utils.Constants
import com.riteshmaagadh.quotesapp.ui.utils.Utils

class CategoriesAdapter(private val list: List<Category>, private val context: Activity) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    inner class CategoriesViewHolder(binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val bgImage = binding.bgImage
        val titleTv = binding.categoryTitleText
        init {
            binding.root.setOnClickListener{
                val intent = Intent(context, CategoryExploreActivity::class.java)
                intent.putExtra(Constants.CATEGORY_ID, list[adapterPosition].id)
                intent.putExtra(Constants.CATEGORY_TITLE, list[adapterPosition].category_title)
                context.startActivity(intent)
                Utils.overrideEnterAnimation(context)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder
        = CategoriesViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        Glide.with(holder.bgImage.context)
            .load(list[position].image_url)
            .into(holder.bgImage)

        holder.titleTv.text = list[position].category_title
    }

    override fun getItemCount(): Int = list.size

}