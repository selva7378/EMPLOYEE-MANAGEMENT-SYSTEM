package com.example.pridenest.ui.screens.manager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.pridenest.LocalPrideNestViewModel
import com.example.pridenest.ui.screens.utility.EmployeeList

@Composable
fun ManagerListingScreen(modifier: Modifier) {
    EmployeeList(false, {})
}