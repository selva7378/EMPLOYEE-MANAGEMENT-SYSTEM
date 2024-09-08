package com.example.pridenest.ui.screens.utility

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pridenest.LocalPrideNestViewModel
import com.example.pridenest.roomdb.Employee

@Composable
fun Dashboard(
    recentHire: List<Employee>,
    totalEmployeeCount: Int = 0,
    uniqueTeamCount: Int = 0,
    navigateToListing: () -> Unit = {},
    modifier: Modifier
) {
    val viewModel = LocalPrideNestViewModel.current
    val lastLoggedInRole by viewModel.lastLoggedInRole.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.setEmployeeCountByTeam(lastLoggedInRole ?: "")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(16.dp))



        Text(text = "Total Employee Count: $totalEmployeeCount")

        Spacer(modifier = Modifier.height(16.dp))

        if (lastLoggedInRole != "manager") {
            Text("Number of Teams: $uniqueTeamCount")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = navigateToListing
        ) {
            Text(text = "All Employees")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "RECENT HIRES",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold,
            style = TextStyle(
                fontStyle = FontStyle.Italic,
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(5f, 5f),
                    blurRadius = 5f
                ),
                fontFamily = FontFamily.Cursive,
                fontSize = 24.sp
            ),
            modifier = Modifier.padding(12.dp)
        )

        RecentHire(
            recentHire = recentHire,
            Modifier
        )

        Button(
            onClick = navigateToListing
        ) {
            Text(text = "All Employees")
        }
    }
}
