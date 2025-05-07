package com.dicoding.forumdiskusiapp.ui.screen.comments

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.forumdiskusiapp.data.model.Post
import com.dicoding.forumdiskusiapp.data.remote.response.CommentItem
import com.dicoding.forumdiskusiapp.di.Injection
import com.dicoding.forumdiskusiapp.di.ViewModelFactory
import com.dicoding.forumdiskusiapp.ui.common.UiState
import com.dicoding.forumdiskusiapp.ui.components.CommentComponent
import com.dicoding.forumdiskusiapp.ui.components.LoadingCircle
import com.dicoding.forumdiskusiapp.ui.components.PostComponent
import com.dicoding.forumdiskusiapp.ui.screen.posts.PostViewModel

@Composable
fun CommentScreen(
    modifier: Modifier = Modifier,
    id: Int,
    postViewModel: PostViewModel,
    viewModel: CommentViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    )
) {
    val postState by postViewModel.currentPost.collectAsState()
    val commentState by viewModel.commentState.collectAsState()

    when (commentState) {
        is UiState.Loading -> {
            postViewModel.getCurrentPost(id)
            viewModel.getAllComments(id)
            LoadingCircle()
        }

        is UiState.Success -> {
            val post = postState
            val comments = (commentState as UiState.Success<List<CommentItem>>).data
            CommentContent(
                post = post,
                comment = comments
            ) { comment ->
                viewModel.addComment(comment = comment)
            }
        }

        is UiState.Error -> {
            Log.d("Comments error", (commentState as UiState.Error).message)
        }
    }

}

@Composable
fun CommentContent(
    modifier: Modifier = Modifier,
    post: Post,
    comment: List<CommentItem>,
    addComment: (CommentItem) -> Unit
) {
    var inputComment by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var commentId by rememberSaveable { mutableIntStateOf(101) }
    val listState = rememberLazyListState()

    LaunchedEffect(comment.size) {
        if(comment.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Scaffold(
        bottomBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
                    .fillMaxWidth()
                    .imePadding()
                    .navigationBarsPadding()
            ) {
                OutlinedTextField(
                    value = inputComment,
                    onValueChange = {
                        inputComment = it
                    },
                    label = {
                        Text("Comment...")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                )
                IconButton(
                    onClick = {
                        val newComment = CommentItem(
                            name = "Aditya",
                            postId = post.id,
                            id = commentId,
                            body = inputComment,
                            email = "adityaprimayudha91@gmail.com"
                        )
                        inputComment = ""
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        addComment(newComment)
                        commentId++
                    },
                    enabled = inputComment.isNotEmpty()
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            PostComponent(
                title = post.title.toString(),
                body = post.body.toString(),
                userName = post.userName
            )
            Text(
                text = "Comment",
                fontSize = 21.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
            )
            LazyColumn(
                state = listState
            ) {
                items(comment, key = { it.id }) { comment ->
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .animateItem(placementSpec = tween(durationMillis = 100))
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(3.dp, Color.Black, CircleShape)
                                .size(32.dp)
                                .background(Color.Yellow)
                        ) {
                        }
                        CommentComponent(
                            name = comment.name.toString(),
                            email = comment.email.toString(),
                            body = comment.body.toString()
                        )
                    }
                }
            }
        }
    }
}