package com.example.pridenest

import android.app.Application
import com.example.pridenest.data.AppContainer
import com.example.pridenest.data.DefaultAppContainer

class PrideNestApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}