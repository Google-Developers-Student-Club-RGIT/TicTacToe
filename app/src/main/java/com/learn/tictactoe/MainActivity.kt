package com.learn.tictactoe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learn.tictactoe.ui.theme.ForkActivity
import com.learn.tictactoe.ui.theme.GameWon

class MainActivity : ComponentActivity() {

    var PLAYER_ONE : Int = 1
    var PLAYER_TWO : Int = 2

    lateinit var currentPlayer : MutableState<Int>
    lateinit var boxValue : MutableState<Array<Array<String>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            currentPlayer = remember {
                mutableStateOf(PLAYER_ONE)
            }

            boxValue = remember {
                mutableStateOf(
                    arrayOf(
                        arrayOf("","",""),
                        arrayOf("","",""),
                        arrayOf("","","")
                    )
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if( currentPlayer.value == PLAYER_ONE ){
                    Text(
                        text = "Player One",
                        fontSize = 50.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }else{
                    Text(
                        text = "Player Two",
                        fontSize = 50.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                CreateGrid(boxValue.value) {
                    if( currentPlayer.value == PLAYER_ONE ){
                        boxValue.value[it[0]][it[1]] = "X"
                        if( isGameWon(currentPlayer.value) ) {
                            var intent : Intent = Intent(this@MainActivity, GameWon::class.java)
                            intent.putExtra("PLAYER",currentPlayer.value)
                            startActivity(intent)
                        }
                        currentPlayer.value = PLAYER_TWO
                    }else{
                        boxValue.value[it[0]][it[1]] = "O"
                        if( isGameWon(currentPlayer.value) ) {
                            var intent : Intent = Intent(this@MainActivity, GameWon::class.java)
                            intent.putExtra("PLAYER",currentPlayer.value)
                            startActivity(intent)
                        }
                        currentPlayer.value = PLAYER_ONE
                    }

                    if( isGameFork() ){
                        var intent : Intent = Intent(this@MainActivity, ForkActivity::class.java)
                        startActivity (intent)
                    }
                }
            }
        }
    }

    fun isGameWon(player : Int ):Boolean{
        lateinit var character : String

        if( player == PLAYER_ONE ){
            character = "X"
        }else{
            character = "O"
        }


        //FOR ROWS
        for(i in 0 .. 2){
            var rowCheck :Boolean = true
            for( j in 0 .. 2){
                if( boxValue.value[i][j] != character ){
                    rowCheck = false
                }
            }

            if( rowCheck == true ){
                return true
            }
        }

        //FOR COLUMNS
        for(i in 0 .. 2){
            var columnCheck :Boolean = true
            for( j in 0 .. 2){
                if( boxValue.value[j][i] != character ){
                    columnCheck = false
                }
            }

            if( columnCheck == true ){
                return true
            }
        }

        //FOR DIAGONALS
        if( (boxValue.value[0][0]==character) && (boxValue.value[1][1] == character) && (boxValue.value[2][2] == character) ){
            return true
        }

        if( (boxValue.value[0][2]==character) && (boxValue.value[1][1] == character) && (boxValue.value[2][0] == character) ){
            return true
        }

        return false
    }

    fun isGameFork() : Boolean {
        for( i in 0 .. 2 ){
            for( j in 0 .. 2){
                Log.d("XYZ",i.toString()+" "+j)
                if( boxValue.value[i][j] == "" ){
                    return false
                }
            }
        }

        return true
    }
}

@Composable
fun CreateBox(
    width: Float,
    boxValue : String,
    updateBoxValue : (Unit) -> Unit
){
    val context = LocalContext.current
    Box(
        modifier=Modifier.fillMaxWidth(width)
            .aspectRatio(1f)
            .border(5.dp,Color.Black)
            .clickable{
                if( boxValue == "" ) {
                    updateBoxValue(Unit)
                }else{
                    Toast.makeText(context,"Select an Empty Box",Toast.LENGTH_SHORT).show()
                }
            },
        contentAlignment = Alignment.Center
    ){
        Text(
            text=boxValue,
            fontSize = 30.sp
        )
    }
}

@Composable
fun CreateRow(
    boxValue : Array<String>,
    updateRow : (Int) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        CreateBox(0.3333f,boxValue[0]){
            updateRow(0)
        }
        CreateBox(0.5f,boxValue[1]){
            updateRow(1)
        }
        CreateBox(1f,boxValue[2]){
            updateRow(2)
        }
    }
}

@Composable
fun CreateGrid(
    boxValue : Array<Array<String>>,
    updateGrid : (Array<Int>) -> Unit
){

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CreateRow(boxValue[0]) {
            updateGrid(arrayOf(0,it))
        }

        CreateRow(boxValue[1]) {
            updateGrid(arrayOf(1,it))
        }

        CreateRow(boxValue[2]) {
            updateGrid(arrayOf(2,it))
        }

    }
}