package com.dicoding.forumdiskusiapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dicoding.forumdiskusiapp.di.Injection
import com.dicoding.forumdiskusiapp.di.ViewModelFactory
import com.dicoding.forumdiskusiapp.ui.components.TopBar
import com.dicoding.forumdiskusiapp.ui.navigation.Screen
import com.dicoding.forumdiskusiapp.ui.screen.comments.CommentScreen
import com.dicoding.forumdiskusiapp.ui.screen.posts.PostScreen
import com.dicoding.forumdiskusiapp.ui.screen.posts.PostViewModel

@Composable
fun ForumDiskusiApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    postViewModel: PostViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    )
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
                        navController.navigate(route = Screen.Comment.createRoute(id))
                    },
                    viewModel = postViewModel
                )
            }

            composable(
                route = Screen.Comment.route,
                arguments = listOf(navArgument("id") {
                    type = NavType.IntType
                }),
            ) {
                val id = it.arguments?.getInt("id") ?: 0
                CommentScreen(id = id, postViewModel = postViewModel)
            }
        }
    }

}