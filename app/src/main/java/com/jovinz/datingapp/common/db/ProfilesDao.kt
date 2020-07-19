package com.jovinz.datingapp.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jovinz.datingapp.home.data.model.ProfilesResponseItem

@Dao
interface ProfilesDao {

    @Query("SELECT * FROM profile")
    suspend fun getAllProfiles(): List<ProfilesResponseItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profilesResponseItem: ProfilesResponseItem)

    @Query("DELETE FROM profile")
    suspend fun nukeTable()

}