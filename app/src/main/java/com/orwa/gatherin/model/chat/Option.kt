package com.orwa.gatherin.model.chat


data class Option(
    val id: Int,
    val body: String,
    var counter: Int = 0,
    var selected: Boolean
)