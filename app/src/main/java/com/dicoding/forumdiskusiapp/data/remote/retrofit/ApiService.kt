package com.dicoding.forumdiskusiapp.data.remote.retrofit

import com.dicoding.forumdiskusiapp.data.remote.response.CommentItem
import com.dicoding.forumdiskusiapp.data.remote.response.PostItem
import com.dicoding.forumdiskusiapp.data.remote.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/posts")
    suspend fun getAllPosts() : List<PostItem>

    @GET("/posts/{id}/comments")
    suspend fun getPostComments(@Path("id") id: Int) : List<CommentItem>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int) : UserResponse

    @POST("/posts")
    suspend fun createNewData(post:PostItem)
}