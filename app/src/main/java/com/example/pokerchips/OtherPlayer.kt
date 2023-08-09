package com.example.pokerchips

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

class OtherPlayer(val icon: ImageView, val nickname: String, var money: Int, val context: Context, var playerCount: Int) {
    //TODO: Добавить отображение очереди
    var queue: Int = 0
    var panel: LinearLayout = LinearLayout(context)
    var moneyView: TextView = TextView(context)
    val scale = icon.resources.displayMetrics.scaledDensity

    init {
        //TODO: Добавить размерную сетку
        panel.layoutParams = ViewGroup.LayoutParams((80 * scale).toInt(), LinearLayout.LayoutParams.MATCH_PARENT)
        panel.orientation = LinearLayout.VERTICAL
        panel.addView(icon)
        val nickView = TextView(panel.context)
        nickView.text = nickname
        panel.addView(nickView)
        moneyView.text = money.toString()
        panel.addView(moneyView)
    }

    fun editMoney(newMoney: Int) {
        this.money = newMoney
    }

    fun changeQueue() {
        if (queue == 0) {
            queue = playerCount - 1
        } else {
            queue--
        }
    }
    fun changeQueue(count: Int) {
        if (queue - count < 0) {
            queue = queue - count + playerCount
        } else {
            queue -= count
        }
    }

}