package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepo
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val database = AsteroidDatabase.getDatabase(application)
    val repo = AsteroidRepo(database)

    init {
        refreshAsteroidService()
        refreshPictureOfDay()
        deleteOldAsteroids()
    }


    fun refreshAsteroidService() {
        viewModelScope.launch {
            repo.refreshAsteroidService()
        }
    }

    fun refreshPictureOfDay() {
        viewModelScope.launch {
            repo.refreshPictureOfDay()
        }
    }

    fun deleteOldAsteroids() {
        viewModelScope.launch {
            repo.deleteOldAsteroids()
        }
    }

    val asteroidFilter = MutableLiveData<FILTER_TYPE>(FILTER_TYPE.WEEK)
    val asteroids = Transformations.switchMap(asteroidFilter) {
        when (it) {
            FILTER_TYPE.TODAY -> repo.today
            FILTER_TYPE.WEEK -> repo.week
            else -> repo.save
        }
    }

    fun updateFilter(filerView: FILTER_TYPE) {
        asteroidFilter.value = filerView
    }

}
enum class FILTER_TYPE { TODAY, WEEK, SAVE }