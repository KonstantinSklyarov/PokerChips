package com.example.pokerchips

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.jar.Attributes

class chip(var imageButton: ImageButton, var appCompatActivity: AppCompatActivity, var chipValue: Int) {
    public var copyImage: ImageView = ImageView(this.appCompatActivity)
    init {
        var draw: Drawable = imageButton.drawable
        //draw.alpha = draw.alpha / 2
        copyImage!!.setImageDrawable(draw)
    }

}