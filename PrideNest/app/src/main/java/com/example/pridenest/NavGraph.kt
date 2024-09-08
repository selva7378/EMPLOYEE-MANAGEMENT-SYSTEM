package com.example.pridenest

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.*
import com.example.pridenest.ui.screens.*
import com.example.pridenest.ui.screens.admin.*
import com.example.pridenest.ui.screens.employee.EmployeeDetailsScreen
import com.example.pridenest.ui.screens.manager.*
import com.example.pridenest.viewmodel.PrideNestViewModel
import kotlinx.coroutines.delay

const val TAG = "NAVGRAPH"

enum class PrideScreen(@StringRes val title: Int) {
    LOGIN_SCREEN(title = R.string.LOGIN_SCREEN),
    ADMIN_DASHBOARD(title = R.string.ADMIN_DASHBOARD),
    MANAGER_DASHBOARD(title = R.string.MANAGER_DASHBOARD),
    EMPLOYEE_DETAILS(title = R.string.EMPLOYEE_DETAILS),
    EMPLOYEE_LOGIN(title = R.string.EMPLOYEE_LOGIN),
    MANAGER_LOGIN(title = R.string.MANAGER_LOGIN),
    ADMIN_LOGIN(title = R.string.ADMIN_LOGIN),
    ADMIN_LISTING(title = R.string.ADMIN_LISTING),
    MANAGER_LISTING(title = R.string.MANAGER_LISTING),
    EMPLOYEE_ADD(title = R.string.EMPLOYEE_ADD),
    EMPLOYEE_DELETE(title = R.string.EMPLOYEE_DELETE)
}

val LocalPrideNestViewModel = compositionLocalOf<PrideNestViewModel> {
    error("No ViewModel provided")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrideNestApp(
    prideNestViewModel: PrideNestViewModel = viewModel(factory = AndroidViewModelProvider.Factory),
    navController: NavHostController = rememberNavController(),
    modifier: Modifier
) {
    var showSplashScreen by remember { mutableStateOf(true) }

    val lastLoggedInUserId by prideNestViewModel.lastLoggedInUserId.collectAsState()
    val lastLoggedInRole by prideNestViewModel.lastLoggedInRole.collectAsState()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = PrideScreen.valueOf(
        backStackEntry?.destination?.route ?: PrideScreen.LOGIN_SCREEN.name
    )
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(Unit) {
        delay(2000)
        showSplashScreen = false
    }

    if (showSplashScreen) {
        SplashScreen()
    } else {
        CompositionLocalProvider(LocalPrideNestViewModel provides prideNestViewModel) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    PrideAppBar(
                        scrollBehavior = scrollBehavior,
                        currentScreen = currentScreen,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() }
                    )
                },
                floatingActionButton = {
                    if (currentScreen == PrideScreen.ADMIN_LISTING) {
                        FloatingActionButton(
                            onClick = { navController.navigate(PrideScreen.EMPLOYEE_ADD.name) },
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .padding(
                                    end = WindowInsets.safeDrawing.asPaddingValues()
                                        .calculateEndPadding(LocalLayoutDirection.current)
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "add employee"
                            )
                        }
                    }
                },
            ) { innerPadding ->
                val startDestination = if (lastLoggedInUserId != null && lastLoggedInRole != null) {
                    when (lastLoggedInRole) {
                        "admin" -> PrideScreen.ADMIN_DASHBOARD.name
                        "manager" -> PrideScreen.MANAGER_DASHBOARD.name
                        "employee" -> PrideScreen.EMPLOYEE_DETAILS.name
                        else -> PrideScreen.LOGIN_SCREEN.name
                    }
                } else {
                    PrideScreen.LOGIN_SCREEN.name
                }

                Log.i(TAG, "$startDestination")

                val onLoginSuccess: (String, String) -> Unit = { userId, role ->
                    val destination = when (role) {
                        "manager" -> PrideScreen.MANAGER_DASHBOARD.name
                        "admin" -> PrideScreen.ADMIN_DASHBOARD.name
                        "employee" -> PrideScreen.EMPLOYEE_DETAILS.name
                        else -> PrideScreen.LOGIN_SCREEN.name
                    }

                    // Save login details in the DataStore and navigate
                    prideNestViewModel.login(userId, role)
                    navController.navigate(destination)
                }

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(route = PrideScreen.LOGIN_SCREEN.name) {
                        RoleScreen(
                            navigateToAdmin = { navController.navigate(PrideScreen.ADMIN_LOGIN.name) },
                            navigateToManager = { navController.navigate(PrideScreen.MANAGER_LOGIN.name) },
                            navigateToEmployee = { navController.navigate(PrideScreen.EMPLOYEE_LOGIN.name) },
                        )
                    }
                    composable(route = PrideScreen.ADMIN_LOGIN.name) {
                        AdminLogin(onLoginSuccess = onLoginSuccess)
                    }
                    composable(route = PrideScreen.MANAGER_LOGIN.name) {
                        ManagerLogin(onLoginSuccess = onLoginSuccess)
                    }
                    composable(route = PrideScreen.EMPLOYEE_LOGIN.name) {
                        EmployeeLogin(onLoginSuccess = onLoginSuccess)
                    }
                    composable(route = PrideScreen.ADMIN_DASHBOARD.name) {
                        AdminDashboard(
                            { navController.navigate(PrideScreen.ADMIN_LISTING.name) },
                            innerPadding
                        )
                    }
                    composable(route = PrideScreen.MANAGER_DASHBOARD.name) {
                        ManagerDashboard(
                            { navController.navigate(PrideScreen.MANAGER_LISTING.name) },
                            innerPadding
                        )
                    }
                    composable(route = PrideScreen.EMPLOYEE_DETAILS.name) {
                        EmployeeDetailsScreen(innerPadding)
                    }
                    composable(route = PrideScreen.ADMIN_LISTING.name) {
                        AdminListingScreen(
                            navigateToEmployeeEditScreen = { navController.navigate(PrideScreen.EMPLOYEE_DELETE.name) },
                            modifier
                        )
                    }
                    composable(route = PrideScreen.MANAGER_LISTING.name) {
                        ManagerListingScreen(modifier)
                    }
                    composable(route = PrideScreen.EMPLOYEE_ADD.name) {
                        EmployeeAddScreen(
                            navigateBack = { navController.navigateUp() }
                        )
                    }

                    composable(route = PrideScreen.EMPLOYEE_DELETE.name) {
                        EmployeeEditScreen(
                            navigateBack = { navController.navigateUp() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrideAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    currentScreen: PrideScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = LocalPrideNestViewModel.current
    val lastLoggedInUserId by viewModel.lastLoggedInUserId.collectAsState()
    var expanded by remember { mutableStateOf(false) }  // State to track whether the menu is expanded

    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            // Menu icon button
            if (lastLoggedInUserId != null) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "more obtions"
                    )
                }
            }

            // DropdownMenu to display options
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Log Out") },
                    onClick = {
                        expanded = false
                        viewModel.logout()
                    }
                )
            }
        }
    )
}

@Composable
fun SplashScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation))

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.fillMaxSize()
    )
}
