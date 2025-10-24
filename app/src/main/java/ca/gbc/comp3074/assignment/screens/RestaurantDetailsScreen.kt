package ca.gbc.comp3074.assignment.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.gbc.comp3074.assignment.navigation.Screen
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailsScreen(
    navController: NavController,
    restaurantId: String?
) {
    //restaurant list
    val restaurants = listOf(
        Triple("Bella Italia", "123 Main St, Downtown", 4),
        Triple("Sushi Paradise", "456 Oak Ave, Midtown", 5),
        Triple("Green Garden Café", "789 Pine Rd, Uptown", 3),
        Triple("Spice Route", "321 Elm St, East Side", 4),
        Triple("Taco Fiesta", "654 Maple Rd, West End", 5)
    )


    val restaurantTags = listOf(
        listOf("Italian", "Pasta", "Vegetarian", "Family Friendly"),
        listOf("Japanese", "Sushi", "Seafood", "Fine Dining"),
        listOf("Vegan", "Organic", "Healthy", "Cafe"),
        listOf("Indian", "Spicy", "Curry", "Takeout"),
        listOf("Mexican", "Tacos", "Street Food", "Casual")
    )

    //convert resutarant id toindex
    val index = restaurantId?.toIntOrNull() ?: 0
    val (name, address, rating) = restaurants.getOrElse(index) { restaurants[0] }
    val tags = restaurantTags.getOrElse(index) { emptyList() }

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
            Text(//name and rating
                text = name,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "★".repeat(rating) + "☆".repeat(5 - rating) + "  ($rating.0)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(24.dp))
            //address and phone
            Text(text = "Address:", style = MaterialTheme.typography.titleSmall)
            Text(text = address, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Phone:", style = MaterialTheme.typography.titleSmall)
            Text(text = "(555) 123-4567", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))
            //restaurant desc
            Text(text = "Description:", style = MaterialTheme.typography.titleSmall)
            Text(
                text = when (index) {
                    0 -> "Authentic Italian restaurant known for homemade pasta and cozy atmosphere."
                    1 -> "Elegant sushi bar offering fresh sashimi and premium seafood."
                    2 -> "Relaxed café serving vegan and organic dishes made from local ingredients."
                    3 -> "Vibrant Indian restaurant famous for spicy curries and aromatic spices."
                    4 -> "Colorful Mexican eatery serving tacos and classic street food favorites."
                    else -> "A wonderful dining experience with excellent service and delicious food."
                },
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

            //map and share button
            val context = LocalContext.current

            Button(
                onClick = {
                    val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")

                    try {
                        context.startActivity(mapIntent)
                    } catch (e: ActivityNotFoundException) {
                        // Fallback if Google Maps isn’t installed
                        val webIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(address)}")
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
                onClick = { /* TODO: Share functionality */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share Restaurant")
            }
        }
    }
}
