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
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dicoding.forumdiskusiapp.data.model.Post
import com.dicoding.forumdiskusiapp.ui.common.UiState
import com.dicoding.forumdiskusiapp.ui.components.LoadingCircle
import com.dicoding.forumdiskusiapp.ui.components.PostComponent

@Composable
fun PostScreen(
    modifier: Modifier = Modifier,
    viewModel: PostViewModel,
    navigateToComment: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var postId by rememberSaveable { mutableIntStateOf(101) }
    var contentIsError by remember { mutableStateOf(false) }
    var titleIsError by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    when (uiState) {
        is UiState.Loading -> {
            viewModel.getAllPosts()
            LoadingCircle()
        }

        is UiState.Success -> {
            val posts = (uiState as UiState.Success<List<Post>>).data
            PostContent(
                modifier = modifier,
                posts = posts,
                addToPost = { post, userId ->
                    viewModel.addPost(post, userId)
                    title = ""
                    content = ""
                    postId++
                },
                navigateToComment = navigateToComment,
                onTitleChanged = { newTitle -> title = newTitle },
                onContentChange = { newContent -> content = newContent },
                changeTitleError = {isError -> titleIsError = isError},
                changeContentError = {isError -> contentIsError = isError},
                title = title,
                content = content,
                postId = postId,
                contentIsError = contentIsError,
                titleIsError = titleIsError,
                listState = listState,
                focusManager = focusManager,
                keyboardController = keyboardController
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
    navigateToComment: (Int) -> Unit,
    onTitleChanged: (String) -> Unit,
    onContentChange: (String) -> Unit,
    title: String,
    changeTitleError: (Boolean) -> Unit,
    changeContentError: (Boolean) -> Unit,
    content: String,
    postId: Int,
    contentIsError: Boolean,
    titleIsError: Boolean,
    listState: LazyListState,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?
) {

    LaunchedEffect(posts.size) {
        if (posts.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChanged,
            label = {
                Text("Title")
            },
            isError = titleIsError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        OutlinedTextField(
            value = content,
            onValueChange = onContentChange,
            label = {
                Text("Content")
            },
            isError = contentIsError,
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
                if (title.isEmpty()) {
                    changeTitleError(true)
                } else if (content.isEmpty()) {
                    changeContentError(true)
                } else {
                    addToPost(post, 11)
                    changeTitleError(false)
                    changeContentError(false)
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
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
