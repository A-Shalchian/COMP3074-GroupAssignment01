package ca.gbc.comp3074.assignment.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ca.gbc.comp3074.assignment.data.Restaurant
import ca.gbc.comp3074.assignment.data.RestaurantDatabase
import ca.gbc.comp3074.assignment.data.RestaurantRepository
import ca.gbc.comp3074.assignment.navigation.Screen
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailsScreen(
    navController: NavController,
    restaurantId: String?
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var restaurant by remember { mutableStateOf<Restaurant?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(restaurantId) {
        restaurantId?.toIntOrNull()?.let { id ->
            val database = RestaurantDatabase.getDatabase(context)
            val repository = RestaurantRepository(database.restaurantDao())
            restaurant = repository.getRestaurant(id)
        }
    }

    if (restaurant == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val tags = restaurant!!.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    Scaffold(
        topBar = {
            TopAppBar(
                //back and edit button
                title = { Text("Restaurant Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.AddEditRestaurant.createRoute(restaurantId))
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = restaurant!!.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            val ratingInt = restaurant!!.rating.toInt()
            Text(
                text = "★".repeat(ratingInt) + "☆".repeat(5 - ratingInt) + "  (${restaurant!!.rating})",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Address:", style = MaterialTheme.typography.titleSmall)
            Text(text = restaurant!!.address, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Phone:", style = MaterialTheme.typography.titleSmall)
            Text(text = restaurant!!.phone.ifEmpty { "Not provided" }, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Description:", style = MaterialTheme.typography.titleSmall)
            Text(
                text = restaurant!!.description.ifEmpty { "No description provided" },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
            //show restaurant tags
            Text(text = "Tags:", style = MaterialTheme.typography.titleSmall)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                tags.forEach { tag ->
                    AssistChip(onClick = {}, label = { Text(tag) })
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(restaurant!!.address)}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")

                    try {
                        context.startActivity(mapIntent)
                    } catch (e: ActivityNotFoundException) {
                        val webIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(restaurant!!.address)}")
                        )
                        context.startActivity(webIntent)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Map, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("View on Map")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    val shareText = """
                        Check out ${restaurant!!.name}!

                        Address: ${restaurant!!.address}
                        Phone: ${restaurant!!.phone}
                        Rating: ${restaurant!!.rating} stars

                        ${restaurant!!.description}

                        Shared from Personal Restaurant Guide
                    """.trimIndent()

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Restaurant Recommendation: ${restaurant!!.name}")
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share Restaurant"))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share Restaurant")
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Restaurant") },
            text = { Text("Are you sure you want to delete ${restaurant!!.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            val database = RestaurantDatabase.getDatabase(context)
                            val repository = RestaurantRepository(database.restaurantDao())
                            repository.delete(restaurant!!)
                            navController.navigateUp()
                        }
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
