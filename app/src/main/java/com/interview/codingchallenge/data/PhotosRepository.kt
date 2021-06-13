package com.interview.codingchallenge.data

import com.interview.codingchallenge.domain.data.PhotosSearchResponse
import com.interview.codingchallenge.repository.PhotoRepo
import io.reactivex.Single

class PhotosRepository : PhotoRepo {

    override fun fetchSearchImages(query: String, pageNumber: Int): Single<PhotosSearchResponse> {
        return super.fetchSearchImages(query, pageNumber)
    }
}