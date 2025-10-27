package ca.gbc.comp3074.assignment.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurants ORDER BY name ASC")
    fun getAllRestaurants(): Flow<List<Restaurant>>

    @Query("SELECT * FROM restaurants WHERE id = :id")
    suspend fun getRestaurantById(id: Int): Restaurant?

    @Query("SELECT * FROM restaurants WHERE name LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%'")
    fun searchRestaurants(query: String): Flow<List<Restaurant>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurant(restaurant: Restaurant): Long

    @Update
    suspend fun updateRestaurant(restaurant: Restaurant)

    @Delete
    suspend fun deleteRestaurant(restaurant: Restaurant)
}
