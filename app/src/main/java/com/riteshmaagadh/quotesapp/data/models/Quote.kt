package com.riteshmaagadh.quotesapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "quote_table")
data class Quote(var quote: String, var writer: String, @PrimaryKey(autoGenerate = false) var id: String){
    constructor() : this("","","")
}