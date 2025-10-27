package ca.gbc.comp3074.assignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.assignment.data.Restaurant
import ca.gbc.comp3074.assignment.data.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RestaurantFormState(
    val id: Int = 0,
    val name: String = "",
    val address: String = "",
    val phone: String = "",
    val description: String = "",
    val tags: String = "",
    val rating: Float = 0f
)

class AddEditRestaurantViewModel(
    private val repository: RestaurantRepository
) : ViewModel() {

    private val _formState = MutableStateFlow(RestaurantFormState())
    val formState: StateFlow<RestaurantFormState> = _formState.asStateFlow()

    fun loadRestaurant(id: Int) {
        viewModelScope.launch {
            repository.getRestaurant(id)?.let { restaurant ->
                _formState.value = RestaurantFormState(
                    id = restaurant.id,
                    name = restaurant.name,
                    address = restaurant.address,
                    phone = restaurant.phone,
                    description = restaurant.description,
                    tags = restaurant.tags,
                    rating = restaurant.rating
                )
            }
        }
    }

    fun updateName(name: String) {
        _formState.value = _formState.value.copy(name = name)
    }

    fun updateAddress(address: String) {
        _formState.value = _formState.value.copy(address = address)
    }

    fun updatePhone(phone: String) {
        _formState.value = _formState.value.copy(phone = phone)
    }

    fun updateDescription(description: String) {
        _formState.value = _formState.value.copy(description = description)
    }

    fun updateTags(tags: String) {
        _formState.value = _formState.value.copy(tags = tags)
    }

    fun updateRating(rating: Float) {
        _formState.value = _formState.value.copy(rating = rating)
    }

    suspend fun saveRestaurant(): Boolean {
        val state = _formState.value
        if (state.name.isBlank() || state.address.isBlank()) {
            return false
        }

        val restaurant = Restaurant(
            id = state.id,
            name = state.name,
            address = state.address,
            phone = state.phone,
            description = state.description,
            tags = state.tags,
            rating = state.rating
        )

        return try {
            if (state.id == 0) {
                repository.insert(restaurant)
            } else {
                repository.update(restaurant)
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
