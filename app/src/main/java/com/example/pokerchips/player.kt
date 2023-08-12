package com.example.pokerchips

class player(var number: Int = 0, var name: String = "", var chipCount: Int = 0, var isDealer: Boolean = false, var currentTurn: Boolean = false ) {

    fun setPlayerName(newName: String = "") {
        this.name = newName
    }
    fun setPlayerChipCount(newChipCount: Int = 0) {
        this.chipCount = newChipCount
    }
    fun setPlayerDealer(isDealer: Boolean) {
        this.isDealer = isDealer
    }
    fun setPlayerCurrentTurn(currentTurn: Boolean) {
        this.currentTurn = currentTurn
    }
    fun setPlayerNumber(newNumber: Int) {
        this.number = newNumber
    }


}