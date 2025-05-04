package com.dicoding.forumdiskusiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.dicoding.forumdiskusiapp.ui.screen.posts.PostScreen
import com.dicoding.forumdiskusiapp.ui.theme.ForumDiskusiAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ForumDiskusiAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PostScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}