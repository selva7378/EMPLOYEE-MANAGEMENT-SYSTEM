package com.example.pridenest.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pridenest.viewmodel.PrideNestViewModel

@Composable
fun SettingsScreen(viewModel: PrideNestViewModel, navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = {
            viewModel.logout()
            navController.navigate("login") {
                popUpTo("main") { inclusive = true }
            }
        }) {
            Text("Logout")
        }
    }
}