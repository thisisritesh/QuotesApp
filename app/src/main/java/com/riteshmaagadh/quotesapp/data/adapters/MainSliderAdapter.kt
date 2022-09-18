package com.riteshmaagadh.quotesapp.data.adapters

import android.graphics.drawable.AnimatedVectorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.riteshmaagadh.quotesapp.R
import com.riteshmaagadh.quotesapp.data.callbacks.DoubleClickListener
import com.riteshmaagadh.quotesapp.data.db.Pref
import com.riteshmaagadh.quotesapp.data.models.Quote
import com.riteshmaagadh.quotesapp.databinding.SliderItemLayoutBinding


class MainSliderAdapter(private val list: MutableList<Quote>, private val callbacks: AdapterCallbacks)
    : RecyclerView.Adapter<MainSliderAdapter.MainSliderViewHolder>() {

    interface AdapterCallbacks {
        fun onQuoteLiked()
    }

    inner class MainSliderViewHolder(binding: SliderItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) {
        val quoteTv = binding.quoteTextView
        val writerTv = binding.writerTextView
        private lateinit var avd: AnimatedVectorDrawableCompat
        private lateinit var avd2: AnimatedVectorDrawable

        init {
            binding.root.setOnClickListener(object : DoubleClickListener(){
                override fun onDoubleClick(v: View?) {
                    callbacks.onQuoteLiked()
                    val drawable = binding.likeAnimBtn.drawable
                    binding.likeAnimBtn.alpha = 0.70F
                    if (drawable is AnimatedVectorDrawableCompat){
                        avd = drawable as AnimatedVectorDrawableCompat
                        avd.start()
                    } else if (drawable is AnimatedVectorDrawable){
                        avd2 = drawable as AnimatedVectorDrawable
                        avd2.start()
                    }
                }

            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainSliderViewHolder
        = MainSliderViewHolder(
        SliderItemLayoutBinding
        .inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: MainSliderViewHolder, position: Int) {
        holder.quoteTv.text = list[position].quote
        holder.writerTv.text = list[position].writer

        TextViewCompat.setTextAppearance(holder.quoteTv, Pref.getPrefInt(holder.quoteTv.context, Pref.SELECTED_QUOTE_FONT_ID))
        TextViewCompat.setTextAppearance(holder.writerTv, Pref.getPrefInt(holder.quoteTv.context, Pref.SELECTED_WRITER_FONT_ID))

    }

    override fun getItemCount(): Int = list.size

}