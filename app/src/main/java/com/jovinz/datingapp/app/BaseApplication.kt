package com.jovinz.datingapp.app

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.facebook.stetho.Stetho
import com.jovinz.datingapp.common.db.AppDatabase
import com.jovinz.datingapp.common.preferences.UserPrefs
import com.jovinz.datingapp.utils.Constants

class BaseApplication : Application() {
    init {
        instance = this
    }

    companion object {
        var instance: BaseApplication? = null

        private fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        var appDatabase: AppDatabase? = null

        private fun createDatabase() {
            appDatabase = Room.databaseBuilder(
                applicationContext(),
                AppDatabase::class.java, Constants.DB_NAME
            ).build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        UserPrefs.init(applicationContext)
        createDatabase()
        Stetho.initializeWithDefaults(this)
    }
}