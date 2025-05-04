package com.dicoding.forumdiskusiapp.ui.screen.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dicoding.forumdiskusiapp.data.model.Post
import com.dicoding.forumdiskusiapp.data.remote.response.CommentItem
import com.dicoding.forumdiskusiapp.ui.components.CommentComponent
import com.dicoding.forumdiskusiapp.ui.components.PostComponent
import com.dicoding.forumdiskusiapp.ui.theme.ForumDiskusiAppTheme

@Composable
fun CommentScreen(modifier: Modifier = Modifier, post: Post, comment: CommentItem) {

    var inputComment by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var commentId by rememberSaveable { mutableStateOf(101) }

    Scaffold(
        bottomBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
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
                        val comment = CommentItem(
                            name = "Aditya",
                            postId = 1111,
                            id = 11111,
                            body = inputComment,
                            email = "adityaprimayudha91@gmail.com"
                        )
                        inputComment = ""
                        focusManager.clearFocus()
                        keyboardController?.hide()
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
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
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
                    body = comment.body.toString()
                )
            }
        }
    }

}

@Composable
fun CommentContent(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true)
@Composable
private fun CommentScreenPrev() {
    ForumDiskusiAppTheme {
        CommentScreen(
            post = Post(
                id = 1,
                title = "2e2e2e2",
                body = "AWDJOIAWDJOAIDJIOASDJWIOAJDIOWAJDIOJADIOAWDJIOSAIOJWDIOASJDIOAWJDIOAJDIOAWJDIOASJDIOAWJDIOAJWDIOJASIODJAWOIDJAWIODJIAw",
                userName = "ADIATA"
            ),
            comment = CommentItem(
                name = "Aditya",
                postId = 191,
                id = 191,
                body = "AJWODIJWAIODJAWDIOWJADIOJWADOIAWJDWIOADJOAI",
                email = "Aditaprimayduabwd81@gmail.com"
            )
        )
    }
}