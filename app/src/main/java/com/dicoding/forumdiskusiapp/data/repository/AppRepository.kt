package com.dicoding.forumdiskusiapp.data.repository

import android.util.Log
import com.dicoding.forumdiskusiapp.data.model.Post
import com.dicoding.forumdiskusiapp.data.model.toPost
import com.dicoding.forumdiskusiapp.data.remote.response.PostItem
import com.dicoding.forumdiskusiapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppRepository {
    var dataPost = emptyList<Post>()
    fun getAllPosts(): Flow<List<Post>> = flow {
        val client = ApiConfig.getApiService()
        dataPost = coroutineScope {
            val result = client.getAllPosts()
            result.map { data ->
                async {
                    val user = client.getUserById(data.userId)
                    data.toPost(userName = user.name)
                }
            }.awaitAll()
        }
        emit(dataPost)
    }

    suspend fun addPost(newData: Post, userId: Int): Post {
        val post = PostItem(
            id = newData.id,
            title = newData.title,
            body = newData.body,
            userId = userId
        )
        try {
            ApiConfig.getApiService().createNewData(post)
        } catch (e: Exception) {
            Log.d("Error Post", "Failed :" + e.message)
        }
        return newData
    }


    companion object {
        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(): AppRepository = instance ?: synchronized(this) {
            AppRepository().apply { instance = this }
        }
    }
}