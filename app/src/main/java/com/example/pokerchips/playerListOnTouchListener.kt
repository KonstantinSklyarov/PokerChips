package com.example.pokerchips

import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity

class playerListOnTouchListener(var layout: MainActivity): OnTouchListener {
    var x: Float = 0f
    var deltaX: Float = 0f

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if (p1 != null) {
            when (p1.action) {
                MotionEvent.ACTION_DOWN -> {
                    layout.prepareMovePlayerLayout()
                    x = p0?.x!!
                    deltaX = p1.x
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    p0?.x = x + (deltaX - p1.x)
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    layout.placePlayers()
                    return true
                }
            }
        }
        return false
    }
}