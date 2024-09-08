package com.example.pridenest.ui.screens.employee

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pridenest.LocalPrideNestViewModel
import com.example.pridenest.R
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun EmployeeDetailsScreen(modifier: PaddingValues) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        val viewModel = LocalPrideNestViewModel.current
        val lastLoggedInUserId by viewModel.lastLoggedInUserId.collectAsState()
        viewModel.getEmployeeById(lastLoggedInUserId?.toInt() ?: 0)
        val employee by viewModel.employee.collectAsState()

        HeaderSection(
            profileImage = employee.profileImage,  // Pass the profile image URI
            name = employee.name,
            role = employee.designation,
            modifier = Modifier
        )
        EmployeeDetailsSection(
            userId = employee.employeeId,
            team = employee.team,
            salary = employee.salary,
        )
    }
}

@Composable
fun HeaderSection(profileImage: String?, name: String, role: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Load profile image with Glide
        profileImage?.let {
            GlideImage(
                imageModel = {it}, // Use the profile image URL from the employee table
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
            )
        } ?: run {
            // Placeholder if no profile image is provided
            Image(
                painter = painterResource(id = R.drawable.place_holder), // your placeholder image
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }

        // Display employee name
        Text(
            text = name,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold,
            style = TextStyle(
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Cursive,
                fontSize = 50.sp
            ),
            modifier = Modifier.padding(12.dp)
        )

        // Display employee role
        Text(
            text = role,
            color = MaterialTheme.colorScheme.primary,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Gray,
                    offset = Offset(5f, 5f),
                    blurRadius = 5f,
                ),
                fontSize = 32.sp
            ),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
        )
    }
}


@Composable
fun EmployeeDetailsSection(
    userId: Int,
    team: String,
    salary: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            Text(
                text = "UserId : ",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = userId.toString(),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            Text(
                text = "Team   : ",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = team,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            Text(
                text = "Salary  : ",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = salary,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}