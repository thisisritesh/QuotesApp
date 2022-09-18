package com.riteshmaagadh.quotesapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.riteshmaagadh.quotesapp.data.dao.LikedQuotesDao
import com.riteshmaagadh.quotesapp.data.models.Quote

@Database(entities = [Quote::class], version = 1)
abstract class LikedQuotesDb : RoomDatabase() {

    abstract fun likedQuotesDao() : LikedQuotesDao

    companion object{
        private var INSTANCE: LikedQuotesDb? = null

        @Synchronized
        fun getInstance(ctx: Context) : LikedQuotesDb {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(ctx.applicationContext, LikedQuotesDb::class.java, "liked_quotes_db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }

}