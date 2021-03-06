package com.cpen391.flappyaccount.model.bean

import java.io.Serializable

/**
 *  User data class to represent a player/user
 *  @params:
 *      top_three_scores: we maintain the top 3 scores of the given user of history, it's 0,0,0 by default
 *
 *  @implements Serializable:
 *      so that we can pass user object through intent
 *
 *  @autho Yuefeng Zhao
 */
data class User(

    var fullName: String,

    var userName: String,

    var email: String,

    var phoneNo: String,

    var password: String,
    var current_score: Long = 0,
    var top_three_scores: List<Long> = listOf(0, 0, 0)
) : Serializable {
    constructor() : this("", "", "", "", "", 0, listOf(0, 0, 0))

    fun isNullUser(): Boolean {
        return this.userName == "" && this.password == ""
    }
}
