package com.riteshmaagadh.quotesapp.ui.themes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.TextViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import com.riteshmaagadh.quotesapp.MainActivity
import com.riteshmaagadh.quotesapp.R
import com.riteshmaagadh.quotesapp.data.adapters.FontsAdapter
import com.riteshmaagadh.quotesapp.data.adapters.ThemesBgAdapter
import com.riteshmaagadh.quotesapp.data.db.Pref
import com.riteshmaagadh.quotesapp.data.models.Category
import com.riteshmaagadh.quotesapp.data.models.Font
import com.riteshmaagadh.quotesapp.data.models.Quote
import com.riteshmaagadh.quotesapp.data.models.Theme
import com.riteshmaagadh.quotesapp.databinding.ActivityThemesBinding
import com.riteshmaagadh.quotesapp.ui.networkerror.NoInternetActivity
import com.riteshmaagadh.quotesapp.ui.networkerror.ServerErrorActivity
import com.riteshmaagadh.quotesapp.ui.utils.Constants
import com.riteshmaagadh.quotesapp.ui.utils.HorizontalMarginItemDecoration
import com.riteshmaagadh.quotesapp.ui.utils.Utils
import java.lang.Math.abs

class ThemesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThemesBinding
    private lateinit var themesAdapter: ThemesBgAdapter
    private lateinit var mTheme: Theme
    private lateinit var mFont: Font
    private lateinit var fontsAdapter: FontsAdapter
    private var isFirstTimeUser = false
    private var themesList: MutableList<Theme> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE

        isFirstTimeUser = intent.extras?.getBoolean(Constants.IS_FIRST_TIME_USER)!!

        if (isFirstTimeUser){
            binding.backArrow.visibility = View.GONE
            binding.nextBtn.visibility = View.VISIBLE
            binding.explanationTv.visibility = View.VISIBLE
            binding.textView.text = "SELECT THEME"
        } else {
            binding.backArrow.visibility = View.VISIBLE
            binding.nextBtn.visibility = View.GONE
            binding.explanationTv.visibility = View.GONE
            binding.textView.text = "THEMES"
        }

        binding.nextBtn.setOnClickListener {
            Pref.putPref(this, Pref.SELECTED_QUOTE_FONT_ID, mFont.quoteFontId)
            Pref.putPref(this, Pref.SELECTED_WRITER_FONT_ID, mFont.writerFontId)
            Pref.putPref(this, Pref.SELECTED_THEME_URL, mTheme.imageUrl)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        initViewPager()

        binding.backArrow.setOnClickListener {
            finish()
        }

//        val themesList: List<Theme> = listOf(
//            Theme("https://picsum.photos/seed/picsum/200/300", 1),
//            Theme("https://picsum.photos/seed/picsum/200/300", 2),
//            Theme("https://picsum.photos/seed/picsum/200/300", 3),
//            Theme("https://picsum.photos/seed/picsum/200/300", 4),
//            Theme("https://picsum.photos/seed/picsum/200/300", 5)
//        )

        themesAdapter = ThemesBgAdapter(themesList, object : ThemesBgAdapter.AdapterCallbacks{
            override fun onThemeSelected() {
                themesAdapter.selectTheme(mTheme.id)
            }
        }, this)

        if (Utils.isNetworkAvailable(this)){
            FirebaseFirestore.getInstance()
                .collection("themes")
                .get()
                .addOnSuccessListener {
                    themesList.addAll(it.toObjects(Theme::class.java))
                    themesAdapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                }
                .addOnFailureListener {
                    startActivity(Intent(this, ServerErrorActivity::class.java))
                }
        } else {
            startActivity(Intent(this, NoInternetActivity::class.java))
        }

        binding.themesBgRecyclerView.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mTheme = themesList[position]
            }
        })

        binding.themesBgRecyclerView.adapter = themesAdapter

        /*
        Anton
        Pacifico
        Secular One
        Caveat
        Satisfy
        IBM Plex Mono
        Signika
        Acme
        Permanent Marker
        Freckle Face
        Righteous
        Cinzel Decorative
        Fredericka the Great
         */

        val fontsList: List<Font> = listOf(
            Font("Bebas Neue",R.style.QuoteStyle_BebasNeue, R.style.WriterStyle_BebasNeue,1),
            Font("Pacifico",R.style.QuoteStyle_Pacifico, R.style.WriterStyle_Pacifico,2),
            Font("Secular One",R.style.QuoteStyle_SecularOne, R.style.WriterStyle_SecularOne, 3),
            Font("Caveat",R.style.QuoteStyle_Caveat, R.style.WriterStyle_Caveat,4),
            Font("Satisfy",R.style.QuoteStyle_Satisfy, R.style.WriterStyle_Satisfy,5),
            Font("IBM Plex Mono",R.style.QuoteStyle_IBMPlexMono, R.style.WriterStyle_IBMPlexMono,6),
            Font("Signika",R.style.QuoteStyle_Signika, R.style.WriterStyle_Signika,7),
            Font("Acme",R.style.QuoteStyle_Acme, R.style.WriterStyle_Acme,8),
            Font("Permanent Marker",R.style.QuoteStyle_PermanentMarker, R.style.WriterStyle_PermanentMarker,9),
            Font("Freckle Face",R.style.QuoteStyle_FreckleFace, R.style.WriterStyle_FreckleFace,10),
            Font("Righteous",R.style.QuoteStyle_Righteous, R.style.WriterStyle_Righteous,11),
            Font("Cinzel Decorative",R.style.QuoteStyle_CinzelDecorative, R.style.WriterStyle_CinzelDecorative,12),
            Font("Fredericka the Great",R.style.QuoteStyle_FrederickaTheGreat, R.style.WriterStyle_FrederickaTheGreat,13)
        )

        fontsAdapter = FontsAdapter(fontsList, this, object : FontsAdapter.AdapterCallbacks{
            override fun onFontChanged(font: Font) {
                TextViewCompat.setTextAppearance(binding.nextBtn, R.style.Quote_Button_Enabled)
                binding.nextBtn.isClickable = true
                mFont = font
                fontsAdapter.selectFont(font.id)
                themesAdapter.setFont(font.quoteFontId)
            }
        })

        binding.fontsRecyclerView.adapter = fontsAdapter


    }

    private fun initViewPager(){
        binding.themesBgRecyclerView.clipToPadding = true
        binding.themesBgRecyclerView.clipChildren = false
        binding.themesBgRecyclerView.offscreenPageLimit = 1


        // View Pager -- start
        val nextItemVisiblePx = resources.getDimension(R.dimen.next_item_visible_px)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.current_item_horizontal_margin_px)

        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx

        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = - pageTranslationX * position
            page.scaleY = 1 - (0.15f * kotlin.math.abs(position))
            page.alpha = 0.25f + (1 - kotlin.math.abs(position))
        }
        binding.themesBgRecyclerView.setPageTransformer(pageTransformer)

        val itemDecoration = HorizontalMarginItemDecoration(
            this,
            R.dimen.next_item_visible_px
        )
        binding.themesBgRecyclerView.addItemDecoration(itemDecoration)

    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun finish() {
        super.finish()
        if (this::mFont.isInitialized && this::mTheme.isInitialized){
            Pref.putPref(this, Pref.SELECTED_QUOTE_FONT_ID, mFont.quoteFontId)
            Pref.putPref(this, Pref.SELECTED_WRITER_FONT_ID, mFont.writerFontId)
            Pref.putPref(this, Pref.SELECTED_THEME_URL, mTheme.imageUrl)
        }
    }

}