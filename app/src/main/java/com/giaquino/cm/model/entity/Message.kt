package com.giaquino.cm.model.entity

import com.google.gson.annotations.SerializedName

data class Message(
        @SerializedName("success") val success: List<String>?,
        @SerializedName("error") val error: List<String>?)