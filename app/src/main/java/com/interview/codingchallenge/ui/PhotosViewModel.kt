package com.interview.codingchallenge.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.interview.codingchallenge.PhotosUtil.DEBOUNCE_DURATION
import com.interview.codingchallenge.repository.PhotoRepo
import com.interview.codingchallenge.repository.PhotosRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit

class PhotosViewModel : BaseViewModel() {
    private val repo: PhotoRepo = PhotosRepository()

    private val mutablePhotosListLiveData = MutableLiveData<PhotosUiModel>()
    private val loaderLiveData = MutableLiveData<Pair<Boolean, Int>>()
    val photosListLiveData: LiveData<PhotosUiModel> = mutablePhotosListLiveData
    val photosLoadingLiveData: LiveData<Pair<Boolean, Int>> = loaderLiveData

    private var searchQuery = SearchQuery("", 1)
    private val searchObs = PublishSubject.create<SearchQuery>()

    init {
        searchObs
            .debounce(DEBOUNCE_DURATION, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .filter {
                return@filter !it.query.isEmpty()
            }
            .distinctUntilChanged()
            .doOnNext {
                loaderLiveData.postValue(Pair(true, it.pageNumber))
            }
            .switchMap { query ->
                repo.fetchSearchImages(query.query, query.pageNumber).toObservable()
                    .subscribeOn(Schedulers.io())
            }
            .subscribeOn(Schedulers.io())
            .subscribe({
                loaderLiveData.postValue(Pair(false, it.photos.page))
                mutablePhotosListLiveData.postValue(PhotosUiModel(
                    page = it.photos.page,
                    pages = it.photos.pages,
                    photo = it.photos.photo.map { photo ->
                        Photo(
                            id = photo.id,
                            url = "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg",
                            title = photo.title
                        )
                    }
                ))
            }, {
                loaderLiveData.postValue(Pair(false, -1))
            }).addToCompositeDisposable()
    }

    fun updateSearchResult(query: String) {
        this.searchQuery = SearchQuery(query, 1)
        searchObs.onNext(searchQuery)
    }

    fun updateSearchPageNumber(pageNumber: Int) {
        this.searchQuery = searchQuery.copy(pageNumber = pageNumber)
        searchObs.onNext(searchQuery)
    }

}
