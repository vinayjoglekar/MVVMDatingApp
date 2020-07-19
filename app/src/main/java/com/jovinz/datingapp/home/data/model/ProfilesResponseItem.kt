package com.jovinz.datingapp.home.data.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Profile")

data class ProfilesResponseItem(

    @ColumnInfo(name = "city") @SerializedName("city") var city: String?,

    @ColumnInfo(name = "first_name") @SerializedName("first_name") var firstName: String?,

    @ColumnInfo(name = "gender") @SerializedName("gender") var gender: String?,

    @ColumnInfo(name = "last_name") @SerializedName("last_name") var lastName: String?,

    @ColumnInfo(name = "picture") @SerializedName("picture") var picture: String?,

    @ColumnInfo(name = "latitude") @SerializedName("latitude") var latitude: Double?,

    @ColumnInfo(name = "longtitude") @SerializedName("longtitude") var longtitude: Double?,

    @PrimaryKey @ColumnInfo(name = "email") @SerializedName("email") var email: String
)