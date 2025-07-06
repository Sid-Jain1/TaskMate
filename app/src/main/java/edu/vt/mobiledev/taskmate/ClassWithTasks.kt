package edu.vt.mobiledev.taskmate

import androidx.room.Embedded
import androidx.room.Relation

data class ClassWithTasks(
    @Embedded val classItem: ClassItem,
    @Relation(
        parentColumn = "id",
        entityColumn = "classId"
    )
    val tasks: List<Task>
)
