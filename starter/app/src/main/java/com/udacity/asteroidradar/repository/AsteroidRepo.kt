package com.udacity.asteroidradar.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.AsteroidApi.RETROFIT_SERVICE
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepo(val database: AsteroidDatabase) {

    private val pictureOfDay = MutableLiveData<PictureOfDay>()

    val yesterdayFormat = getDayDates(-1)
    val todayFormat = getDayDates(0)
    val weekFormat = getDayDates(7)

    val week =
        Transformations.map(database.asteroidDao.getByFilterDate(todayFormat, weekFormat)) {
            it.asDomainModel()
        }

    var today = Transformations.map(database.asteroidDao.getWithDate(todayFormat)) {
        it.asDomainModel()
    }

    var save = Transformations.map(database.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }

    suspend fun deleteOldAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deleteAsteroidWithDate(yesterdayFormat)
        }
    }
    suspend fun refreshAsteroidService() {
        withContext(Dispatchers.IO) {
            try {
                val str = RETROFIT_SERVICE.getAstreoids(getDayDates())
                val jsonObject = JSONObject(str)
                val asteroidList: ArrayList<Asteroid> = parseAsteroidsJsonResult(jsonObject)
                val list = asteroidList.asDatabaseModel()
                list.forEach {
                    database.asteroidDao.insertAll(it)
                }
            } catch (e: java.lang.Exception) {
               e.printStackTrace()
            }
        }
    }

    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                pictureOfDay.postValue(RETROFIT_SERVICE.getPictureOfDay())
            } catch (e: Exception) {
               e.printStackTrace()
            }
        }
    }


}

fun getDayDates(calendarAddAmountDay: Int = 0): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, calendarAddAmountDay)
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    val currentTime = calendar.time
    return dateFormat.format(currentTime)
}
