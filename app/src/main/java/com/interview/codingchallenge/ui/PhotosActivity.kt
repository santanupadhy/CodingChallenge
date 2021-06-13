package com.interview.codingchallenge.ui

import android.app.SearchManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interview.codingchallenge.PhotosUtil.GRID_SPAN_COUNT
import com.interview.codingchallenge.PhotosUtil.VISIBLE_THRESHOLD
import com.interview.codingchallenge.R
import com.interview.codingchallenge.ui.RxSearchObservable.fromView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class PhotosActivity : AppCompatActivity() {
    private val compositeDisposable = CompositeDisposable()
    private val adapterSearchPhoto by lazy {
        AdapterSearchPhoto()
    }
    private val photosViewModel: PhotosViewModel by viewModels()

    private var isLoading = false
    private var itemTotalPage: Int = 0
    private var itemCurrentPage: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ed_search.apply {
            setSearchableInfo(
                (getSystemService(SEARCH_SERVICE) as SearchManager).getSearchableInfo(
                    componentName
                )
            )
            setIconifiedByDefault(false)
            isFocusable = true
            isIconified = false
            requestFocusFromTouch()
            requestFocus()
            queryHint = getString(R.string.et_search_hint)
            setOnQueryTextFocusChangeListener { view: View, hasFocus: Boolean ->
                if (hasFocus) {
                    showInputMethod(view.findFocus())
                }
            }
            setOnCloseListener {
                ed_search.onActionViewCollapsed()
                false
            }
        }
        rv_photo_list.apply {
            layoutManager = GridLayoutManager(this@PhotosActivity, GRID_SPAN_COUNT)
            adapter = adapterSearchPhoto
        }
        setUpSearchObservable()
        setUpRecyclerViewListener()
        subscribeForData()
    }

    private fun showInputMethod(view: View) {
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(view, 0)
    }

    private fun setUpSearchObservable() {
        compositeDisposable.add(fromView(ed_search).subscribe {
            photosViewModel.updateSearchResult(it)
        })
    }

    private fun setUpRecyclerViewListener() {
        rv_photo_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager as? GridLayoutManager ?: return
                val lastVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition() * GRID_SPAN_COUNT
                if (!isLoading
                    && lastVisibleItem >= adapterSearchPhoto.itemCount - VISIBLE_THRESHOLD * GRID_SPAN_COUNT
                    && itemCurrentPage < itemTotalPage
                ) {
                    photosViewModel.updateSearchPageNumber(itemCurrentPage + 1)
                }
            }
        })
    }

    private fun subscribeForData() {
        photosViewModel.photosListLiveData.observe(this, {
            itemCurrentPage = it.page
            itemTotalPage = it.pages
            adapterSearchPhoto.apply {
                if (it.page == 1)
                    setData(it.photo)
                else
                    addToLast(it.photo)
            }
        })

        photosViewModel.photosLoadingLiveData.observe(this, { (isLoading, pageNumber) ->
            this.isLoading = isLoading
            with(adapterSearchPhoto) {
                if (isLoading) {
                    if (pageNumber != 1)
                        addLoading()
                    else {
                        allClear()
                    }
                } else {
                    removeLoading()
                }
            }
        })
    }
}