package com.jovinz.datingapp.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jovinz.datingapp.home.data.model.ProfilesResponseItem
import com.jovinz.datingapp.utils.Constants


@Database(
    entities = [ProfilesResponseItem::class],
    version = Constants.DB_VERSION,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun profilesDao(): ProfilesDao
}