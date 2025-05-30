package com.dicoding.forumdiskusiapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class PostItem(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("body")
	val body: String? = null,

	@field:SerializedName("userId")
	val userId: Int
)
