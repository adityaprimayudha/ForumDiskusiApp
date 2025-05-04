package com.dicoding.forumdiskusiapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.forumdiskusiapp.data.repository.AppRepository
import com.dicoding.forumdiskusiapp.ui.screen.comments.CommentViewModel
import com.dicoding.forumdiskusiapp.ui.screen.posts.PostViewModel

class ViewModelFactory(private val repository: AppRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PostViewModel::class.java)) {
            return PostViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(CommentViewModel::class.java)) {
            return CommentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown viewModel :"+modelClass.name)
    }
}