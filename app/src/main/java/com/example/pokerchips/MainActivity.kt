package com.example.pokerchips

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isVisible
import androidx.core.view.marginLeft

class MainActivity : AppCompatActivity() {

    var playerChipsCount: Int = 500
    var currentBet: Int = 0
    var currentBetText: TextView? = null
    var playerChipsCountText: TextView? = null
    var betChips = listOf<Chip>()

    var players = ArrayDeque<OtherPlayer>()
    var playerLayout: RelativeLayout? = null
    fun placePlayers() {
        playerLayout?.removeAllViews()
        if (players.size > 5) {
            var lastPlayer = players.removeLast()
            playerLayout?.addView(players.last().panel)
            playerLayout?.addView(lastPlayer.panel)
            players.addLast(lastPlayer)
            playerLayout?.addView(players.first().panel)
            playerLayout?.addView(players[1].panel)
            playerLayout?.addView(players[2].panel)
        } else {
            for (i in 0 until players.size) {
                playerLayout?.addView(players.get(i).panel)
            }
        }
    }

    fun prepareMovePlayerLayout() {
        var params = LinearLayout.LayoutParams((100 * players.size * betChips.get(0).scale).toInt(), (100 * betChips.get(0).scale).toInt())
        playerLayout?.layoutParams = params
        playerLayout?.x = -1 * (players.size - 5) * 100 * betChips.get(0).scale
        playerLayout?.removeAllViews()
        if (players.size > 5) {
            //Добавили левый конец
            for (i in 0 until players.size - 5) {
                playerLayout?.addView(players.get(players.size - i).panel)
            }
            //Добавили тело
            for (i in 0 until 5) {
                playerLayout?.addView(players.get(i).panel)
            }
            //Добавим хвост
            for (i in 5 until players.size) {
                playerLayout?.addView(players.get(i).panel)
            }
        } else {
            for (i in 0 until players.size) {
                playerLayout?.addView(players.get(i).panel)
            }
        }
    }


    fun refreshTextAfterBet() {
        currentBetText?.text = currentBet.toString()
        playerChipsCountText?.text = playerChipsCount.toString()
    }

    fun betChip(chip: Chip) : Boolean {
        if (playerChipsCount >= chip.chipValue) {
            playerChipsCount -= chip.chipValue
            currentBet += chip.chipValue
            refreshTextAfterBet()
            if (playerChipsCount < 100) {
                betChips.get(4).imageButton.alpha = 0.5f
            }
            if (playerChipsCount < 50) {
                betChips.get(3).imageButton.alpha = 0.5f
            }
            if (playerChipsCount < 25) {
                betChips.get(2).imageButton.alpha = 0.5f
            }
            if (playerChipsCount < 10) {
                betChips.get(1).imageButton.alpha = 0.5f
            }
            if (playerChipsCount < 5) {
                betChips.get(0).imageButton.alpha = 0.5f
            }
            return true
        }
        return false
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentBetText = findViewById(R.id.currentBetText)
        playerChipsCountText = findViewById(R.id.playerChipsCountText)
        var gestureDetector: GestureDetectorCompat
        val cLayout = findViewById<LinearLayout>(R.id.chips)
        val chip5 = Chip(findViewById(R.id.bet5), this, 5, cLayout)
        val chip10 = Chip(findViewById(R.id.bet10), this, 10, cLayout)
        val chip25 = Chip(findViewById(R.id.bet25), this, 25, cLayout)
        val chip50 = Chip(findViewById(R.id.bet50), this, 50, cLayout)
        val chip100 = Chip(findViewById(R.id.bet100), this, 100, cLayout)

        betChips = listOf(
            chip5,
            chip10,
            chip25,
            chip50,
            chip100
        )
        //TODO: Установить с сервера количество фишек
        playerChipsCountText?.text = playerChipsCount.toString()

        //TODO: Настройка игроков
        for (i in 0 until 5) {
            players.add(OtherPlayer(chip5.imageDefault, "Player " + i.toString(), 500, this, 5))
        }
        playerLayout = findViewById(R.id.playersLayout)
        playerLayout?.setOnTouchListener(playerListOnTouchListener(this))
        placePlayers()


        fun updateButtonStates() {
            for (chip in betChips)  {
                val isEnabled = playerChipsCount >= chip.chipValue
                val alpha = if (isEnabled) 1.0f else 0.5f
                chip.imageButton.isEnabled = isEnabled
                chip.imageButton.alpha = alpha
            }
        }


        @SuppressLint("ClickableViewAccessibility")
        fun setupButtonChipClickListeners() {
            try {
                for (chip in betChips) {
                    chip.imageButton.setOnTouchListener(ChipOnTouchListener(chip, this))
                }
            } catch (nul: NullPointerException) {
                println(nul.message)
            }
        }
        setupButtonChipClickListeners()

        val leftButton: Button = findViewById(R.id.leftButton)
        val centerButton: Button = findViewById(R.id.centerButton)
        val rightButton: Button = findViewById(R.id.rightButton)
        val passConfirmNo: Button = findViewById(R.id.passConfirmNo)
        val passConfirmYes: Button = findViewById(R.id.passConfirmYes)
        val betWindow: LinearLayout = findViewById(R.id.betWindow)


        leftButton.setOnClickListener {
            if (betWindow.isVisible == false) {
                toggleBetWindowVisibility()
            } else {
                playerChipsCount += currentBet
                currentBet = 0
                betChips.map { c: Chip -> c.clearChip() }
                refreshTextAfterBet()
                updateButtonStates()
                toggleBetWindowVisibility()
                refreshTextAfterBet()

            }
        }

        centerButton.setOnClickListener {

        }

        rightButton.setOnClickListener{
            togglePassConfirmVisibility()
            centerButton.isEnabled = false
            leftButton.isEnabled = false
            rightButton.isEnabled = false
            if (betWindow.isVisible) {
                toggleBetWindowVisibility()
                refreshTextAfterBet()
            }
        }

        passConfirmYes.setOnClickListener {
            leftButton.alpha = 0.5f
            leftButton.isEnabled = false
            centerButton.alpha = 0.5f
            centerButton.isEnabled = false
            rightButton.alpha = 0.5f
            rightButton.isEnabled = false
            togglePassConfirmVisibility()
            refreshTextAfterBet()
            if (leftButton.text == "Отмена") {
                leftButton.text = "Ставка"
                playerChipsCount += currentBet
                currentBet = 0
                refreshTextAfterBet()

            }
        }

        passConfirmNo.setOnClickListener {
            togglePassConfirmVisibility()
            centerButton.isEnabled = true
            leftButton.isEnabled = true
            rightButton.isEnabled = true
        }
        toggleBetWindowVisibility()
        togglePassConfirmVisibility()
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

    fun toggleBetWindowVisibility() {
        val leftButton: Button = findViewById(R.id.leftButton)
        val betWindow: LinearLayout = findViewById(R.id.betWindow) // Замените на ваш реальный id

        if (betWindow.visibility == View.VISIBLE) {
            // Если видимо, скрываем
            betWindow.visibility = View.GONE
            leftButton.text = "Ставка"
            playerChipsCount += currentBet
            currentBet = 0
        } else {
            // Если скрыто, показываем
            betWindow.visibility = View.VISIBLE
            leftButton.text = "Назад"
        }
    }

    fun togglePassConfirmVisibility() {
        val passConfirm: LinearLayout = findViewById(R.id.passConfirm)
        val passConfirmBg: FrameLayout = findViewById(R.id.passConfirmBg)
        if (passConfirm.visibility == View.VISIBLE) {
            // Если видимо, скрываем
            passConfirm.visibility = View.GONE
            passConfirmBg.visibility = View.GONE

        } else {
            // Если скрыто, показываем
            passConfirm.visibility = View.VISIBLE
            passConfirmBg.visibility = View.VISIBLE
            playerChipsCount += currentBet
            currentBet = 0
        }
    }


}