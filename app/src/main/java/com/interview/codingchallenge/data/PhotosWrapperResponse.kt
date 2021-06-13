package com.interview.codingchallenge.domain.data

// The outermost wrapper for the api response
data class PhotosSearchResponse(
    val photos: PhotosMetaData
)

data class PhotosMetaData(
    val page: Int,
    val pages:Int,
    val perpage:Int,
    val total:Int,
    val photo: List<PhotoResponse>
)

data class PhotoResponse(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String
)
