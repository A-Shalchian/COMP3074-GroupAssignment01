package ca.gbc.comp3074.assignment.navigation

sealed class Screen(val route: String) {
    object WelcomeScreen : Screen("welcome_screen")
    object RestaurantList : Screen("restaurant_list")
    object RestaurantDetails : Screen("restaurant_details/{restaurantId}") {
        fun createRoute(restaurantId: String) = "restaurant_details/$restaurantId"
    }
    object AddEditRestaurant : Screen("add_edit_restaurant?restaurantId={restaurantId}") {
        fun createRoute(restaurantId: String? = null) =
            if (restaurantId != null) "add_edit_restaurant?restaurantId=$restaurantId"
            else "add_edit_restaurant"
    }
    object MapScreen : Screen("map/{restaurantId}") {
        fun createRoute(restaurantId: String) = "map/$restaurantId"
    }
    object About : Screen("about")
}
