package com.riteshmaagadh.quotesapp.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riteshmaagadh.quotesapp.R
import com.riteshmaagadh.quotesapp.data.db.Pref
import com.riteshmaagadh.quotesapp.data.models.Theme
import com.riteshmaagadh.quotesapp.databinding.ThemesItemBinding

class ThemesBgAdapter(private val list: List<Theme>, private val callbacks: AdapterCallbacks, private val context: Context) : RecyclerView.Adapter<ThemesBgAdapter.ThemesViewHolder>() {

    private var selectedFontId = Pref.getPrefInt(context, Pref.SELECTED_QUOTE_FONT_ID)
    private var selectedThemeId = Pref.getPrefString(context, Pref.SELECTED_THEME_URL)

    interface AdapterCallbacks {
        fun onThemeSelected()
    }

    inner class ThemesViewHolder(binding: ThemesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val bgImage = binding.bgImage
        val sampleTxt = binding.sampleTxtTv
        val rootView = binding.themesRoot
        init {
            binding.root.setOnClickListener {
                callbacks.onThemeSelected()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemesViewHolder
        = ThemesViewHolder(ThemesItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ThemesViewHolder, position: Int) {
        Glide.with(context)
            .load(list[position].imageUrl)
            .into(holder.bgImage)

        if (selectedFontId != 0){
            TextViewCompat.setTextAppearance(holder.sampleTxt, selectedFontId)
        }

        if (selectedThemeId == list[position].id){
            holder.rootView.setBackgroundResource(R.drawable.selected_theme_item_bg)
        } else {
            holder.rootView.setBackgroundResource(R.drawable.unselected_theme_item_bg)
        }

    }

    override fun getItemCount(): Int = list.size

    public fun setFont(font: Int){
        selectedFontId = font
        notifyDataSetChanged()
    }

    public fun selectTheme(id: String){
        selectedThemeId = id
        notifyDataSetChanged()
    }

}