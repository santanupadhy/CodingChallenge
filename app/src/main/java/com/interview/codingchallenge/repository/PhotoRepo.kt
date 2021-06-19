package com.interview.codingchallenge.repository

import com.interview.codingchallenge.PhotosUtil
import com.interview.codingchallenge.domain.data.PhotosSearchResponse
import com.interview.codingchallenge.networking.WebClient
import io.reactivex.Single

interface PhotoRepo {

    fun fetchSearchImages(query: String, pageNumber: Int): Single<PhotosSearchResponse>
}