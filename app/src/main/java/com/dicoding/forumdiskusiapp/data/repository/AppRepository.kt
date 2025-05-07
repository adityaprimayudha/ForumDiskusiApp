package com.dicoding.forumdiskusiapp.data.repository

import android.util.Log
import com.dicoding.forumdiskusiapp.data.model.Post
import com.dicoding.forumdiskusiapp.data.model.toPost
import com.dicoding.forumdiskusiapp.data.remote.response.CommentItem
import com.dicoding.forumdiskusiapp.data.remote.response.PostItem
import com.dicoding.forumdiskusiapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AppRepository {
    var dataPost = emptyList<Post>()
    var dataComment = emptyList<CommentItem>()
    val client = ApiConfig.getApiService()

    fun getAllPosts(): Flow<List<Post>> = flow {
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
    }.flowOn(Dispatchers.IO)

    fun getPostById(id: Int) : Flow<Post> = flow {
        val post = coroutineScope {
            val result = client.getPostById(id)
            async {
                val user = client.getUserById(result.userId)
                result.toPost(userName = user.name)
            }.await()
        }
        emit(post)
    }.flowOn(Dispatchers.IO)

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

    suspend fun addComment(newData: CommentItem) : CommentItem {
        try {
            ApiConfig.getApiService().createNewComments(newData)

        } catch (e:Exception) {
            Log.d("Error Comment", "Failed :" + e.message)
        }
        return newData
    }

    fun getAllCommentOfAPost(id: Int): Flow<List<CommentItem>> = flow {
        dataComment = client.getPostComments(id)
        emit(dataComment)
    }.flowOn(Dispatchers.IO)


    companion object {
        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(): AppRepository = instance ?: synchronized(this) {
            AppRepository().apply { instance = this }
        }
    }
}