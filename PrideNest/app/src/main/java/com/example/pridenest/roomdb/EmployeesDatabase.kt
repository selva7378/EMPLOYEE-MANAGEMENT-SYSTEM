package com.example.pridenest.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Employee::class], version = 1, exportSchema = false)
abstract class EmployeesDatabase: RoomDatabase() {

    abstract fun employeesDao(): EmployeesDao

    companion object {
        @Volatile
        private var Instance: EmployeesDatabase? = null

        fun getDatabase(context: Context): EmployeesDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, EmployeesDatabase::class.java, "news_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}