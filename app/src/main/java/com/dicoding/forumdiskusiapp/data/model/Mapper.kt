package com.dicoding.forumdiskusiapp.data.model

import com.dicoding.forumdiskusiapp.data.remote.response.PostItem

fun PostItem.toPost(userName : String) : Post {
    return Post(
        id = this.id,
        title = this.title,
        body = this.body,
        userName = userName
    )
}