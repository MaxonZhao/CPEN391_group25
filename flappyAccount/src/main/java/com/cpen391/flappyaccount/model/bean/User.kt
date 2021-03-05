package com.cpen391.flappyaccount.model.bean

import java.io.Serializable

data class User(

    var fullName: String,

    var userName: String,

    var email: String,

    var phoneNo: String,

    var password: String,
    var current_score: Long = 0,
    var top_three_scores: List<Long> = listOf(1,2,3)
) : Serializable {
    constructor() : this("", "", "", "", "", 0, listOf(1,2,3))
    fun isNullUser() : Boolean {
        return this.userName == "" && this.password == ""
    }
}
