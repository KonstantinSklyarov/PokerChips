package com.example.pokerchips

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import android.widget.ImageView
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    var playerChipsCount: Int = 500
    var currentBet: Int = 0
    var currentAnimation: ObjectAnimator? = null

    //Лошара
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val cLayout = findViewById<LinearLayout>(R.id.chips)
        val imageView: ImageView = findViewById(R.id.prefabImageBet5)
        var chip5 = chip(findViewById(R.id.bet5), this, 5, cLayout)
        var chip10 = chip(findViewById(R.id.bet10), this, 10, cLayout)
        var chip25 = chip(findViewById(R.id.bet25), this, 25, cLayout)
        var chip50 = chip(findViewById(R.id.bet50), this, 50, cLayout)
        var chip100 = chip(findViewById(R.id.bet100), this, 100, cLayout)

        val betChips = listOf(
            chip5,
            chip10,
            chip25,
            chip50,
            chip100
        )

        var currentBetText: TextView = findViewById(R.id.currentBetText)
        var playerChipsCountText: TextView = findViewById(R.id.playerChipsCountText)
        playerChipsCountText.text = playerChipsCount.toString()

        fun betChip(chipValue: Int) {
            currentBet += chipValue
            playerChipsCount -= chipValue

        }
        fun refreshTextAfterBet() {
            currentBetText.text = currentBet.toString()
            playerChipsCountText.text = playerChipsCount.toString()
        }

        fun updateButtonStates() {
            for (chip in betChips)  {
                val isEnabled = playerChipsCount >= chip.chipValue
                val alpha = if (isEnabled) 1.0f else 0.5f
                chip.imageButton.isEnabled = isEnabled
                chip.imageButton.alpha = alpha
            }
        }

        fun setupButtonChipClickListeners() {
            try {
                for (chip in betChips) {
                    chip.imageButton.setOnClickListener() {
                        currentAnimation = createAlphaAnimation(chip.imageButton)
                        currentAnimation?.start()
                        if (playerChipsCount >= chip.chipValue) {
                            betChip(chip.chipValue)
                            refreshTextAfterBet()
                            chip.imageButton.alpha = 0.5f
                            //animateAndDisappear(chip.getChip())
                            currentAnimation = createAlphaAnimation(chip.imageButton)
                            currentAnimation?.start()
                            updateButtonStates()
                            chip.movingToCenter()
                        }
                        if (playerChipsCount <= chip.chipValue) {
                            currentAnimation?.cancel() // (если быстро кликать, то кнопка после блокировки станет обратно яркой, потому что запущенная с предыдущего клика анимация ещё идёт. Эта штука отменяет анимацию и даёт кнопке нормально заблокироваться)
                        }
                    }
                }
            } catch (nul: NullPointerException) {
                println(nul.message)
            }
        }
        setupButtonChipClickListeners()
        val chipsPrefabs = mutableListOf<ImageView>()

        val leftButton: Button = findViewById(R.id.leftButton)



//        fun onChipButtonClick(button: ImageButton) {
//            val index = betButtons.indexOf(button)
//            if (index != -1) {
//                val chipValue = chipValues[index]
//                if (playerChipsCount >= chipValue) {
//                    betChip(chipValue)
//                    refreshTextAfterBet()
//                    button.alpha = 0.5f
//                    currentAnimation = createAlphaAnimation(button)
//                    currentAnimation?.start()
//                    updateButtonStates()
//                    val imageView: ImageView = findViewById(R.id.prefabImageBet5)
//                    val duplicatedImageView: ImageView = createImageViewCopy(this, imageView)
//                    //hipsPrefabs.add(testImage)
//                    animateAndDisappear(testImage)
//                }
//                if (playerChipsCount <= chipValue) {
//                    currentAnimation?.cancel() // (если быстро кликать, то кнопка после блокировки станет обратно яркой, потому что запущенная с предыдущего клика анимация ещё идёт. Эта штука отменяет анимацию и даёт кнопке нормально заблокироваться)
//                }
//            }
//        }

        leftButton.setOnClickListener {
            if (leftButton.text == "Ставка") {
                leftButton.text = "Отмена"
            } else {
                leftButton.text = "Ставка"
                playerChipsCount += currentBet
                currentBet = 0
                betChips.map { c: chip -> c.clearChip() }
                refreshTextAfterBet()
                updateButtonStates()
            }
        }
    }
    fun createAlphaAnimation(view: View): ObjectAnimator {
        val alphaAnimation = ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1.0f)
        alphaAnimation
        alphaAnimation.duration = 200
        return alphaAnimation
    }
    private fun animateAndDisappear(view: View) {
        val centerX = resources.displayMetrics.widthPixels / 2f
        val centerY = resources.displayMetrics.heightPixels / 2f

        view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                view.viewTreeObserver.removeOnPreDrawListener(this)

                val originalX = view.x + view.width / 2
                val originalY = view.y + view.height / 2

                val copyAnimatorX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 0.8f)
                val copyAnimatorY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 0.8f)

                val translateAnimatorX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, centerX - originalX)
                val translateAnimatorY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, centerY - originalY)

                val alphaAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f)

                val animatorSet = AnimatorSet().apply {
                    playTogether(copyAnimatorX, copyAnimatorY, translateAnimatorX, translateAnimatorY, alphaAnimator)
                    duration = 700
                }

                animatorSet.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        view.x = originalX - view.width / 2
                        view.y = originalY - view.height / 2

                        // Удаляем объект после завершения анимации
                        val parent = view.parent as ViewGroup
                        //parent.removeView(view)
                    }
                })

                animatorSet.start()

                return true
            }
        })
    }

    fun createImageViewCopy(context: Context, imageView: ImageView): ImageView {
        val newImageView = ImageView(context)

        // Копируем параметры размеров и позиции
        val layoutParams = imageView.layoutParams
        newImageView.layoutParams = layoutParams

        // Копируем изображение (Drawable) с оригинального ImageView
        val drawable = imageView.drawable
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            val newDrawable = BitmapDrawable(context.resources, bitmap.copy(bitmap.config, true))
            newImageView.setImageDrawable(newDrawable)
        }

        // Установим остальные параметры, если необходимо
        newImageView.scaleType = imageView.scaleType
        newImageView.adjustViewBounds = imageView.adjustViewBounds
        newImageView.background = imageView.background

        // Добавляем новый ImageView на родительский контейнер
        val parent = imageView.parent as ViewGroup
        val index = parent.indexOfChild(imageView)
        parent.addView(newImageView, index)

        return newImageView
    }

}