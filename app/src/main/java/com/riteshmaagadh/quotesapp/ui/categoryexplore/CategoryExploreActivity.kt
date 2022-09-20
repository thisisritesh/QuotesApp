package com.riteshmaagadh.quotesapp.ui.categoryexplore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*
import com.riteshmaagadh.quotesapp.R
import com.riteshmaagadh.quotesapp.data.adapters.MainSliderAdapter
import com.riteshmaagadh.quotesapp.data.callbacks.FragmentCallbacks
import com.riteshmaagadh.quotesapp.data.db.LikedQuotesDb
import com.riteshmaagadh.quotesapp.data.models.Quote
import com.riteshmaagadh.quotesapp.data.models.Theme
import com.riteshmaagadh.quotesapp.databinding.ActivityCategoryExploreBinding
import com.riteshmaagadh.quotesapp.databinding.FragmentHomeBinding
import com.riteshmaagadh.quotesapp.ui.networkerror.NoInternetActivity
import com.riteshmaagadh.quotesapp.ui.networkerror.ServerErrorActivity
import com.riteshmaagadh.quotesapp.ui.share.ShareBottomSheet
import com.riteshmaagadh.quotesapp.ui.utils.AnimUtils
import com.riteshmaagadh.quotesapp.ui.utils.Constants
import com.riteshmaagadh.quotesapp.ui.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CategoryExploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryExploreBinding
    private lateinit var mQuote: Quote
    private var isLiked = false
    private var likedQuotesList: MutableList<Quote> = mutableListOf()
    private lateinit var mTTS: TextToSpeech

    private lateinit var collectionRef: CollectionReference
    private var lastResult: DocumentSnapshot? = null
    private var list: MutableList<Quote> = mutableListOf()
    private lateinit var mAdapter: MainSliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)


        init()

        binding.hamburgerMenu.setOnClickListener {
            finish()
        }


//        val list: MutableList<Quote> = mutableListOf(
//            Quote("When you have a dream, you've got to grab it and never let go.","— Carol Burnett","1"),
//            Quote("Nothing is impossible. The word itself says 'I'm possible!","— Audrey Hepburn","2"),
//            Quote("There is nothing impossible to they who will try.","— Alexander the Great","3"),
//            Quote("The bad news is time flies. The good news is you're the pilot.","— Michael Altshuler","4"),
//            Quote("Life has got all those twists and turns. You've got to hold on tight and off you go.","— Nicole Kidman","5"),
//            Quote("Keep your face always toward the sunshine, and shadows will fall behind you.","— Walt Whitman","6")
//        )

        val id = intent.extras?.getString(Constants.CATEGORY_ID)!!
        val title = intent.extras?.getString(Constants.CATEGORY_TITLE)!!

        binding.categoryTitleTv.text = title

        collectionRef = FirebaseFirestore.getInstance().collection("category_$id")

        mAdapter = MainSliderAdapter(list, object : MainSliderAdapter.AdapterCallbacks{
            override fun onQuoteLiked() {
                likeQuote(mQuote)
            }
        })

        binding.exploreViewPager.adapter = mAdapter

        if (Utils.isNetworkAvailable(this)){
            fetchQuotes()
        } else {
            startActivity(Intent(this, NoInternetActivity::class.java))
        }


        binding.exploreViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (mTTS.isSpeaking){
                    mTTS.stop()
                }
            }
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mQuote = list[position]
                isLiked = if (isAlreadyLiked(mQuote)){
                    binding.likeIcon.setImageResource(R.drawable.ic_round_favorite_24)
                    true
                } else {
                    binding.likeIcon.setImageResource(R.drawable.ic_round_favorite_border_24)
                    false
                }
                if (position == list.size - 2){
                    fetchQuotes()
                }
            }
        })

        binding.playIcon.setOnClickListener {
            if (!mTTS.isSpeaking){
                mTTS.speak(mQuote.quote, TextToSpeech.QUEUE_FLUSH, null)
            }
        }

        binding.likeIcon.setOnClickListener {
            if (isLiked){
                unlikeQuote(mQuote)
            } else {
                likeQuote(mQuote)
            }
        }

        binding.shareIcon.setOnClickListener {
            ShareBottomSheet().show(supportFragmentManager, "SHARE_BOTTOM_SHEET")
        }


    }

    private fun fetchQuotes(){
        val query: Query = if (lastResult == null) {
            collectionRef
                .orderBy("quote")
                .limit(5)
        } else {
            collectionRef
                .orderBy("quote")
                .startAfter(lastResult)
                .limit(5)
        }
        query.get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                if (queryDocumentSnapshots.size() > 0) {
                    lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1]
                    list.addAll(queryDocumentSnapshots.toObjects(Quote::class.java))
                    mAdapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                }
            })
            .addOnFailureListener {
                startActivity(Intent(this, ServerErrorActivity::class.java))
            }
    }



    private fun isAlreadyLiked(quote: Quote) : Boolean {
        for (item in likedQuotesList){
            if (item.id == quote.id){
                return true
            }
        }
        return false
    }

    private fun likeQuote(quote: Quote){
        lifecycleScope.launch(Dispatchers.IO){
            LikedQuotesDb.getInstance(this@CategoryExploreActivity)
                .likedQuotesDao()
                .insertLikedQuote(quote)
        }
        AnimUtils.bounceView(binding.likeIcon, this)
        binding.likeIcon.setImageResource(R.drawable.ic_round_favorite_24)
        isLiked = true
    }

    private fun unlikeQuote(mQuote: Quote) {
        lifecycleScope.launch(Dispatchers.IO){
            LikedQuotesDb.getInstance(this@CategoryExploreActivity)
                .likedQuotesDao()
                .deleteQuote(mQuote)
            withContext(Dispatchers.Main){
                AnimUtils.bounceView(binding.likeIcon, this@CategoryExploreActivity)
                binding.likeIcon.setImageResource(R.drawable.ic_round_favorite_border_24)
                isLiked = false
            }
        }
    }

    private fun init(){

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO){
            val list: LiveData<List<Quote>> = LikedQuotesDb.getInstance(this@CategoryExploreActivity)
                .likedQuotesDao()
                .getAllLikedQuotesLiveData()
            withContext(Dispatchers.Main){
                list.observe(this@CategoryExploreActivity){
                    likedQuotesList.clear()
                    likedQuotesList.addAll(it)
                }
            }
        }

        mTTS = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result: Int = mTTS.setLanguage(Locale.UK)
                if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.e("TTS", "Language not supported")
                }
            } else {
                Log.e("TTS", "Initialization failed")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mTTS.stop()
        mTTS.shutdown()
    }

}