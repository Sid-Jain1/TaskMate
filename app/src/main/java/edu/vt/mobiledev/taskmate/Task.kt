package edu.vt.mobiledev.taskmate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "task")
data class Task(
    @PrimaryKey @ColumnInfo(name = "taskId")
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val dueDate: Date? = null,
    val kind: TaskKind,
    val classId: UUID
)

enum class TaskKind {
    ASSIGNED,
    COMPLETED,
    LATE,
    OPTIONAL
}