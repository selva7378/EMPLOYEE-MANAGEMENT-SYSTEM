package com.example.pridenest.ui.screens.admin

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.pridenest.LocalPrideNestViewModel
import com.example.pridenest.ui.screens.utility.Dashboard

@Composable
fun AdminDashboard(
    navigateToAdminListing: () -> Unit = {},
    modifier: PaddingValues
) {
    val viewModel = LocalPrideNestViewModel.current
    val totalEmployeeCount by viewModel.dbSize.collectAsState()
    val uniqueTeamCount by viewModel.uniqueTeamCount.collectAsState()
    val recentHire by viewModel.allEmployeesList.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getRecentHiresOverall()
        viewModel.setDbSize()
        viewModel.setUniqueTeamCount()
    }
    Dashboard(
        recentHire = recentHire,
        totalEmployeeCount = totalEmployeeCount,
        uniqueTeamCount = uniqueTeamCount,
        navigateToListing = navigateToAdminListing,
        modifier = Modifier
    )
}

