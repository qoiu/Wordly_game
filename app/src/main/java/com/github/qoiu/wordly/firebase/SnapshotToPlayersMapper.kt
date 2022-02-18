package com.github.qoiu.wordly.firebase

import android.util.Log
import com.github.qoiu.wordly.Mapper
import com.google.firebase.database.DataSnapshot

class SnapshotToPlayersMapper : Mapper.Data<DataSnapshot, DbResponse.Success<List<Player>>> {
    override fun map(data: DataSnapshot): DbResponse.Success<List<Player>> {
        Log.w("fire", data.childrenCount.toString())
        Log.w("fire", data.children.toString())
        data.children.forEach{
            Log.w("fire", it.child("id").value?.toString()?:"noValue")
            Log.w("fire", it.child("score").value.toString())
        }
        val list =data.children
        val result = mutableListOf<Player>()
        list.forEach {
            val id = it.child("id").value
            val score = it.child("score").value
            if(id != null && score !=null)
                try {
                    result.add(Player(id.toString().toInt(),score.toString().toInt()))
                }catch (e: Exception){
                    Log.w("fire", e.toString())
                }
        }
        return DbResponse.Success(result)
    }
}