package com.example.pridenest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pridenest.datastore.DataStoreManager
import com.example.pridenest.repository.EmployeesDatabaseRepository
import com.example.pridenest.roomdb.Employee
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PrideNestViewModel(
    private val employeeDb: EmployeesDatabaseRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _lastLoggedInUserId = MutableStateFlow<String?>(null)
    val lastLoggedInUserId: StateFlow<String?> = _lastLoggedInUserId

    private val _uniqueTeam: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val uniqueTeam: StateFlow<List<String?>> = _uniqueTeam

    private val _lastLoggedInUserTeam = MutableStateFlow<String?>(null)
    val lastLoggedInUserTeam: StateFlow<String?> = _lastLoggedInUserTeam


    private val _dbSize = MutableStateFlow(0)
    val dbSize: StateFlow<Int> = _dbSize

    private var _employeeToDelete = MutableStateFlow(Employee())
    val employeeToDelete: StateFlow<Employee> = _employeeToDelete

    private var _employee = MutableStateFlow(Employee())
    val employee: StateFlow<Employee> = _employee

    private val _uniqueTeamCount = MutableStateFlow(0)
    val uniqueTeamCount: StateFlow<Int> = _uniqueTeamCount

    private val _employeeCountByTeam = MutableStateFlow(0)
    val employeeCountByTeam: StateFlow<Int> = _employeeCountByTeam

    private val _allEmployeesList: MutableStateFlow<List<Employee>> = MutableStateFlow(listOf())
    val allEmployeesList: StateFlow<List<Employee>> = _allEmployeesList

    private val _employeesListByManager: MutableStateFlow<List<Employee>> =
        MutableStateFlow(listOf())
    val employeesListByManager: StateFlow<List<Employee>> = _employeesListByManager


    private val _lastLoggedInRole = MutableStateFlow<String?>(null)
    val lastLoggedInRole: StateFlow<String?> = _lastLoggedInRole

    init {
        viewModelScope.launch {
            initializeData()

            launch {
                dataStoreManager.lastLoggedInRole.collect { role ->
                    _lastLoggedInRole.value = role
                }
            }

            launch {
                dataStoreManager.lastLoggedInUserId.collect { userId ->
                    _lastLoggedInUserId.value = userId
                }
            }


        }
    }

    fun getAllEmployees() {
        viewModelScope.launch {
            employeeDb.getAll().stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            ).collect { employees ->
                _allEmployeesList.value = employees
            }
        }
    }

    fun getAllEmployeesByManager() {
        viewModelScope.launch {
            employeeDb.getAll().stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            ).collect { employees ->
                _allEmployeesList.value = employees
            }
        }
    }

    suspend fun setUniqueTeamCount() {
        _uniqueTeamCount.value = employeeDb.getUniqueTeamCount()
    }

    suspend fun setEmployeeCountByTeam(team: String) {
        _employeeCountByTeam.value = employeeDb.getEmployeeCountByTeam(team)
    }

    suspend fun getTeamByEmployeeId(employeeId: Int) {
        _lastLoggedInUserTeam.value = employeeDb.getTeamByEmployeeId(employeeId)
    }


    fun getEmployeesBySearch(query: String) {
        if (query.isEmpty()) {
            getAllEmployees()
        } else {
            viewModelScope.launch {
                employeeDb.search(query).stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList()
                ).collect { employees ->
                    _allEmployeesList.value = employees
                }
            }
        }
    }

    fun getEmployeesBySearchInTeam(query: String, team: String ) {
        if (query.isEmpty()) {
            getAllEmployees()
        } else {
            viewModelScope.launch {
                employeeDb.searchInTeam(query, team).stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList()
                ).collect { employees ->
                    _allEmployeesList.value = employees
                }
            }
        }
    }



    fun getEmployeesByTeam(team: String) {
        viewModelScope.launch {
            if (team == "all") {
                getAllEmployees()
            } else {
                employeeDb.getAllByTeam(team).stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList()
                ).collect { employees ->
                    _allEmployeesList.value = employees
                }
            }
        }
    }

    fun getUniqueTeam() {
        viewModelScope.launch {

            employeeDb.getUniqueTeams().stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            ).collect { teams ->
                _uniqueTeam.value = teams
            }
        }

    }

    private suspend fun initializeData() {
        setDbSize()
        if (dbSize.value == 0) {
            insertAdmin()
        }
    }


    suspend fun insertEmployee(employee: Employee) {
        employeeDb.insert(employee)
    }

    fun getRecentHiresOverall(){
        viewModelScope.launch {
            employeeDb.getRecentHiresOverall().stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            ).collect { employees ->
                _allEmployeesList.value = employees
            }
        }
    }

    fun getRecentHiresByTeam(team: String){
        viewModelScope.launch {
            employeeDb.getRecentHiresByTeam(team).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            ).collect { employees ->
                _allEmployeesList.value = employees
            }
        }
    }

    fun getEmployeeById(employeeId: Int){
        viewModelScope.launch {
            employeeDb.getEmployeeById(employeeId)
                .collect { employee ->
                    _employee.value = employee
                    _employeeToDelete.value = employee
                }
        }
    }

    suspend fun deleteEmployee(employee: Employee){
            employeeDb.delete(employee)
    }

    private suspend fun insertAdmin() {
        employeeDb.insert(
            Employee(
                name = "selva",
                password = "selva".trim(),
                designation = "admin",
                team = "admin",
                salary = "100000",
                profileImage = null,
            )
        )
        employeeDb.insert(
            Employee(
                name = "ganesh",
                password = "selva".trim(),
                designation = "manager",
                team = "jambav",
                salary = "100000",
                profileImage = null,
            )
        )
        employeeDb.insert(
            Employee(
                name = "selvaganesh",
                password = "selva".trim(),
                designation = "ios developer",
                team = "desk",
                salary = "100000",
                profileImage = null,
            )
        )
    }


    suspend fun setDbSize() {
        _dbSize.value = employeeDb.getDbSize()
    }

    suspend fun validateEmployee(userId: String, password: String): Employee? {
        Log.i("viewmodel", "$password is the cur password")
        return employeeDb.validateEmployeeCredentials(if (userId == "") 0 else userId.toInt(), password.trim())
    }


    // datastore

    fun login(userId: String, role: String) {
        viewModelScope.launch {
            dataStoreManager.saveLoginDetails(userId, role)

            Log.i("viewmodel", "${_lastLoggedInRole.value}")
            Log.i("viewmodel", "${_lastLoggedInUserId.value}")
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStoreManager.clearLoginDetails()
        }
    }
}