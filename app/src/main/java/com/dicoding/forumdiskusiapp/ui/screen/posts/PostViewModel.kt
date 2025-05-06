package com.dicoding.forumdiskusiapp.ui.screen.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.forumdiskusiapp.data.model.Post
import com.dicoding.forumdiskusiapp.data.repository.AppRepository
import com.dicoding.forumdiskusiapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PostViewModel(val repository: AppRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<Post>>> =
        MutableStateFlow(UiState.Loading)
    val uiState get() = _uiState.asStateFlow()

    private var _currentPost : MutableStateFlow<Post> = MutableStateFlow(Post(-1, "","",""))
    val currentPost get() = _currentPost

    fun getAllPosts() {
        viewModelScope.launch {
            repository.getAllPosts()
                .catch { e ->
                    _uiState.value = UiState.Error(e.message.toString())
                }
                .collect { data ->
                    _uiState.value = UiState.Success(data)
                }
        }
    }

    fun getCurrentPost(id: Int) {
        if(_uiState.value is UiState.Success) {
            val result = (_uiState.value as UiState.Success<List<Post>>).data.find { it.id == id }
            if (result != null) {
                _currentPost.value = result
            }
        }
    }

    fun addPost(post: Post, userId: Int) {
        viewModelScope.launch {
            val newPost = repository.addPost(post, userId)
            val currentState = _uiState.value
            if (currentState is UiState.Success) {
                val currentList = currentState.data
                val updatedList = listOf(newPost) + currentList
                _uiState.value = UiState.Success(updatedList)
            }
        }
    }
}