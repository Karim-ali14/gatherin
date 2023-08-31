package com.orwa.gatherin.model.chat


data class Question(
    val id: Int,
    val type: String,
    val body: String,
    val isAnswer:Boolean,
    val options: List<Option>?

)