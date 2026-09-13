package com.vaulto.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "collections")
data class Collection(
    @PrimaryKey(autoGenerate = true)
    val id: Long =0,
    val name: String,
    val icon: String ="📁",
    val createdAt: Long=System.currentTimeMillis()
)