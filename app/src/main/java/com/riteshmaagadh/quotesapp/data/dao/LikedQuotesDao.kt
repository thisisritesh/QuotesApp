package com.riteshmaagadh.quotesapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.riteshmaagadh.quotesapp.data.models.Quote

@Dao
interface LikedQuotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikedQuote(quote: Quote)

    @Query("SELECT * FROM quote_table")
    fun getAllLikedQuotesLiveData() : LiveData<List<Quote>>

    @Query("SELECT * FROM quote_table")
    fun getAllLikedQuotes() : List<Quote>

    @Delete
    suspend fun deleteQuote(quote: Quote)

}