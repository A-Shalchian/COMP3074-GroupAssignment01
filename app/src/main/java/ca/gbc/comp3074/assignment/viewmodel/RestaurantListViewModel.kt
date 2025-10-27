package ca.gbc.comp3074.assignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.assignment.data.Restaurant
import ca.gbc.comp3074.assignment.data.RestaurantRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RestaurantListViewModel(
    private val repository: RestaurantRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val restaurants: StateFlow<List<Restaurant>> = searchQuery
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.allRestaurants
            } else {
                repository.searchRestaurants(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun deleteRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            repository.delete(restaurant)
        }
    }
}
