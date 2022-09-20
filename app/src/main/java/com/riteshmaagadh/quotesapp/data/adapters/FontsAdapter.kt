package com.riteshmaagadh.quotesapp.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.riteshmaagadh.quotesapp.R
import com.riteshmaagadh.quotesapp.data.db.Pref
import com.riteshmaagadh.quotesapp.data.models.Font
import com.riteshmaagadh.quotesapp.databinding.FontItemBinding

class FontsAdapter(private val list: List<Font>, private val context: Context, private val callbacks: AdapterCallbacks) : RecyclerView.Adapter<FontsAdapter.FontViewHolder>() {

    interface AdapterCallbacks{
        fun onFontChanged(font: Font)
    }

    inner class FontViewHolder(binding: FontItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val parent = binding.fontParent
        val fontText = binding.fontText
        init {
            binding.root.setOnClickListener{
                callbacks.onFontChanged(list[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder
        = FontViewHolder(FontItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        holder.fontText.text = list[position].fontName
        TextViewCompat.setTextAppearance(holder.fontText, list[position].writerFontId)
        holder.fontText.setTextColor(ContextCompat.getColor(context, R.color.black))

        if (Pref.getPrefInt(context, Pref.SELECTED_QUOTE_FONT_ID) == list[position].quoteFontId){
            holder.parent.setBackgroundResource(R.drawable.selected_theme_item_bg)
        } else {
            holder.parent.setBackgroundResource(R.drawable.unselected_theme_item_bg)
        }

    }

    override fun getItemCount(): Int = list.size

}