package com.zivaclinic.skinscanner.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.zivaclinic.skinscanner.data.local.ScanDatabase
import com.zivaclinic.skinscanner.data.repository.ScanRepository
import com.zivaclinic.skinscanner.ui.screens.history.HistoryScreen
import com.zivaclinic.skinscanner.ui.screens.history.HistoryViewModel
import com.zivaclinic.skinscanner.ui.screens.home.HomeScreen
import com.zivaclinic.skinscanner.ui.screens.profile.ProfileScreen
import com.zivaclinic.skinscanner.ui.screens.results.ResultsScreen
import com.zivaclinic.skinscanner.ui.screens.results.ResultsViewModel
import com.zivaclinic.skinscanner.ui.screens.scan.ScanScreen
import com.zivaclinic.skinscanner.ui.screens.scan.ScanViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Scan : Screen("scan", "Scan")
    object Results : Screen("results/{scanId}", "Results")
    object History : Screen("history", "History", Icons.Default.History)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.History,
    Screen.Profile
)

@Composable
fun AppNavigation(
    navController: NavHostController,
    scanDatabase: ScanDatabase
) {
    val repository = ScanRepository(scanDatabase.scanDao())

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.Scan.route) {
            val viewModel = ScanViewModel(repository)
            ScanScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.Results.route,
            arguments = listOf(navArgument("scanId") { type = NavType.StringType })
        ) { backStackEntry ->
            val scanId = backStackEntry.arguments?.getString("scanId") ?: ""
            val viewModel = ResultsViewModel(repository)
            ResultsScreen(
                navController = navController,
                scanId = scanId,
                viewModel = viewModel
            )
        }

        composable(Screen.History.route) {
            val viewModel = HistoryViewModel(repository)
            HistoryScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomNavItems.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            NavigationBarItem(
                icon = {
                    screen.icon?.let { icon ->
                        Icon(
                            imageVector = icon,
                            contentDescription = screen.title
                        )
                    }
                },
                label = { Text(screen.title) },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
        }
    }
}

@Composable
fun MainScreen(
    navController: NavHostController,
    scanDatabase: ScanDatabase
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        AppNavigation(
            navController = navController,
            scanDatabase = scanDatabase,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    scanDatabase: ScanDatabase,
    modifier: Modifier = Modifier
) {
    val repository = ScanRepository(scanDatabase.scanDao())

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.Scan.route) {
            val viewModel = ScanViewModel(repository)
            ScanScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.Results.route,
            arguments = listOf(navArgument("scanId") { type = NavType.StringType })
        ) { backStackEntry ->
            val scanId = backStackEntry.arguments?.getString("scanId") ?: ""
            val viewModel = ResultsViewModel(repository)
            ResultsScreen(
                navController = navController,
                scanId = scanId,
                viewModel = viewModel
            )
        }

        composable(Screen.History.route) {
            val viewModel = HistoryViewModel(repository)
            HistoryScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}
