package com.interview.codingchallenge.ui

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel:ViewModel(){

    val compositeDisposable=CompositeDisposable()

    override fun onCleared() {
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
        super.onCleared()
    }

    fun Disposable.addToCompositeDisposable(){
        compositeDisposable.add(this)
    }

}