package com.github.qoiu.wordly

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.github.qoiu.wordly.firebase.DbResponse
import com.github.qoiu.wordly.ui.theme.WordlyTheme

class MainActivity : ComponentActivity() {
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MainActivityViewModel()
        viewModel.init(this,{
            if(it is DbResponse.Success<*>)
                Log.w("result",it.list.toString())
        })
        setContent {
            WordlyTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting(viewModel,"Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(viewModel: MainActivityViewModel,name: String) {
    Button(onClick = {
        viewModel.addValue()
    }) {
        Text("Hello")
    }
}
