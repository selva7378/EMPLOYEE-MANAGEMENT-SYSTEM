package com.example.pridenest.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeesDao {
    @Insert
    suspend fun insert(employee: Employee)

    @Query("SELECT * FROM Employees ORDER BY employeeId DESC")
    fun getAll(): Flow<List<Employee>>

    @Delete
    suspend fun delete(employee: Employee)

    @Query("SELECT team FROM Employees WHERE employeeId = :employeeId")
    suspend fun getTeamByEmployeeId(employeeId: Int): String?

    @Query("DELETE FROM Employees")
    suspend fun clear()

    @Query("SELECT * FROM Employees WHERE employeeId = :employeeId AND password = :password AND designation = :role")
    suspend fun validateEmployeeCredentials(employeeId: Int, password: String, role: String): Employee?

    @Query("SELECT * FROM Employees WHERE employeeId = :employeeId AND password = :password")
    suspend fun validateEmployeeCredentials(employeeId: Int, password: String): Employee?

    @Query("SELECT designation FROM Employees WHERE employeeId = :employeeId ")
    fun getRole(employeeId: Int): Flow<String>

    @Query("SELECT * FROM Employees WHERE employeeId = :employeeId")
    fun getEmployeeById(employeeId: Int): Flow<Employee>



    @Query("SELECT COUNT(*) FROM Employees")
    suspend fun getDbSize(): Int

    @Query("SELECT * FROM Employees WHERE name LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<Employee>>

    @Query("SELECT * FROM Employees WHERE team = :team AND name LIKE '%' || :query || '%'")
    fun searchInTeam(query: String, team: String): Flow<List<Employee>>


    @Query("SELECT * FROM Employees WHERE team = :team")
    fun getAllByTeam(team: String): Flow<List<Employee>>

    @Query("SELECT DISTINCT team FROM Employees")
    fun getUniqueTeams(): Flow<List<String>>

    @Query("SELECT * FROM Employees ORDER BY employeeId DESC LIMIT 5")
    fun getRecentHiresOverall(): Flow<List<Employee>>

    @Query("SELECT COUNT(DISTINCT team) FROM Employees")
    suspend fun getUniqueTeamCount(): Int

    @Query("SELECT COUNT(*) FROM Employees WHERE team = :team")
    suspend fun getEmployeeCountByTeam(team: String): Int

    @Query("SELECT * FROM Employees WHERE team = :team ORDER BY employeeId DESC LIMIT 5")
    fun getRecentHiresByTeam(team: String): Flow<List<Employee>>
}