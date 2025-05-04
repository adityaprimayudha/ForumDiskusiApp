package com.dicoding.forumdiskusiapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dicoding.forumdiskusiapp.ui.components.TopBar
import com.dicoding.forumdiskusiapp.ui.navigation.Screen
import com.dicoding.forumdiskusiapp.ui.screen.comments.CommentScreen
import com.dicoding.forumdiskusiapp.ui.screen.posts.PostScreen

@Composable
fun ForumDiskusiApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(navController = navController)
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Post.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Post.route) {
                PostScreen(
                    navigateToComment = { id ->
                        navController.navigate(Screen.Comment.createRoute(id))
                    }
                )
            }

            composable(
                route = Screen.Comment.route,
                arguments = listOf(navArgument("id") {
                    type = NavType.IntType
                }),
            ) {
                val id = it.arguments?.getInt("id") ?: 0
                CommentScreen(id = id)
            }
        }
    }

}