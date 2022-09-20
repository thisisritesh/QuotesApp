package com.riteshmaagadh.quotesapp.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.riteshmaagadh.quotesapp.R
import com.riteshmaagadh.quotesapp.data.adapters.MainSliderAdapter
import com.riteshmaagadh.quotesapp.data.callbacks.FragmentCallbacks
import com.riteshmaagadh.quotesapp.data.db.LikedQuotesDb
import com.riteshmaagadh.quotesapp.data.db.Pref
import com.riteshmaagadh.quotesapp.data.models.Quote
import com.riteshmaagadh.quotesapp.databinding.FragmentHomeBinding
import com.riteshmaagadh.quotesapp.ui.networkerror.NoInternetActivity
import com.riteshmaagadh.quotesapp.ui.networkerror.ServerErrorActivity
import com.riteshmaagadh.quotesapp.ui.share.ShareBottomSheet
import com.riteshmaagadh.quotesapp.ui.themes.ThemesActivity
import com.riteshmaagadh.quotesapp.ui.utils.AnimUtils
import com.riteshmaagadh.quotesapp.ui.utils.Constants
import com.riteshmaagadh.quotesapp.ui.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var fragmentCallbacks: FragmentCallbacks
    private lateinit var mQuote: Quote
    private var isLiked = false
    private var likedQuotesList: MutableList<Quote> = mutableListOf()
    private lateinit var mTTS: TextToSpeech
    private lateinit var mainSliderAdapter: MainSliderAdapter
    private var list: MutableList<Quote> = mutableListOf()

    private var collectionRef = FirebaseFirestore.getInstance().collection("home_quotes")
    private var lastResult: DocumentSnapshot? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            fragmentCallbacks = context as FragmentCallbacks
        } catch (e: Exception){
            print("$context must implement fragmentcallbacks")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.VISIBLE

        init()

        binding.hamburgerMenu.setOnClickListener {
            fragmentCallbacks.onMenuIconClicked()
        }


        mainSliderAdapter = MainSliderAdapter(list, object : MainSliderAdapter.AdapterCallbacks{
            override fun onQuoteLiked() {
                likeQuote(mQuote)
            }
        })

        if (Utils.isNetworkAvailable(requireContext())){
            fetchQuotes()
        } else {
            startActivity(Intent(requireContext(), NoInternetActivity::class.java))
        }

        binding.mainViewPager.adapter = mainSliderAdapter

        binding.mainViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
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
                mTTS.speak(mQuote.quote,TextToSpeech.QUEUE_FLUSH, null)
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
            ShareBottomSheet().show(childFragmentManager, "SHARE_BOTTOM_SHEET")
        }

        binding.themesIcon.setOnClickListener {
            val intent = Intent(requireContext(), ThemesActivity::class.java)
            intent.putExtra(Constants.IS_FIRST_TIME_USER, false)
            startActivity(intent)
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
                    mainSliderAdapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                }
            })
            .addOnFailureListener {
                startActivity(Intent(requireContext(), ServerErrorActivity::class.java))
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
            LikedQuotesDb.getInstance(requireContext())
                .likedQuotesDao()
                .insertLikedQuote(quote)
        }
        AnimUtils.bounceView(binding.likeIcon, requireContext())
        binding.likeIcon.setImageResource(R.drawable.ic_round_favorite_24)
        isLiked = true
    }

    private fun unlikeQuote(mQuote: Quote) {
        lifecycleScope.launch(Dispatchers.IO){
            LikedQuotesDb.getInstance(requireContext())
                .likedQuotesDao()
                .deleteQuote(mQuote)
            withContext(Dispatchers.Main){
                AnimUtils.bounceView(binding.likeIcon, requireContext())
                binding.likeIcon.setImageResource(R.drawable.ic_round_favorite_border_24)
                isLiked = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainSliderAdapter.notifyDataSetChanged()
        Glide.with(requireContext())
            .load(Pref.getPrefString(requireContext(), Pref.SELECTED_THEME_URL))
            .into(binding.backgroundImage)
    }

    private fun init(){

        lifecycleScope.launch(Dispatchers.IO){
            val list: LiveData<List<Quote>> = LikedQuotesDb.getInstance(requireContext())
                .likedQuotesDao()
                .getAllLikedQuotesLiveData()
            withContext(Dispatchers.Main){
                list.observe(requireActivity()){
                    likedQuotesList.clear()
                    likedQuotesList.addAll(it)
                }
            }
        }

        mTTS = TextToSpeech(requireContext()) { status ->
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


    override fun onDestroyView() {
        super.onDestroyView()
        mTTS.stop()
        mTTS.shutdown()
        _binding = null
    }
}