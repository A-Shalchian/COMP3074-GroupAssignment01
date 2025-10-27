package ca.gbc.comp3074.assignment.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.gbc.comp3074.assignment.data.Restaurant
import ca.gbc.comp3074.assignment.data.RestaurantDatabase
import ca.gbc.comp3074.assignment.data.RestaurantRepository
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavController,
    restaurantId: String?
) {
    val context = LocalContext.current
    var restaurant by remember { mutableStateOf<Restaurant?>(null) }

    LaunchedEffect(restaurantId) {
        restaurantId?.toIntOrNull()?.let { id ->
            val database = RestaurantDatabase.getDatabase(context)
            val repository = RestaurantRepository(database.restaurantDao())
            restaurant = repository.getRestaurant(id)
        }
    }

    val location = LatLng(43.6532, -79.3832)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(restaurant?.name ?: "Restaurant Location") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    restaurant?.let {
                        val uri = Uri.parse("google.navigation:q=${location.latitude},${location.longitude}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.setPackage("com.google.android.apps.maps")
                        context.startActivity(intent)
                    }
                }
            ) {
                Icon(Icons.Default.Directions, contentDescription = "Get Directions")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (restaurant != null) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = MarkerState(position = location),
                        title = restaurant!!.name,
                        snippet = restaurant!!.address
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
