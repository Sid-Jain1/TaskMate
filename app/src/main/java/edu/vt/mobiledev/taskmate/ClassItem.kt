package edu.vt.mobiledev.taskmate

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "class_item")
data class ClassItem(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val description: String = "",
    val lastUpdated: Date = Date()
) {
    @Ignore
    var tasks: List<Task> = emptyList()


    val completedCount get() = tasks.count { it.kind == TaskKind.COMPLETED }
    val totalCount get() = tasks.size


}
