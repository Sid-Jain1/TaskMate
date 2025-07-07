package edu.vt.mobiledev.taskmate

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.map
import java.util.UUID

//constant database name
private const val DATABASE_NAME = "task-database"

/**
 * Repository for managing and accessing data from the task database
 */
class TaskRepository private constructor(context: Context) {

    // Builds the Room database instance
    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val taskDao = database.taskDao()

     //Returns a flow of class items along with their tasks.
    fun getClasses() = taskDao.getClasses().map { classMap ->
        classMap.map { (classItem, tasks) ->
            classItem.apply {
                this.tasks = tasks.toMutableList()
            }
        }
    }


    /**
     * Gets single ClassItem with its associated tasks by ID.
     */
    suspend fun getClass(id: UUID): ClassItem {
        return taskDao.getClassAndTasks(id)
    }
    /**
     * Updates the class and replaces its associated tasks.
     */
    suspend fun updateClass(classItem: ClassItem) {
        taskDao.updateClassAndTasks(classItem)
    }
    /**
     * Inserts a new class and all of its tasks.
     */
    suspend fun addClass(classItem: ClassItem) {
        taskDao.insertClassAndTasks(classItem)
    }
    /**
     * Deletes a class and all of its associated tasks.
     */
    suspend fun deleteClass(classItem: ClassItem) {
        taskDao.deleteClassAndTasks(classItem)
    }

    companion object {
        private var INSTANCE: TaskRepository? = null

        /**
         * Initializes repository
         */
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TaskRepository(context)
            }
        }
        /**
         * Returns the repository instance. Throws error if not initialized.
         */
        fun get(): TaskRepository {
            return INSTANCE
                ?: throw IllegalStateException("TaskRepository must be initialized")
        }
    }
}
