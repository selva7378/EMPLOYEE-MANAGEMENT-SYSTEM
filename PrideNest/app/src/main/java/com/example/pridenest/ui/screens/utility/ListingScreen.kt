package com.example.pridenest.ui.screens.utility

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pridenest.LocalPrideNestViewModel
import com.example.pridenest.PrideScreen
import com.example.pridenest.R
import com.example.pridenest.roomdb.Employee
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeList(
    allowCardClick: Boolean,
    navigateToEditScreen : () -> Unit,
    modifier: Modifier = Modifier
) {


    val viewModel = LocalPrideNestViewModel.current
    val lastLoggedInRole by viewModel.lastLoggedInRole.collectAsState()
    val lastLoggedInUserTeam by viewModel.lastLoggedInUserTeam.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getUniqueTeam()
        if (lastLoggedInRole == "admin") {
            viewModel.getAllEmployees()
        } else if(lastLoggedInRole == "manager"){
            Log.i("teamname", "$lastLoggedInUserTeam team")
            viewModel.getEmployeesByTeam(lastLoggedInUserTeam!!)
        }
    }

    val allEmployeeList by viewModel.allEmployeesList.collectAsState()
    val uniqueTeams by viewModel.uniqueTeam.collectAsState()

    var searchValue by rememberSaveable {
        mutableStateOf("")
    }
    var isSearchFocesed by rememberSaveable {
        mutableStateOf(false)
    }
    val filterEmployee: (String) -> Unit = { team ->
        viewModel.getEmployeesByTeam(team)
    }


    Column {
        Spacer(modifier = Modifier.height(4.dp))
        DockedSearchBar(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            },
            trailingIcon = {
                if(isSearchFocesed){
                    IconButton(
                        onClick = {
                            isSearchFocesed = false
                            searchValue = ""
                            viewModel.getAllEmployees()
                        },
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            },
            query = searchValue,
            onQueryChange = {
                searchValue = it
                if (lastLoggedInRole == "admin"){
                    viewModel.getEmployeesBySearch(it)
                }else {
                    viewModel.getEmployeesBySearchInTeam(it, lastLoggedInUserTeam!!)
                }

            },
            onSearch = {
                if (lastLoggedInRole == "admin"){
                    viewModel.getEmployeesBySearch(it)
                }else {
                viewModel.getEmployeesBySearchInTeam(it, lastLoggedInUserTeam!!)
            }
            },
            active = isSearchFocesed,
            onActiveChange = { isActive ->
                isSearchFocesed = isActive
            },
            modifier = Modifier.height(56.dp).fillMaxWidth()
        ) {
        }

        if (lastLoggedInRole == "admin") {
            TeamList(
                teams = uniqueTeams,
                filterEmployee = filterEmployee
            )
        }

        val listState = rememberLazyListState()


        LazyColumn(
            state = listState, // Assign the remembered list state
            modifier = modifier.fillMaxSize(),

            ) {
            items(allEmployeeList) { employee ->
                EmployeeCard(
                    allowCardClick = allowCardClick,
                    navigateToEditScreen = navigateToEditScreen,
                    employee = employee,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_small)))
            }
        }
    }
}

@Composable
fun TeamList(
    teams: List<String?>,
    filterEmployee: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = LocalPrideNestViewModel.current

    var selectedTeam by rememberSaveable { mutableStateOf<String?>(null) }

    LazyRow {
        items(teams) { team ->
            TeamCard(
                category = team ?: "",
                isSelected = selectedTeam == team,
                onTeamClick = { selectedCategory ->
                    if (selectedCategory == selectedTeam) {
                        selectedTeam = null
                        viewModel.getAllEmployees()
                    } else {
                        selectedTeam = selectedCategory
                        filterEmployee(selectedCategory)
                    }
                }
            )
        }
    }
}


@Composable
fun TeamCard(
    category: String,
    isSelected: Boolean,
    onTeamClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp)
            .graphicsLayer {
                alpha = if (isSelected) 1f else 0.7f
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = {
            onTeamClick(category)
        }
    ) {
        Text(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.CenterHorizontally),
            text = category,
            style = if (isSelected) MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            else MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}



@Composable
fun EmployeeCard(
    allowCardClick: Boolean,
    navigateToEditScreen : () -> Unit,
    employee: Employee,
    modifier: Modifier = Modifier
) {
    val viewModel = LocalPrideNestViewModel.current
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .fillMaxWidth()
            .clickable {
                if(allowCardClick) {
                    viewModel.getEmployeeById(employee.employeeId)
                    navigateToEditScreen()
                }
            }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .sizeIn(minHeight = 72.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1F)
            ) {
                Row(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(
                        text = "Name  : ",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = employee.name,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                Row(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(
                        text = "UserId : ",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = employee.employeeId.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                Row(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(
                        text = "Role     : ",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = employee.designation,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                Row(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(
                        text = "Team   : ",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = employee.team,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                Row(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(
                        text = "Salary  : ",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = employee.salary,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            // Handle the GlideImage logic here
            GlideImage(
                imageModel = {
                    if (employee.profileImage.isNullOrEmpty()) {
                        R.drawable.place_holder
                    } else {
                        Uri.parse(employee.profileImage)
                    }
                },
                modifier = Modifier
                    .size(110.dp)
                    .clip(MaterialTheme.shapes.small),
            )
        }
    }
}

