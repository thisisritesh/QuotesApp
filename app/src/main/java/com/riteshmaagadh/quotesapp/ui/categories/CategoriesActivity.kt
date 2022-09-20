package com.riteshmaagadh.quotesapp.ui.categories

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import com.riteshmaagadh.quotesapp.R
import com.riteshmaagadh.quotesapp.data.adapters.CategoriesAdapter
import com.riteshmaagadh.quotesapp.data.models.Category
import com.riteshmaagadh.quotesapp.data.models.Quote
import com.riteshmaagadh.quotesapp.databinding.ActivityCategoriesBinding
import com.riteshmaagadh.quotesapp.ui.networkerror.NoInternetActivity
import com.riteshmaagadh.quotesapp.ui.networkerror.ServerErrorActivity
import com.riteshmaagadh.quotesapp.ui.utils.Utils

class CategoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriesBinding
    private val list: MutableList<Category> = mutableListOf()
    private lateinit var mAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE

//        val list: List<Category> = listOf(
//            Category("1","Category 1", "https://picsum.photos/id/237/200/300"),
//            Category("1","Category 2", "https://picsum.photos/id/237/200/300"),
//            Category("1","Category 3", "https://picsum.photos/id/237/200/300"),
//            Category("1","Category 4", "https://picsum.photos/id/237/200/300"),
//            Category("1","Category 5", "https://picsum.photos/id/237/200/300"),
//            Category("1","Category 6", "https://picsum.photos/id/237/200/300"),
//            Category("1","Category 7", "https://picsum.photos/id/237/200/300"),
//            Category("1","Category 8", "https://picsum.photos/id/237/200/300"),
//            Category("1","Category 9", "https://picsum.photos/id/237/200/300"),
//            Category("1","Category 10", "https://picsum.photos/id/237/200/300")
//        )

        mAdapter = CategoriesAdapter(list,this)
        binding.categoryRecyclerView.adapter = mAdapter


        if (Utils.isNetworkAvailable(this)){
            FirebaseFirestore.getInstance()
                .collection("categories")
                .get()
                .addOnSuccessListener {
                    list.addAll(it.toObjects(Category::class.java))
                    mAdapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                }
                .addOnFailureListener {
                    startActivity(Intent(this, ServerErrorActivity::class.java))
                    Utils.overrideEnterAnimation(this)
                }
        } else {
            startActivity(Intent(this, NoInternetActivity::class.java))
            Utils.overrideEnterAnimation(this)
        }

//        for (item in list){
//            FirebaseFirestore.getInstance()
//                .collection("categories")
//                .add(item)
//                .addOnSuccessListener {
//                    FirebaseFirestore.getInstance().collection("categories")
//                        .document(it.id)
//                        .update("id", it.id)
//                        .addOnSuccessListener {
//                            Log.e("ADDING_DATA", "addOnSuccessListener: in update" )
//                        }
//                        .addOnFailureListener {
//                            Log.e("ADDING_DATA", "addOnFailureListener: in update" )
//                        }
//                    Log.e("ADDING_DATA", "addOnSuccessListener: " )
//                }
//                .addOnFailureListener {
//                    Log.e("ADDING_DATA", "addOnFailureListener: ", it )
//                }
//        }

        binding.backArrow.setOnClickListener {
            finish()
        }


    }

    override fun onBackPressed() {
        finish()
    }

    override fun finish() {
        super.finish()
        Utils.overrideExitAnimation(this)
    }


}