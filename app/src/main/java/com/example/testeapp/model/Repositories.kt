package com.example.testeapp.model

data class Repositories (
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<Post>
    )