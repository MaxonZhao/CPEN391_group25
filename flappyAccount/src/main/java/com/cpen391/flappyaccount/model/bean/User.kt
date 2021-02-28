package com.cpen391.flappyaccount.model.bean

data class User(
    var fullName: String,
    var userName: String,
    var email: String,
    var phoneNo: String,
    var password: String,
    var current_score: Int = 0,
    var top_three_scores: List<Int> = listOf(1,2,3)
)
