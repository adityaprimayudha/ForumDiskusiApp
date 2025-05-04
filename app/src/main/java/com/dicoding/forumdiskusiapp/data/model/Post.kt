package com.dicoding.forumdiskusiapp.data.model

data class Post(
    val id : Int,
    val title : String? = null,
    val body : String? = null,
    val userName : String
)
