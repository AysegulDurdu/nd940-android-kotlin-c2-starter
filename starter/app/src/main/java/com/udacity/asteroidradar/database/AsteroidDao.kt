package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM AsteroidEntitiyTable order by closeApproachDate")
    fun getAsteroids(): LiveData<List<AsteroidEntitiyTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroidEntitiyTable: AsteroidEntitiyTable)

    @Query("select * from AsteroidEntitiyTable where closeApproachDate between :startDate and :endDate order by closeApproachDate")
    fun getByFilterDate(startDate: String, endDate: String): LiveData<List<AsteroidEntitiyTable>>

    @Query("SELECT * FROM AsteroidEntitiyTable WHERE closeApproachDate = :startDate order by closeApproachDate")
    fun getWithDate(startDate: String): LiveData<List<AsteroidEntitiyTable>>

    @Query("DELETE FROM AsteroidEntitiyTable WHERE closeApproachDate = :date")
    fun deleteAsteroidWithDate(date: String)

}