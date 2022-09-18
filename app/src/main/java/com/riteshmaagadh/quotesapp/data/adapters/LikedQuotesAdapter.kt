package com.riteshmaagadh.quotesapp.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riteshmaagadh.quotesapp.data.db.LikedQuotesDb
import com.riteshmaagadh.quotesapp.data.models.Quote
import com.riteshmaagadh.quotesapp.databinding.LikedQuoteItemBinding
import com.riteshmaagadh.quotesapp.ui.utils.AnimUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LikedQuotesAdapter(private val list: List<Quote>) : RecyclerView.Adapter<LikedQuotesAdapter.LikedQuotesViewHolder>() {


    inner class LikedQuotesViewHolder(binding: LikedQuoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val quoteTv = binding.likedQuoteText
        val writerTv = binding.writerText
        val likeIcon = binding.likeIcon
        init {
            likeIcon.setOnClickListener {
                AnimUtils.bounceView(it, it.context)
                GlobalScope.launch(Dispatchers.IO){
                    LikedQuotesDb.getInstance(it.context)
                        .likedQuotesDao()
                        .deleteQuote(list[adapterPosition])
                    withContext(Dispatchers.Main){
                        list.drop(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedQuotesViewHolder
        = LikedQuotesViewHolder(LikedQuoteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: LikedQuotesViewHolder, position: Int) {
        holder.quoteTv.text = list[position].quote
        holder.writerTv.text = list[position].writer
    }

    override fun getItemCount(): Int = list.size

}