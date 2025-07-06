package edu.vt.mobiledev.taskmate

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    version = 1,
    entities = [ClassItem::class, Task::class]
)
@TypeConverters(TaskTypeConverters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}