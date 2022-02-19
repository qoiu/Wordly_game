package com.github.qoiu.wordly

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.qoiu.wordly.firebase.DbResponse
import com.github.qoiu.wordly.ui.theme.WordlyTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MainActivityViewModel()
        viewModel.observeWords(this, {
            if (it is DbResponse.Success<*>)
                Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT).show()
        })
        viewModel.init(this, {
            if (it is DbResponse.Success<*>)
                Log.w("result", it.data.toString())
        })
        setContent {
            WordlyTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        val state = rememberSaveable { mutableStateOf(3) }
        if (state.value == 1)
            FirstScreen(state)
        if (state.value == 2)
            Greeting(state)
        if (state.value == 3)
            Game()
    }

    @Composable
    fun Header() {
        val playerName = rememberSaveable {
            mutableStateOf("Player")
        }
        val textInput = rememberSaveable { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .height(50.dp)
                .fillMaxWidth()
        ) {
            TextField(
                value = playerName.value,
                modifier = Modifier,
                singleLine = true,
                onValueChange = { text ->
                    playerName.value = text
                    textInput.value = true
                },
                textStyle = TextStyle(color = MaterialTheme.colors.secondaryVariant)
            )
            if (textInput.value) {
                Button(onClick = { textInput.value = false }) {
                    Text(text = "Ok")
                }
            }
        }
    }

    @Composable
    fun FirstScreen(state: MutableState<Int>) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { state.value = 3 }, modifier = Modifier.padding(10.dp)) {
                Text(text = "New game")
            }
            Button(onClick = { state.value = 2 }, modifier = Modifier.padding(10.dp)) {
                Text(text = "Connect")
            }
            Button(onClick = { this@MainActivity.finish() }, modifier = Modifier.padding(10.dp)) {
                Text(text = "Exit")
            }
        }
    }

    @Composable
    fun Greeting(state: MutableState<Int>) {
        Button(onClick = {
            viewModel.getWord()
            state.value = 1
        }) {
            Text("Hello")
        }
    }

    @Composable
    fun Game() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(ScrollState(0)),
            verticalArrangement = Arrangement.Top
        ) {
            Header()
            LetterRow(amount = 5)
            LetterRow(amount = 5)
            LetterRow(amount = 5)
            LetterRow(amount = 5)
        }
        GameKeyboard()
    }

    @Composable
    fun LetterRow(amount: Int) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            val width = LocalConfiguration.current.screenWidthDp
            for (i in 0 until amount) {
                Letter(letter = "A", width / 5)
            }
        }
    }

    @Composable
    fun Letter(letter: String = "", size: Int = 100) {
        Card(
            modifier = Modifier
                .height(size.dp)
                .width(size.dp)
                .padding(10.dp), elevation = 5.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = letter,
                    textAlign = TextAlign.Center,
                    fontSize = size.dpToSp
                )
            }
        }
    }

    @Composable
    fun GameKeyboard() {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray)
            ) {
                KeyboardTop(listOf("Й", "Ц", "У", "К", "Е", "Н", "Г", "Ш", "Щ", "З", "Х", "Ъ"))
                KeyboardMid(listOf("Ф", "Ы", "В", "А", "П", "Р", "О", "Л", "Д", "Ж", "Э"))
                KeyboardBot(listOf("Я", "Ч", "С", "М", "И", "Т", "Ь", "Б", "Ю"))
            }
        }
    }

    @Composable
    fun KeyboardTop(letters: List<String>) {
        Row(Modifier.fillMaxWidth()) {
            val size = (LocalConfiguration.current.screenWidthDp) / (letters.size)
            letters.forEach { KeyboardKey(size, it) }
        }
    }

    @Composable
    fun KeyboardMid(letters: List<String>) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            val size = (LocalConfiguration.current.screenWidthDp) / (letters.size + 1)
            letters.forEach { KeyboardKey(size, it) }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    "Back"
                )
            }
        }
    }

    @Composable
    fun KeyboardBot(letters: List<String>) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            val size = (LocalConfiguration.current.screenWidthDp) / (letters.size + 3)
            letters.forEach {
                KeyboardKey(size, it)
            }
            Button(
                onClick = { }, modifier = Modifier
                    .absoluteOffset()
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(text = "Ввод")
            }
        }
    }

    @Composable
    fun KeyboardKey(size: Int, value: String) {
        Card(
            modifier = Modifier
                .height((size * 1.4).dp)
                .width(size.dp)
                .padding(2.dp)
                .clickable {
                    Toast
                        .makeText(this@MainActivity, value, Toast.LENGTH_SHORT)
                        .show()
                }
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = value,
                    textAlign = TextAlign.Center,
                    fontSize = size.dpToSp
                )
            }
        }
    }

    private inline val Int.dpToSp
        get(): TextUnit {
            val sp = resources.displayMetrics.density
            return (this / sp).sp
        }
}

