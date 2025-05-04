package com.dicoding.forumdiskusiapp.ui.navigation

sealed class Screen(val route: String) {
    data object Post: Screen("post")
    data object Comment: Screen("post/{id}") {
        fun createRoute(id: Int) = "post/$id"
    }
}