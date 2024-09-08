package com.example.pridenest.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Employees")
data class Employee(
    @PrimaryKey(autoGenerate = true)
    var employeeId: Int = 0,
    val name: String = "",
    val password: String = "",
    val designation: String = "",
    val team: String = "",
    val salary: String = "",
    val profileImage: String? = null,
)



