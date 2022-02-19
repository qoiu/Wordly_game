package com.github.qoiu.wordly.firebase

import com.github.qoiu.wordly.Mapper
import com.google.firebase.database.DataSnapshot

class SnapshotToWordMapper : Mapper.Data<DataSnapshot, DbResponse.Success<String>> {
    override fun map(data: DataSnapshot): DbResponse.Success<String> {
        val list = data.children
        val result = mutableListOf<String>()
        list.forEach {
            result.add(it.value.toString())
        }
        return DbResponse.Success(result[(0 until result.size).random()])
    }
}