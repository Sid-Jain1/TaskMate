package edu.vt.mobiledev.taskmate

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * DAO for accessing and managing the task data and class data
 */
@Dao
interface TaskDao {

    @Transaction
    @Query("SELECT * FROM class_item ORDER BY lastUpdated DESC")
    fun getClasses(): Flow<List<ClassWithTasks>>

    /**
     * Retrieves a single ClassItem by its ID.
     */
    @Query("SELECT * FROM class_item WHERE id = (:id)")
    suspend fun internalGetClass(id: UUID): ClassItem

    /**
     * Retrieves all tasks associated with a particular class ID.
     */
    @Query("SELECT * FROM task WHERE classId = (:classId)")
    suspend fun internalGetTasksForClass(classId: UUID): List<Task>
    /**
     * Retrieves a class and its tasks as a ClassItem.
     */
    @Transaction
    suspend fun getClassAndTasks(id: UUID): ClassItem {
        return internalGetClass(id).apply {
            tasks = internalGetTasksForClass(id).toMutableList()
        }
    }


    /**
     * Updates a ClassItem in the database.
     */
    @Update
    suspend fun internalUpdateClass(classItem: ClassItem)
    /**
     * Inserts a single Task into the database.
     */
    @Insert
    suspend fun internalInsertTask(task: Task)
    /**
     * Deletes all tasks associated with the given class ID.
     */
    @Query("DELETE FROM task WHERE classId = (:id)")
    suspend fun internalDeleteTasksFromClass(id: UUID)
    /**
     * Replaces the current tasks of a class.
     */
    @Transaction
    suspend fun updateClassAndTasks(classItem: ClassItem) {
        internalDeleteTasksFromClass(classItem.id)
        classItem.tasks.forEach { internalInsertTask(it) }
        internalUpdateClass(classItem)
    }
    /**
     * Inserts a ClassItem into the database.
     */
    @Insert
    suspend fun internalInsertClass(classItem: ClassItem)
    /**
     * Inserts a ClassItem and all associated tasks in a transaction.
     */
    @Transaction
    suspend fun insertClassAndTasks(classItem: ClassItem) {
        internalInsertClass(classItem)
        classItem.tasks.forEach { internalInsertTask(it) }
    }
    /**
     * Deletes a ClassItem from the database.
     */
    @Delete
    suspend fun internalDeleteClass(classItem: ClassItem)
    /**
     * Deletes a class and all its associated tasks in a single transaction.
     */
    @Transaction
    suspend fun deleteClassAndTasks(classItem: ClassItem) {
        internalDeleteTasksFromClass(classItem.id)
        internalDeleteClass(classItem)
    }
}
