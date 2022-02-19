package com.github.qoiu.wordly.firebase

import com.github.qoiu.wordly.Communication
import com.github.qoiu.wordly.Mapper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface MyDatabase {

    suspend fun <T> addListener(
        referenceName: String,
        mapper: Mapper.Data<DataSnapshot, DbResponse.Success<T>>,
        communication: Communication<DbResponse>
    )

    suspend fun <T> word(
        mapper: Mapper.Data<DataSnapshot, DbResponse.Success<T>>,
        communication: Communication<DbResponse>
    )

    suspend fun modify(player: Player)
    suspend fun remove(player: Player)

    class Base : MyDatabase {
        private val database by lazy {
            FirebaseDatabase.getInstance(DB_URL)
        }

        override suspend fun <T> addListener(
            referenceName: String,
            mapper: Mapper.Data<DataSnapshot, DbResponse.Success<T>>,
            communication: Communication<DbResponse>
        ): Unit = withContext(Dispatchers.IO) {
            try {
                val ref = database.getReference(referenceName)
                ref.addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            communication.provide(mapper.map(snapshot))
                        }

                        override fun onCancelled(error: DatabaseError) {
                            throw IllegalStateException("Canceled: ${error.message}")
                        }
                    })
            } catch (e: Exception) {
                communication.provide(DbResponse.Error(e))
            }
        }

        override suspend fun <T> word(
            mapper: Mapper.Data<DataSnapshot, DbResponse.Success<T>>,
            communication: Communication<DbResponse>
        ) {
            val task = database.getReference("words").get()
            task.addOnSuccessListener {
                communication.provide(mapper.map(it))
            }
        }

        override suspend fun modify(player: Player) {
            val ref = database.getReference(DB_PLAYERS).child("$DB_PLAYER ${player.id}")
            ref.child("id").setValue(player.id)
            ref.child("score").setValue(player.score)
        }

        override suspend fun remove(player: Player) {
            val ref = database.getReference(DB_PLAYERS).child("$DB_PLAYER ${player.id}")
            ref.removeValue()
        }

        companion object {
            private const val DB_URL =
                "https://wordly-4da10-default-rtdb.europe-west1.firebasedatabase.app"
            private const val DB_PLAYERS = "players"
            private const val DB_PLAYER = "player"
        }
    }
}