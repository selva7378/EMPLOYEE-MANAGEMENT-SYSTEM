package com.example.pridenest.ui.screens.admin

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.pridenest.PrideScreen
import com.example.pridenest.ui.screens.utility.EmployeeList

@Composable
fun AdminListingScreen(
    navigateToEmployeeEditScreen: () -> Unit,
    modifier: Modifier
) {

    EmployeeList(
        allowCardClick = true,
        navigateToEmployeeEditScreen
    )

}






