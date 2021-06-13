package com.interview.codingchallenge.ui

data class PhotosUiModel(
    val page: Int,
    val pages: Int,
    val photo: List<Photo>
)

data class Photo(
    val id: String,
    val url: String,
    val title: String
)