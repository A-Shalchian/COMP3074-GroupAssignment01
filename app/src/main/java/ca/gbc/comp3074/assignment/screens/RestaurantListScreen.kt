package ca.gbc.comp3074.assignment.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ca.gbc.comp3074.assignment.data.RestaurantDatabase
import ca.gbc.comp3074.assignment.data.RestaurantRepository
import ca.gbc.comp3074.assignment.navigation.Screen
import ca.gbc.comp3074.assignment.viewmodel.RestaurantListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantListScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: RestaurantListViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                val database = RestaurantDatabase.getDatabase(context)
                val repository = RestaurantRepository(database.restaurantDao())
                @Suppress("UNCHECKED_CAST")
                return RestaurantListViewModel(repository) as T
            }
        }
    )

    val restaurants by viewModel.restaurants.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            //title and about button
            TopAppBar(
                title = { Text("My Restaurants", fontWeight = FontWeight.Medium) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                        Icon(Icons.Default.Info, contentDescription = "About")
                    }
                }
            )
        },
        floatingActionButton = {
            //add a new restaurant
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddEditRestaurant.createRoute()) },
                modifier = Modifier.semantics { contentDescription = "Add new restaurant" }
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                label = { Text("Search by name or tag") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp)
            ) {
                items(restaurants.size) { index ->
                    val restaurant = restaurants[index]
                    RestaurantCard(
                        name = restaurant.name,
                        address = restaurant.address,
                        rating = restaurant.rating.toInt(),
                        backgroundColor = listOf(
                            Color(0xFFFFE5B4),
                            Color(0xFFE8F5E9),
                            Color(0xFFE3F2FD),
                            Color(0xFFFFF9C4)
                        )[index % 4],
                        onClick = { navController.navigate(Screen.RestaurantDetails.createRoute("${restaurant.id}")) }
                    )
                }
                if (restaurants.isEmpty()) {
                    item {
                        Text(
                            text = "No restaurants found. Add your first restaurant!",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RestaurantCard(
    name: String,
    address: String,
    rating: Int,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(//reusable card that shows the restaurant info
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
            .semantics { contentDescription = "$name, $address, $rating stars" },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //image placeholder
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Restaurant info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "★".repeat(rating) + "☆".repeat(5 - rating),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "($rating.0)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


