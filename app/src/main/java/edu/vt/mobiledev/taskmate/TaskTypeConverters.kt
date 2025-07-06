package edu.vt.mobiledev.taskmate

import androidx.room.TypeConverter
import java.util.Date
import java.util.UUID

class TaskTypeConverters {
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? = uuid?.toString()

    @TypeConverter
    fun toUUID(uuid: String?): UUID? = uuid?.let(UUID::fromString)

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun toDate(millis: Long?): Date? = millis?.let { Date(it) }
}

