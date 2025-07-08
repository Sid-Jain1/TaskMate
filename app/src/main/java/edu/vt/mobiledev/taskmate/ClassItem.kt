package edu.vt.mobiledev.taskmate

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID
//Siddharth Jain
//PID: siddharthjain
//Class Information
@Entity(tableName = "class_item")
data class ClassItem(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val description: String = "",
    val lastUpdated: Date = Date()
) {
    //gets list of tasks
    @Ignore
    var tasks: List<Task> = emptyList()

    //gets complete count
    val completedCount get() = tasks.count { it.kind == TaskKind.COMPLETED }
    //gets total count
    val totalCount get() = tasks.size


}
