package edu.vt.mobiledev.taskmate

import androidx.room.TypeConverter
import java.util.Date
import java.util.UUID
//Siddharth Jain
//PID: siddharthjain
class TaskTypeConverters {
    /**
     * converts from UUID
     */
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? = uuid?.toString()
    /**
     * converts to UUID
     */
    @TypeConverter
    fun toUUID(uuid: String?): UUID? = uuid?.let(UUID::fromString)
    /**
     * converts date
     */
    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time
    /**
     * converts date
     */
    @TypeConverter
    fun toDate(millis: Long?): Date? = millis?.let { Date(it) }
}

