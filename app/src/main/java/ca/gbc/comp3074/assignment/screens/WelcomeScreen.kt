package ca.gbc.comp3074.assignment.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ca.gbc.comp3074.assignment.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(navController: NavController) {
    // Auto-navigate to restaurant list after 2 seconds
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(Screen.RestaurantList.route) {
            popUpTo(Screen.WelcomeScreen.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Personal Restaurant Guide",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your dining companion",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
