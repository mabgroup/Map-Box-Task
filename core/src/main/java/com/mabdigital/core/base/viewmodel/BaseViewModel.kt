package com.mabdigital.core.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

abstract class BaseViewModel<STATE : BaseState, VME : BaseViewModelEvent>
    :ViewModel(){

    protected fun CoroutineScope.safeLaunch(launchBody: suspend () -> Unit): Job {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }

        return this.launch(Dispatchers.IO + coroutineExceptionHandler) {
            launchBody.invoke()
        }
    }

    protected val state = MultipleLiveEvent<STATE>()

    fun getState(): LiveData<STATE> = state

    abstract fun onEvent(uiEvent: VME)
}