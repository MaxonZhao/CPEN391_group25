package com.cpen391.moduledemo.model.response

data class GithubRepo(val id: Int,
                      val name: String,
                      val htmlUrl: String,
                      val description: String,
                      val language: String,
                      val stagezersCount: Int)