package com.example.pokerchips

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton

class ChipOnTouchListener(val chip: Chip, val mainActivity: MainActivity) : View.OnTouchListener {
    private fun wasSwipedUp(event: MotionEvent): Boolean {
        val deltaY = event.y - event.rawY // Разница между вертикальной координатой касания и глобальной координатой касания

        val minSwipeDistance = 100 // Минимальное расстояние для определения свайпа вверх

        return deltaY < -minSwipeDistance
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Палец прикоснулся к кнопке (начало касания)
                    chip.imageButton.alpha = 0.5f
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    // Палец движется по кнопке
                    chip.imageButton.alpha = 0.5f
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    // Палец отпущен от кнопки (конец касания)
                    chip.imageButton.alpha = 1f
                    if (mainActivity.betChip(chip)) {
                        chip.movingToCenter()
                    } else {
                        chip.imageButton.alpha = 0.5f
                    }
                }

            }
        }
        return false;
    }
}