package com.dicoding.forumdiskusiapp.ui.screen.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.forumdiskusiapp.data.remote.response.CommentItem
import com.dicoding.forumdiskusiapp.data.repository.AppRepository
import com.dicoding.forumdiskusiapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CommentViewModel(val repository: AppRepository) : ViewModel() {
    /*private val _post: MutableStateFlow<Post> = MutableStateFlow(Post(id = 0, userName = ""))
    val post get() = _post.asStateFlow()*/

    private val _commentState: MutableStateFlow<UiState<List<CommentItem>>> =
        MutableStateFlow(UiState.Loading)
    val commentState get() = _commentState.asStateFlow()

    /*fun getThePost(id: Int) {
        viewModelScope.launch {
            repository.getPostById(id)
                .catch { e ->
                    _post.value = Post(id = 0, userName = "")
                }
                .collect { post ->
                    _post.value = post
                }
        }
    }*/

    fun getAllComments(id: Int) {
        viewModelScope.launch {
            repository.getAllCommentOfAPost(id)
                .catch { e ->
                    _commentState.value = UiState.Error(e.message.toString())
                }
                .collect { data ->
                    _commentState.value = UiState.Success(data)
                }
        }
    }

    fun addComment(comment: CommentItem) {
        viewModelScope.launch {
            val newPost = repository.addComment(comment)
            val currentState = _commentState.value
            if (currentState is UiState.Success) {
                val currentList = currentState.data
                val updatedList = listOf(newPost) + currentList
                _commentState.value = UiState.Success(updatedList)
            }
        }
    }

}