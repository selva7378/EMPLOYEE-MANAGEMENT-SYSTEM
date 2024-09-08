package com.example.pridenest.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pridenest.LocalPrideNestViewModel
import com.example.pridenest.roomdb.Employee
import com.example.pridenest.viewmodel.PrideNestViewModel
import kotlinx.coroutines.launch


@Composable
fun RoleScreen(
    navigateToAdmin: () -> Unit,
    navigateToManager: () -> Unit,
    navigateToEmployee: () -> Unit,
    modifier: Modifier = Modifier
) {

    // Fancy gradient background
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Admin Button
                FancyButton(
                    text = "Admin",
                    onClick = navigateToAdmin,
                )


                // Manager Button
                FancyButton(
                    text = "Manager",
                    onClick = navigateToManager,
                )
            }


            // Employee Button
            FancyButton(
                text = "Employee",
                onClick = navigateToEmployee,
            )
        }
    }
}

// Reusable Fancy Button
@Composable
fun FancyButton(
    text: String,
    onClick: () -> Unit,
) {
    ElevatedButton(
        onClick = onClick,
        modifier = Modifier
            .height(60.dp) // Set fixed height
            .width(130.dp),
        shape = CutCornerShape(topEnd = 16.dp, bottomStart = 16.dp, bottomEnd =  16.dp, topStart =  16.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(8.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}


@Composable
fun EmployeeLogin(
    onLoginSuccess: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LoginScreen(onLoginSuccess, "employee")
}

@Composable
fun AdminLogin(onLoginSuccess: (String, String) -> Unit, modifier: Modifier = Modifier) {
    LoginScreen(onLoginSuccess, "admin")
}

@Composable
fun ManagerLogin(onLoginSuccess: (String, String) -> Unit, modifier: Modifier = Modifier) {
    LoginScreen(onLoginSuccess, "manager")
}

@Composable
fun LoginScreen(
    onLoginSuccess: (String, String) -> Unit,
    role: String,
    modifier: Modifier = Modifier
) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val viewModel = LocalPrideNestViewModel.current
    var employee: Employee?


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login as $role", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = userId,
            onValueChange = { userId = it },
            label = { Text("User ID") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            value = password,
            onValueChange = { password = it.trim()  },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))
        ElevatedButton(
            onClick = {
                coroutineScope.launch {
                    // Call ViewModel to validate user credentials
                    employee = viewModel.validateEmployee(userId, password, role)
                    if (employee != null) {
                        // Valid login, navigate to the next screen and pass userId
                        onLoginSuccess(userId, role)
                    } else {
                        // Invalid login, show Toast
                        Toast.makeText(context, "Invalid User ID or Password", Toast.LENGTH_SHORT).show()
                    }
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}
