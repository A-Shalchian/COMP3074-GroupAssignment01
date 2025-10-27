package ca.gbc.comp3074.assignment.data

import kotlinx.coroutines.flow.Flow

class RestaurantRepository(private val restaurantDao: RestaurantDao) {
    val allRestaurants: Flow<List<Restaurant>> = restaurantDao.getAllRestaurants()

    fun searchRestaurants(query: String): Flow<List<Restaurant>> {
        return restaurantDao.searchRestaurants(query)
    }

    suspend fun getRestaurant(id: Int): Restaurant? {
        return restaurantDao.getRestaurantById(id)
    }

    suspend fun insert(restaurant: Restaurant): Long {
        return restaurantDao.insertRestaurant(restaurant)
    }

    suspend fun update(restaurant: Restaurant) {
        restaurantDao.updateRestaurant(restaurant)
    }

    suspend fun delete(restaurant: Restaurant) {
        restaurantDao.deleteRestaurant(restaurant)
    }
}
