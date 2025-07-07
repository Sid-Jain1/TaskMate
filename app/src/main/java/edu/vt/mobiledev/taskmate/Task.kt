package edu.vt.mobiledev.taskmate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

/**
 * Room Entity that represents a Task.
 * Each Task is associated with a Class via classId.
 */
@Entity(tableName = "task")
data class Task(
    @PrimaryKey @ColumnInfo(name = "taskId")
    // Unique ID for the task, auto-generated using UUID
    val id: UUID = UUID.randomUUID(),

    // Title or name of the task
    val title: String = "",
    // Optional due date for the task (can be nullable)
    val dueDate: Date? = null,
    // Type or status of the task (e.g., ASSIGNED, COMPLETED)
    val kind: TaskKind,
    //Key to be associated with the specific class
    val classId: UUID
)

/**
 * Enum representing the kind or status of a task.
 */
enum class TaskKind {
    ASSIGNED,   // Task is assigned but not completed
    COMPLETED,  // Task has been finished
    LATE,       // Task is overdue
    OPTIONAL    // Task is not required
}