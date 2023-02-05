package com.kmyiu.simpletodoapp

import android.app.Application
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
        const val dbName = "todoTask_database"

        val applicationScope = CoroutineScope(SupervisorJob())
        val database by lazy { AppDatabase.getDatabase(appContext, applicationScope) }
    }
}