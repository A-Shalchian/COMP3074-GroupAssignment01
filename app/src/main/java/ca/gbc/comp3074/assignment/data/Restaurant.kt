package ca.gbc.comp3074.assignment.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class Restaurant(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val address: String,
    val phone: String,
    val description: String,
    val tags: String,
    val rating: Float
)
