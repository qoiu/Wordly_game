package com.github.qoiu.wordly

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface Mapper {
    interface Data<I, O> : Mapper {
        fun map(data: I): O
    }

    interface Object<I, O> : Mapper {
        fun map(mapper: Data<I, O>): O

    }
}

interface Provide<T> {
    fun provide(data: T)
}

interface Observe<T> {
    fun observe(owner: LifecycleOwner, observer: Observer<T>)
}

interface Communication<T> : Provide<T>, Observe<T> {

    open class Base<T : Any> : Communication<T> {
        private val liveData = MutableLiveData<T>()
        override fun provide(data: T) {
            liveData.postValue(data)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(owner, observer)
        }
    }
}