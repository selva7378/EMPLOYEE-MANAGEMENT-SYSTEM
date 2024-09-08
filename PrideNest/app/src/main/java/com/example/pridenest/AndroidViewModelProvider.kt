package com.example.pridenest

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pridenest.viewmodel.PrideNestViewModel

object AndroidViewModelProvider {

    val Factory = viewModelFactory {

        initializer {

            PrideNestViewModel(
                PrideNestApplication().container.employeesRepository,
                PrideNestApplication().container.dataStoreManager
            )
        }
    }
}


fun CreationExtras.PrideNestApplication(): PrideNestApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PrideNestApplication)