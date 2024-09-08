package com.example.pridenest.ui.screens.manager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pridenest.LocalPrideNestViewModel
import com.example.pridenest.ui.screens.utility.Dashboard

@Composable
fun ManagerDashboard(
    navigateToManagerListing: () -> Unit = {},
    modifier: PaddingValues
) {
    val viewModel = LocalPrideNestViewModel.current
    val lastLoggedInUserId by viewModel.lastLoggedInUserId.collectAsState()
    val lastLoggedInUserTeam by viewModel.lastLoggedInUserTeam.collectAsState()
    val recentHires by viewModel.allEmployeesList.collectAsState() // Recent hires from viewModel
    val totalEmployeeCount by viewModel.employeeCountByTeam.collectAsState() // Total employee count by team

    // Load recent hires and employee count for the manager's team
    LaunchedEffect(lastLoggedInUserTeam) {
        viewModel.getTeamByEmployeeId(lastLoggedInUserId?.toInt() ?: 0)
        if (lastLoggedInUserTeam != null) {
            viewModel.getRecentHiresByTeam(lastLoggedInUserTeam!!)
            viewModel.setEmployeeCountByTeam(lastLoggedInUserTeam!!)
        }
    }
    Dashboard(
        recentHire = recentHires,
        totalEmployeeCount = totalEmployeeCount,
        navigateToListing = navigateToManagerListing,
        modifier = Modifier
    )
}
