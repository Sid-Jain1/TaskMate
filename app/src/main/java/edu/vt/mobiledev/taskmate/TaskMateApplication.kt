package edu.vt.mobiledev.taskmate

import android.app.Application

class TaskMateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TaskRepository.initialize(this)
    }
}
