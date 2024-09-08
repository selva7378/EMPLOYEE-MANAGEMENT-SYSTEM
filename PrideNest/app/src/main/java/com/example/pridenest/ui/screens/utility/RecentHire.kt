package com.example.pridenest.ui.screens.utility

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.pridenest.LocalPrideNestViewModel
import com.example.pridenest.R
import com.example.pridenest.roomdb.Employee

@Composable
fun RecentHire(
    recentHire: List<Employee>,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),

        ) {
        items(recentHire) { employee ->
            EmployeeCard(
                allowCardClick = false,
                navigateToEditScreen = {},
                employee = employee,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small)))
        }
    }
}