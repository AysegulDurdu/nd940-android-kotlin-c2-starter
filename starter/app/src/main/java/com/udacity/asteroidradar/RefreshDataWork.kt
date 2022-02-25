package com.udacity.asteroidradar

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepo
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "Refresh_Data_Worker"
    }
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepo(database)
        return try {
            repository.refreshAsteroidService()
            repository.refreshPictureOfDay()
            repository.deleteOldAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}