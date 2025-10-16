package ca.gbc.comp3074.assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ca.gbc.comp3074.assignment.navigation.Screen
import ca.gbc.comp3074.assignment.screens.*
import ca.gbc.comp3074.assignment.ui.theme.AssignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssignmentTheme {
                RestaurantGuideApp()
            }
        }
    }
}

@Composable
fun RestaurantGuideApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.WelcomeScreen.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.WelcomeScreen.route) {
            WelcomeScreen(navController)
        }

        composable(Screen.RestaurantList.route) {
            RestaurantListScreen(navController)
        }

        composable(
            route = Screen.RestaurantDetails.route,
            arguments = listOf(
                navArgument("restaurantId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId")
            RestaurantDetailsScreen(navController, restaurantId)
        }

        composable(
            route = Screen.AddEditRestaurant.route,
            arguments = listOf(
                navArgument("restaurantId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId")
            AddEditRestaurantScreen(navController, restaurantId)
        }

        composable(
            route = Screen.MapScreen.route,
            arguments = listOf(
                navArgument("restaurantId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId")
            MapScreen(navController, restaurantId)
        }

        composable(Screen.About.route) {
            AboutScreen(navController)
        }
    }
}