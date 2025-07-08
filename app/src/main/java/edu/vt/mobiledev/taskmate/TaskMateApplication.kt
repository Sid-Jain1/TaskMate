package edu.vt.mobiledev.taskmate

import android.app.Application
//Siddharth Jain
//PID: siddharthjain
/**
 * Creates and initializes the repository
 */
class TaskMateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TaskRepository.initialize(this)
    }
}
