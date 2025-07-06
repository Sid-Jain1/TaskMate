package edu.vt.mobiledev.taskmate

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.map
import java.util.UUID

private const val DATABASE_NAME = "task-database"

class TaskRepository private constructor(context: Context) {

    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val taskDao = database.taskDao()

    fun getClasses() = taskDao.getClasses().map { classMap ->
        classMap.map { (classItem, tasks) ->
            classItem.apply {
                this.tasks = tasks.toMutableList()
            }
        }
    }



    suspend fun getClass(id: UUID): ClassItem {
        return taskDao.getClassAndTasks(id)
    }

    suspend fun updateClass(classItem: ClassItem) {
        taskDao.updateClassAndTasks(classItem)
    }

    suspend fun addClass(classItem: ClassItem) {
        taskDao.insertClassAndTasks(classItem)
    }

    suspend fun deleteClass(classItem: ClassItem) {
        taskDao.deleteClassAndTasks(classItem)
    }

    companion object {
        private var INSTANCE: TaskRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TaskRepository(context)
            }
        }

        fun get(): TaskRepository {
            return INSTANCE
                ?: throw IllegalStateException("TaskRepository must be initialized")
        }
    }
}
