package com.dicoding.forumdiskusiapp.ui.screen.posts

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.forumdiskusiapp.data.model.Post
import com.dicoding.forumdiskusiapp.di.Injection
import com.dicoding.forumdiskusiapp.di.ViewModelFactory
import com.dicoding.forumdiskusiapp.ui.common.UiState
import com.dicoding.forumdiskusiapp.ui.components.PostComponent

@Composable
fun PostScreen(
    modifier: Modifier = Modifier,
    viewModel: PostViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToComment: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is UiState.Loading -> {
            viewModel.getAllPosts()
        }

        is UiState.Success -> {
            val posts = (uiState as UiState.Success<List<Post>>).data
            PostContent(
                modifier = modifier,
                posts = posts,
                addToPost = { post, userId ->
                    viewModel.addPost(post, userId)
                },
                navigateToComment = navigateToComment
            )
        }

        is UiState.Error -> {
            Log.d("Posts error", (uiState as UiState.Error).message)
        }
    }
}

@Composable
fun PostContent(
    modifier: Modifier = Modifier,
    posts: List<Post>,
    addToPost: (Post, Int) -> Unit,
    navigateToComment: (Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var postId by rememberSaveable { mutableStateOf(101) }
    val listState = rememberLazyListState()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(posts.size) {
        if (posts.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },
            label = {
                Text("Title")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        OutlinedTextField(
            value = content,
            onValueChange = {
                content = it
            },
            label = {
                Text("Content")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Button(
            onClick = {
                val post = Post(
                    id = postId,
                    title = title,
                    body = content,
                    userName = "Aditya"
                )
                addToPost(post, 11)
                title = ""
                content = ""
                focusManager.clearFocus()
                keyboardController?.hide()
                postId++
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text("Post")
        }

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            state = listState
        ) {
            items(posts, key = { it.id }) { data ->
                PostComponent(
                    title = data.title.toString(),
                    body = data.body.toString(),
                    userName = data.userName,
                    modifier = Modifier
                        .animateItem(placementSpec = tween(durationMillis = 100))
                        .clickable {
                            navigateToComment(data.id)
                        }
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(height = 0.5.dp, width = Dp.Unspecified),
                    color = Color.Black
                )
            }
        }

    }
}
