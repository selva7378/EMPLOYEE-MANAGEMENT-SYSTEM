package com.example.pridenest.repository

import android.util.Log
import com.example.pridenest.roomdb.Employee
import com.example.pridenest.roomdb.EmployeesDao
import kotlinx.coroutines.flow.Flow

interface EmployeesDatabaseRepository {

    suspend fun insert(employee: Employee)

    fun getAll(): Flow<List<Employee>>

    fun getRole(employeeId: Int): Flow<String>

    suspend fun getTeamByEmployeeId(employeeId: Int): String?

//    suspend fun validateEmployeeCredentials(employeeId: Int, password: String, designation: String): Employee?

    suspend fun validateEmployeeCredentials(employeeId: Int, password: String): Employee?

    suspend fun getDbSize(): Int

    fun search(query: String): Flow<List<Employee>>

    fun searchInTeam(query: String, team: String): Flow<List<Employee>>

    fun getAllByTeam(team: String): Flow<List<Employee>>

    fun getUniqueTeams(): Flow<List<String>>

    fun getRecentHiresOverall(): Flow<List<Employee>>

    suspend fun getUniqueTeamCount(): Int

    suspend fun getEmployeeCountByTeam(team: String): Int

    fun getEmployeeById(employeeId: Int): Flow<Employee>

    fun getRecentHiresByTeam(team: String): Flow<List<Employee>>

    suspend fun delete(employee: Employee)
}

class OfflineEmployeesRepository(private val employeesDao: EmployeesDao) : EmployeesDatabaseRepository {
    override suspend fun insert(employee: Employee) = employeesDao.insert(employee)

    override fun getAll(): Flow<List<Employee>> = employeesDao.getAll()

    override fun getRole(employeeId: Int): Flow<String> = employeesDao.getRole(employeeId)

//    override suspend fun validateEmployeeCredentials(employeeId: Int, password: String, designation: String): Employee? = employeesDao.validateEmployeeCredentials(employeeId, password, designation)

    override suspend fun validateEmployeeCredentials(employeeId: Int, password: String): Employee? = employeesDao.validateEmployeeCredentials(employeeId, password)

    override suspend fun getDbSize(): Int = employeesDao.getDbSize()

    override suspend fun getTeamByEmployeeId(employeeId: Int): String? = employeesDao.getTeamByEmployeeId(employeeId)

    override fun search(query: String): Flow<List<Employee>> = employeesDao.search(query)

    override fun searchInTeam(query: String, team: String): Flow<List<Employee>> = employeesDao.searchInTeam(query, team)

    override fun getAllByTeam(team: String): Flow<List<Employee>> = employeesDao.getAllByTeam(team)

    override fun getUniqueTeams(): Flow<List<String>> = employeesDao.getUniqueTeams()

    override fun getRecentHiresOverall(): Flow<List<Employee>> = employeesDao.getRecentHiresOverall()

    override suspend fun getUniqueTeamCount(): Int = employeesDao.getUniqueTeamCount()

    override suspend fun getEmployeeCountByTeam(team: String): Int = employeesDao.getEmployeeCountByTeam(team)

    override fun getEmployeeById(employeeId: Int): Flow<Employee> = employeesDao.getEmployeeById(employeeId)

    override fun getRecentHiresByTeam(team: String): Flow<List<Employee>> = employeesDao.getRecentHiresByTeam(team)

    override suspend fun delete(employee: Employee) {
        if (employee.designation.equals("Admin", ignoreCase = true)) {
            val adminCount = employeesDao.getAdminCount()
            Log.i("database", "$adminCount")
            if (adminCount > 1) {
                employeesDao.delete(employee)
            }
        } else {
            employeesDao.delete(employee)
        }
    }


}