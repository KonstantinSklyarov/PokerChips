package com.example.pokerchips

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.jar.Attributes

class DefaultButton(var imageButton: ImageButton, var appCompatActivity: AppCompatActivity, var chipValue: Int) {
    var copyImage: ImageView? = null
    init {
        copyImage = ImageView(appCompatActivity)
        var draw: Drawable = imageButton.drawable
        draw.alpha = draw.alpha / 2
        copyImage!!.setImageDrawable(draw)

    }

}