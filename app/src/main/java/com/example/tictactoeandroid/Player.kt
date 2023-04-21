package com.example.tictactoeandroid

data class Player(val name: String, val type: PlayerType) {
    enum class PlayerType {
        HUMAN,
        COMPUTER
    }
}
