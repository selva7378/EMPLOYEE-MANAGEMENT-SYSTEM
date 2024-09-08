package com.example.pridenest.ui.screens.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.pridenest.LocalPrideNestViewModel
import com.example.pridenest.R
import com.example.pridenest.roomdb.Employee
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeAddScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = LocalPrideNestViewModel.current
    val coroutineScope = rememberCoroutineScope()

    ItemEntryBody(
        onSaveClick = { employee ->
            coroutineScope.launch {
                viewModel.insertEmployee(employee)
                navigateBack()
            }
        },
        modifier = Modifier.verticalScroll(rememberScrollState())
    )
}

@Composable
fun ItemEntryBody(
    onSaveClick: (Employee) -> Unit,
    modifier: Modifier = Modifier
) {
    var employeeName by remember { mutableStateOf("") }
    var employeeDesignation by remember { mutableStateOf("") }
    var employeeTeam by remember { mutableStateOf("") }
    var employeeSalary by remember { mutableStateOf(0.0) }
    var employeePassword by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    // Check if all required fields are filled
    val isFormValid = employeeName.isNotBlank() && employeeDesignation.isNotBlank() && employeeTeam.isNotBlank() && employeePassword.isNotBlank() && employeeSalary > 0.0

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)).imePadding(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
    ) {
        // Pass the state and event handlers to ItemInputForm
        ItemInputForm(
            employeeName = employeeName,
            employeeDesignation = employeeDesignation,
            employeeTeam = employeeTeam,
            employeeSalary = employeeSalary,
            employeePassword = employeePassword,
            onEmployeeNameChange = { employeeName = it },
            onEmployeeDesignationChange = { employeeDesignation = it },
            onEmployeeTeamChange = { employeeTeam = it },
            onEmployeeSalaryChange = { employeeSalary = it },
            onEmployeePasswordChange = { employeePassword = it },
            onProfileImageChange = { profileImageUri = it }
        )

        Button(
            onClick = {
                onSaveClick(
                    Employee(
                        name = employeeName,
                        designation = employeeDesignation,
                        team = employeeTeam,
                        salary = employeeSalary.toString(),
                        password = employeePassword,
                        profileImage = profileImageUri?.toString()
                    )
                )
            },
            enabled = isFormValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save Action")
        }
    }
}

@Composable
fun ItemInputForm(
    employeeName: String,
    employeeDesignation: String,
    employeeTeam: String,
    employeeSalary: Double,
    employeePassword: String,
    onEmployeeNameChange: (String) -> Unit,
    onEmployeeDesignationChange: (String) -> Unit,
    onEmployeeTeamChange: (String) -> Unit,
    onEmployeeSalaryChange: (Double) -> Unit,
    onEmployeePasswordChange: (String) -> Unit,
    onProfileImageChange: (Uri) -> Unit,
    enabled: Boolean = true
) {
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Create an ActivityResultLauncher to pick an image from the gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profileImageUri = it
            onProfileImageChange(it) // Update the profileImageUri in the parent composable
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        // Name Input
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            value = employeeName,
            onValueChange = onEmployeeNameChange,
            label = { Text("Name*") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Designation Input
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            value = employeeDesignation,
            onValueChange = onEmployeeDesignationChange,
            label = { Text("Designation*") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Team Input
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            value = employeeTeam,
            onValueChange = onEmployeeTeamChange,
            label = { Text("Team*") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Salary Input
        OutlinedTextField(
            value = if (employeeSalary != 0.0) employeeSalary.toString() else "",
            onValueChange = { onEmployeeSalaryChange(it.toDoubleOrNull() ?: 0.0) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            label = { Text("Salary*") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Password Input
        OutlinedTextField(
            value = employeePassword,
            onValueChange = onEmployeePasswordChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            label = { Text("Password*") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        // Profile Image Selector
        Button(
            onClick = {
                imagePickerLauncher.launch("image/*") // Launch the image picker
            },
            enabled = enabled
        ) {
            Text("Select Profile Image")
        }

        profileImageUri?.let {
            GlideImage(
                imageModel = {it},
                modifier = Modifier.size(100.dp).clip(CircleShape),
            )
        }

        if (enabled) {
            Text(
                text = "*required fields",
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

