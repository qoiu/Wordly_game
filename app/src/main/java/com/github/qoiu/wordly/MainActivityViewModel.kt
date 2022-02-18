package com.github.qoiu.wordly

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.qoiu.wordly.firebase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val database by lazy { MyDatabase.Base()}
    private val data = PlayerCommunication()

    fun init(owner: LifecycleOwner,observer: Observer<DbResponse>) {
        data.observe(owner,observer)

        viewModelScope.launch(Dispatchers.IO) {
            database.addListener("players", SnapshotToPlayersMapper(), data)
        }
    }

    fun addValue() {
        viewModelScope.launch(Dispatchers.IO) {
            database.modify(Player(2,125))
        }
    }
}