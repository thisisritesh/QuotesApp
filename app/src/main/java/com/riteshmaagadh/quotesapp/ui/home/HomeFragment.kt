package com.riteshmaagadh.quotesapp.ui.home

import android.content.Context
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
import com.riteshmaagadh.quotesapp.R
import com.riteshmaagadh.quotesapp.data.adapters.MainSliderAdapter
import com.riteshmaagadh.quotesapp.data.callbacks.FragmentCallbacks
import com.riteshmaagadh.quotesapp.data.db.LikedQuotesDb
import com.riteshmaagadh.quotesapp.data.models.Quote
import com.riteshmaagadh.quotesapp.databinding.FragmentHomeBinding
import com.riteshmaagadh.quotesapp.ui.share.ShareBottomSheet
import com.riteshmaagadh.quotesapp.ui.utils.AnimUtils
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
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        binding.hamburgerMenu.setOnClickListener {
            fragmentCallbacks.onMenuIconClicked()
        }


        val list: MutableList<Quote> = mutableListOf(
            Quote("When you have a dream, you've got to grab it and never let go.","— Carol Burnett","1"),
            Quote("Nothing is impossible. The word itself says 'I'm possible!","— Audrey Hepburn","2"),
            Quote("There is nothing impossible to they who will try.","— Alexander the Great","3"),
            Quote("The bad news is time flies. The good news is you're the pilot.","— Michael Altshuler","4"),
            Quote("Life has got all those twists and turns. You've got to hold on tight and off you go.","— Nicole Kidman","5"),
            Quote("Keep your face always toward the sunshine, and shadows will fall behind you.","— Walt Whitman","6")
        )

        binding.mainViewPager.adapter = MainSliderAdapter(list, object : MainSliderAdapter.AdapterCallbacks{
            override fun onQuoteLiked() {
                likeQuote(mQuote)
            }
        })

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