package com.interview.codingchallenge.repository

import com.interview.codingchallenge.PhotosUtil
import com.interview.codingchallenge.domain.data.PhotosSearchResponse
import com.interview.codingchallenge.networking.WebClient
import io.reactivex.Single

class PhotosRepository : PhotoRepo {

    override fun fetchSearchImages(query: String, pageNumber: Int): Single<PhotosSearchResponse> {
        return WebClient.client.fetchSearchImages(
            hashMapOf(
                "text" to query,
                "page" to pageNumber,
                "per_page" to PhotosUtil.PAGE_SIZE
            )
        )
    }

}