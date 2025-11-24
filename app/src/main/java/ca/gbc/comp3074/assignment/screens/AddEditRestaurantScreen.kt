package ca.gbc.comp3074.assignment.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ca.gbc.comp3074.assignment.data.RestaurantDatabase
import ca.gbc.comp3074.assignment.data.RestaurantRepository
import ca.gbc.comp3074.assignment.viewmodel.AddEditRestaurantViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.FlowRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRestaurantScreen(
    navController: NavController,
    restaurantId: String?
) {
    val context = LocalContext.current
    val viewModel: AddEditRestaurantViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                val database = RestaurantDatabase.getDatabase(context)
                val repository = RestaurantRepository(database.restaurantDao())
                @Suppress("UNCHECKED_CAST")
                return AddEditRestaurantViewModel(repository) as T
            }
        }
    )

    val formState by viewModel.formState.collectAsState()
    val scope = rememberCoroutineScope()
    val isEdit = restaurantId != null

    // ------------------------------
    // NEW TAG SYSTEM ADDED HERE
    // ------------------------------
    val availableTags = listOf(
        "Italian", "Vegan", "Cafe", "Dessert", "Fast Food",
        "Breakfast", "Seafood", "BBQ", "Healthy", "Asian", "Mexican"
    )

    var selectedTags by remember {
        mutableStateOf(
            formState.tags.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        )
    }
    // ------------------------------

    LaunchedEffect(restaurantId) {
        restaurantId?.toIntOrNull()?.let { id ->
            viewModel.loadRestaurant(id)

            // Update selected tags after loading existing restaurant
            selectedTags = formState.tags.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Edit Restaurant" else "Add Restaurant") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = formState.name,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Restaurant Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = formState.address,
                onValueChange = { viewModel.updateAddress(it) },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = formState.phone,
                onValueChange = { viewModel.updatePhone(it) },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = formState.description,
                onValueChange = { viewModel.updateDescription(it) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ------------------------------
            // NEW TAG UI — REPLACES OLD TEXTFIELD
            // ------------------------------
            Text("Select Tags:", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                availableTags.forEach { tag ->
                    val isSelected = selectedTags.contains(tag)

                    AssistChip(
                        onClick = {
                            selectedTags = if (isSelected) {
                                selectedTags - tag
                            } else {
                                selectedTags + tag
                            }
                            viewModel.updateTags(selectedTags.joinToString(", "))
                        },
                        label = { Text(tag) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (isSelected)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = if (isSelected)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
            // ------------------------------

            Spacer(modifier = Modifier.height(16.dp))

            Text("Rating: ${formState.rating.toInt()} stars")
            Slider(
                value = formState.rating,
                onValueChange = { viewModel.updateRating(it) },
                valueRange = 0f..5f,
                steps = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    scope.launch {
                        if (viewModel.saveRestaurant()) {
                            navController.navigateUp()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEdit) "Update Restaurant" else "Save Restaurant")
            }
        }
    }
}
