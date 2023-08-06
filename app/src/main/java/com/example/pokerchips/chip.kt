package com.example.pokerchips

import android.icu.util.TimeUnit
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.ArraySet
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.lang.NullPointerException
import java.util.ArrayList

//Лошара ты
class chip(var imageButton: ImageButton, var appCompatActivity: AppCompatActivity, var chipValue: Int) {
    //Значение sp
    var scale = appCompatActivity.resources.displayMetrics.scaledDensity
    //Высота экрана
    var height = appCompatActivity.resources.displayMetrics.heightPixels
    //Координата левого края полоски с фишками
    var xCoord = appCompatActivity.resources.displayMetrics.widthPixels - 382*scale

    val imageDefault = getChip()
    var chipSet = ArrayList<ImageView>()
    init {
        imageDefault.alpha = 0f
    }

    fun clearChip() {
        for (chip: ImageView in chipSet) {
            if (chip != null && chip.parent != null) {
                (chip.parent as ViewGroup).removeView(chip)
            }
        }
    }
    //Получение нового изображения фишки в месте нахождения оригинала
    fun getChip() : ImageView {
        val newImage = ImageView(this.appCompatActivity)
        newImage.setImageDrawable(imageButton.drawable.mutate())
        when (chipValue) {
            5 -> newImage.x = xCoord
            10 -> newImage.x = xCoord + (75 * scale)
            25 -> newImage.x = xCoord + (75 * scale) * 2
            50 -> newImage.x = xCoord + (75 * scale) * 3
            100 -> newImage.x = xCoord + (75 * scale) * 4
        }
        //newImage.x += (-19 * scale)
        newImage.y = height - (16 + 80 + 5 + 75) * scale //-20 * scale
        newImage.requestLayout()
        appCompatActivity.addContentView(newImage, ViewGroup.LayoutParams((75 * scale).toInt(), (75 * scale).toInt()))
        java.util.concurrent.TimeUnit.MILLISECONDS.sleep(20)
        return newImage
    }
    //Анимация движения фишки к центру
    fun movingToCenter() {
        val imageView = getChip()
        //Координаты конечного местоположения
        val xCenter = appCompatActivity.resources.displayMetrics.widthPixels / 2f - 30 * scale
        val yCenter = height / 2 - 25 * scale
        //Сколько длится анимация
        val animDur = 1000L
        val animation = TranslateAnimation(0f, xCenter - imageDefault.x, 0f, yCenter - imageDefault.y).apply {
            duration = animDur
        }
        var alpha  = AlphaAnimation(1f, 0f).apply {
            duration = animDur;
        }
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                imageView.x = xCenter
                imageView.y = yCenter
                imageView.alpha = 0f
                chipSet.add(imageView)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        alpha.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(animation)
        animationSet.addAnimation(alpha)
        imageView.startAnimation(animationSet)
        //imageView.startAnimation(animation)

    }

}