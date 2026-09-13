package com.vaulto.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vaulto.data.entity.Collection
import com.vaulto.data.entity.SavedItem
import com.vaulto.data.local.SavedItemDao

@Database(
    entities = [
        SavedItem::class,
        Collection::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VaultoDatabase : RoomDatabase() {

    abstract fun savedItemDao(): SavedItemDao

    abstract fun collectionDao(): CollectionDao

    companion object {

        @Volatile
        private var INSTANCE: VaultoDatabase? = null

        fun getDatabase(context: Context): VaultoDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VaultoDatabase::class.java,
                    "vaulto_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}