package com.example.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.app.AlertDialog

class MainActivity2 : AppCompatActivity(), View.OnClickListener {

    private var playerOneActive = true
    private lateinit var playerOneScore: TextView
    private lateinit var playerTwoScore: TextView
    private lateinit var playerStatus: TextView
    private val buttons = Array<Button?>(9) { null }
    private lateinit var reset: Button
    private lateinit var playagain: Button
    private lateinit var highScoreButton: Button
    private val gameState = IntArray(9) { 2 }
    private val winningPositions = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
    )
    private var rounds = 0
    private var playerOneScoreCount = 0
    private var playerTwoScoreCount = 0
    private var highScores = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        playerOneScore = findViewById(R.id.score_Player1)
        playerTwoScore = findViewById(R.id.score_Player2)
        playerStatus = findViewById(R.id.textStatus)
        reset = findViewById(R.id.btn_reset)
        playagain = findViewById(R.id.btn_play_again)
        highScoreButton = findViewById(R.id.highscore)

        for (i in buttons.indices) {
            buttons[i] = findViewById(resources.getIdentifier("btn$i", "id", packageName))
            buttons[i]?.setOnClickListener(this)
        }

        reset.setOnClickListener {
            playAgain()
            playerOneScoreCount = 0
            playerTwoScoreCount = 0
            updatePlayerScore()
        }

        playagain.setOnClickListener {
            playAgain()
        }

        highScoreButton.setOnClickListener {
            displayHighScores()
        }
    }

    override fun onClick(view: View) {
        if ((view as Button).text.toString() != "") {
            return
        } else if (checkWinner()) {
            return
        }
        val buttonID = resources.getResourceEntryName(view.id)
        val gameStatePointer = buttonID.substring(buttonID.length - 1).toInt()

        if (playerOneActive) {
            view.text = "X"
            view.setTextColor(Color.parseColor("#ffc34a"))
            gameState[gameStatePointer] = 0
        } else {
            view.text = "O"
            view.setTextColor(Color.parseColor("#70fc3a"))
            gameState[gameStatePointer] = 1
        }
        rounds++

        if (checkWinner()) {
            if (playerOneActive) {
                playerOneScoreCount += 10 // Increase player one's score by 10
                updatePlayerScore()
                updateHighScores(playerOneScoreCount)
                playerStatus.text = "Player-1 has won"
            } else {
                playerTwoScoreCount += 10 // Increase player two's score by 10
                updatePlayerScore()
                updateHighScores(playerTwoScoreCount)
                playerStatus.text = "Player-2 has won"
            }
        } else if (rounds == 9) {
            playerStatus.text = "No Winner"
        } else {
            playerOneActive = !playerOneActive
        }
    }

    private fun checkWinner(): Boolean {
        var winnerResults = false
        for (winningPositions in winningPositions) {
            if (gameState[winningPositions[0]] == gameState[winningPositions[1]] &&
                gameState[winningPositions[1]] == gameState[winningPositions[2]] &&
                gameState[winningPositions[0]] != 2
            ) {
                winnerResults = true
            }
        }
        return winnerResults
    }

    private fun playAgain() {
        rounds = 0
        playerOneActive = true
        for (i in buttons.indices) {
            gameState[i] = 2
            buttons[i]?.text = ""
        }
        playerStatus.text = "Status"
    }

    private fun updatePlayerScore() {
        playerOneScore.text = playerOneScoreCount.toString()
        playerTwoScore.text = playerTwoScoreCount.toString()
    }

    private fun updateHighScores(score: Int) {
        highScores.add(score)
        highScores.sortDescending()
        if (highScores.size > 10) {
            highScores = highScores.take(10).toMutableList()
        }
    }

    private fun displayHighScores() {
        val highScoresText = highScores.joinToString("\n")
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("High Scores")
        alertDialogBuilder.setMessage(highScoresText)
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}