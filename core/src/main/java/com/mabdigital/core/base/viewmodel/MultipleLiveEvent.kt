package com.mabdigital.core.base.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class MultipleLiveEvent<T> : MutableLiveData<T>() {
    private val mPending = AtomicBoolean(false)
    private val values: Queue<T?> = LinkedList()
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) {data :T? ->
            if(mPending.compareAndSet(true,false)) {
                observer.onChanged(data)
                if(values.isNotEmpty())
                    pollValue()
            }
        }
    }

    override fun postValue(value: T?) {
        CoroutineScope(Main).launch {
            setValue(value)
        }
    }

    private fun pollValue() {
        super.postValue(values.poll())
    }

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

}