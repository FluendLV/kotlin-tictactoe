package com.example.tictactoeandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import android.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var player1: Player
    private lateinit var player2: Player
    private var currentPlayer: Player? = null
    private var gameBoard: Array<Array<Button?>> = arrayOf()
    private var moves = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Greeting message
        val greetingDialog = AlertDialog.Builder(this)
        greetingDialog.setTitle("Welcome to Tic-Tac-Toe!")
        greetingDialog.setMessage("Choose a game mode and start playing. Have fun!")
        greetingDialog.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        greetingDialog.show()

        val modeRadioGroup = findViewById<RadioGroup>(R.id.modeRadioGroup)
        modeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val player2Type = when (checkedId) {
                R.id.pvpRadioButton -> Player.PlayerType.HUMAN
                R.id.pvcRadioButton -> Player.PlayerType.COMPUTER
                else -> Player.PlayerType.HUMAN
            }
            initPlayersForMode(player2Type)
        }

        // Ensure the PvP mode is selected by default
        findViewById<RadioButton>(R.id.pvpRadioButton).isChecked = true

        initBoard()
    }

    private fun initPlayersForMode(player2Type: Player.PlayerType) {
        player1 = Player("X", Player.PlayerType.HUMAN)
        player2 = Player(if (player2Type == Player.PlayerType.HUMAN) "O" else "O", player2Type)
        currentPlayer = player1
    }

    private fun initBoard() {
        gameBoard = arrayOf(
            arrayOf(findViewById(R.id.button1), findViewById(R.id.button2), findViewById(R.id.button3)),
            arrayOf(findViewById(R.id.button4), findViewById(R.id.button5), findViewById(R.id.button6)),
            arrayOf(findViewById(R.id.button7), findViewById(R.id.button8), findViewById(R.id.button9))
        )

        for (i in 0..2) {
            for (j in 0..2) {
                gameBoard[i][j]?.setOnClickListener {
                    handleUserInput(i, j)
                }
            }
        }
    }

    private fun handleUserInput(row: Int, col: Int) {
        if (gameBoard[row][col]?.text.isNullOrEmpty()) {
            gameBoard[row][col]?.text = currentPlayer?.name

            if (checkWin(row, col)) {
                showWinMessage(currentPlayer?.name)
            } else if (++moves == 9) {
                showDrawMessage()
            } else {
                switchPlayer()
                if (currentPlayer?.type == Player.PlayerType.COMPUTER) {
                    handleComputerMove()
                }
            }
        }
    }

    private fun handleComputerMove() {
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (i in 0..2) {
            for (j in 0..2) {
                if (gameBoard[i][j]?.text.isNullOrEmpty()) {
                    emptyCells.add(Pair(i, j))
                }
            }
        }

        if (emptyCells.isNotEmpty()) {
            val (row, col) = emptyCells.random()
            gameBoard[row][col]?.text = currentPlayer?.name

            if (checkWin(row, col)) {
                showWinMessage(currentPlayer?.name)
            } else if (++moves == 9) {
                showDrawMessage()
            } else {
                switchPlayer()
            }
        }
    }

    private fun checkWin(row: Int, col: Int): Boolean {
        val symbol = currentPlayer?.name

        // Check row
        if (gameBoard[row].all { it?.text == symbol }) return true

        // Check column
        if (gameBoard.map { it[col] }.all { it?.text == symbol }) return true

        // Check main diagonal
        if (row == col && gameBoard.indices.all { gameBoard[it][it]?.text == symbol }) return true

        // Check secondary diagonal
        if (row + col == 2 && gameBoard.indices.all { gameBoard[it][2 - it]?.text == symbol }) return true

        return false
    }


    private fun showWinMessage(winnerSymbol: String?) {
        Toast.makeText(this, "Player with $winnerSymbol wins!", Toast.LENGTH_SHORT).show()
        resetGame()
    }

    private fun showDrawMessage() {
        Toast.makeText(this, "It's a draw!", Toast.LENGTH_SHORT).show()
        resetGame()
    }

    private fun switchPlayer() {
        currentPlayer = if (currentPlayer == player1) player2 else player1
    }

    private fun resetGame() {
        for (i in 0..2) {
            for (j in 0..2) {
                gameBoard[i][j]?.text = ""
            }
        }
        moves = 0
    }
}

