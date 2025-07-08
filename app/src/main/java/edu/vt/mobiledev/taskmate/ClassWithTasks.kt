package edu.vt.mobiledev.taskmate

import androidx.room.Embedded
import androidx.room.Relation
//Siddharth Jain
//PID: siddharthjain
data class ClassWithTasks(
    @Embedded val classItem: ClassItem,
    @Relation(
        parentColumn = "id",
        entityColumn = "classId"
    )
    val tasks: List<Task>
)
