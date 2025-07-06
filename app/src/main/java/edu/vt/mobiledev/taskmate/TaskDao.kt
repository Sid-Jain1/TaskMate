package edu.vt.mobiledev.taskmate

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TaskDao {

    @Transaction
    @Query("SELECT * FROM class_item ORDER BY lastUpdated DESC")
    fun getClasses(): Flow<List<ClassWithTasks>>


    @Query("SELECT * FROM class_item WHERE id = (:id)")
    suspend fun internalGetClass(id: UUID): ClassItem

    @Query("SELECT * FROM task WHERE classId = (:classId)")
    suspend fun internalGetTasksForClass(classId: UUID): List<Task>

    @Transaction
    suspend fun getClassAndTasks(id: UUID): ClassItem {
        return internalGetClass(id).apply {
            tasks = internalGetTasksForClass(id).toMutableList()
        }
    }


    @Update
    suspend fun internalUpdateClass(classItem: ClassItem)

    @Insert
    suspend fun internalInsertTask(task: Task)

    @Query("DELETE FROM task WHERE classId = (:id)")
    suspend fun internalDeleteTasksFromClass(id: UUID)

    @Transaction
    suspend fun updateClassAndTasks(classItem: ClassItem) {
        internalDeleteTasksFromClass(classItem.id)
        classItem.tasks.forEach { internalInsertTask(it) }
        internalUpdateClass(classItem)
    }

    @Insert
    suspend fun internalInsertClass(classItem: ClassItem)

    @Transaction
    suspend fun insertClassAndTasks(classItem: ClassItem) {
        internalInsertClass(classItem)
        classItem.tasks.forEach { internalInsertTask(it) }
    }

    @Delete
    suspend fun internalDeleteClass(classItem: ClassItem)

    @Transaction
    suspend fun deleteClassAndTasks(classItem: ClassItem) {
        internalDeleteTasksFromClass(classItem.id)
        internalDeleteClass(classItem)
    }
}
