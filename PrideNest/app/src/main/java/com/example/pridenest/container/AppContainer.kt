package com.example.pridenest.data

import android.content.Context
import com.example.pridenest.datastore.DataStoreManager
import com.example.pridenest.repository.EmployeesDatabaseRepository
import com.example.pridenest.repository.OfflineEmployeesRepository
import com.example.pridenest.roomdb.EmployeesDatabase

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val employeesRepository: EmployeesDatabaseRepository
    val dataStoreManager: DataStoreManager
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class DefaultAppContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val employeesRepository: EmployeesDatabaseRepository by lazy {
        OfflineEmployeesRepository(EmployeesDatabase.getDatabase(context).employeesDao())
    }

    override val dataStoreManager: DataStoreManager by lazy {
        DataStoreManager(context)
    }

}