package com.interview.codingchallenge.networking

import com.interview.codingchallenge.BuildConfig
import com.interview.codingchallenge.domain.data.PhotosSearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {

    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1&api_key=${BuildConfig.api_key}")
    @JvmSuppressWildcards
    fun fetchSearchImages(@QueryMap map: Map<String, Any>): Single<PhotosSearchResponse>
}
