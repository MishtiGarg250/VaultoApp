package com.vaulto.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_items")
data class SavedItem(

    @PrimaryKey(autoGenerate = true)
    val id:Long=0,

    val title: String,
    val url: String? = null,
    val note: String?=null,
    val collectionId: Long?= null,
    val isFavorite: Boolean =false,
    val isArchived: Boolean=false,
    val createdAt: Long = System.currentTimeMillis()
)
