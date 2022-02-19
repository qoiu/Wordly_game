package com.github.qoiu.wordly

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.qoiu.wordly.firebase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val database by lazy { MyDatabase.Base() }
    private val data = Communication.Base<DbResponse>()
    private val words = Communication.Base<DbResponse>()

    fun observeWords(owner: LifecycleOwner, observer: Observer<DbResponse>) {
        words.observe(owner, observer)
    }

    fun init(owner: LifecycleOwner, observer: Observer<DbResponse>) {
        data.observe(owner, observer)
        viewModelScope.launch(Dispatchers.IO) {
            database.addListener("players", SnapshotToPlayersMapper(), data)
        }
    }


    fun getWord() {
        viewModelScope.launch(Dispatchers.IO) {
            database.word(SnapshotToWordMapper(), words)
        }
    }

    fun addValue() {
        viewModelScope.launch(Dispatchers.IO) {
            database.modify(Player(2, 125))
        }
    }
}