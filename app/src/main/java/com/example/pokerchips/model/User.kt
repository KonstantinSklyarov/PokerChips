package com.example.pokerchips.model

data class User(
    val id: Long,
    val photo: String,
    var name: String,
    val chipCount: Int,
    var selectWinner: Boolean
)