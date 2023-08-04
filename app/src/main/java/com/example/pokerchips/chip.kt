package com.example.pokerchips

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

//Лошара ты
class chip(var imageButton: ImageButton, var appCompatActivity: AppCompatActivity, var chipValue: Int) {
    public var copyImage: ImageView = ImageView(this.appCompatActivity)
    init {
        var draw: Drawable = imageButton.drawable.mutate()
        draw.alpha = draw.alpha
        copyImage.setImageDrawable(draw)
        val scale = appCompatActivity.resources.displayMetrics.scaledDensity
        // Устанавливаем контейнер как контент для активности
        appCompatActivity.addContentView(copyImage, ViewGroup.LayoutParams((73 * scale).toInt(), (73 * scale).toInt()))
        //appCompatActivity.setContentView(container)

    }

}