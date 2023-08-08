package com.example.pokerchips

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.animation.ObjectAnimator
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.LinearLayout
import android.view.MotionEvent
import android.widget.ImageButton
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {


    //Лошара
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var playerChipsCount: Int = 500
        var currentBet: Int = 0
        var currentAnimation: ObjectAnimator? = null
        val leftButton: Button = findViewById(R.id.leftButton)
        val centerButton: Button = findViewById(R.id.centerButton)
        val rightButton: Button = findViewById(R.id.rightButton)
        val passConfirmNo: Button = findViewById(R.id.passConfirmNo)
        val passConfirmYes: Button = findViewById(R.id.passConfirmYes)
        val betWindow: LinearLayout = findViewById(R.id.betWindow)
        val giveDealer: Button = findViewById(R.id.giveDealer)
        val changeName: Button = findViewById(R.id.changeName)
        val editChipCount: Button = findViewById(R.id.editChipCount)
        val kick: Button = findViewById(R.id.kick)
        val changeNameButton: Button = findViewById(R.id.changeName)
        val groupButton: ImageButton = findViewById(R.id.groupButton)
        val cLayout = findViewById<LinearLayout>(R.id.chips)
        val groupScreen: FrameLayout = findViewById(R.id.groupScreen)
        var currentBetText: TextView = findViewById(R.id.currentBetText)
        var playerChipsCountText: TextView = findViewById(R.id.playerChipsCountText)
        val passConfirm: LinearLayout = findViewById(R.id.passConfirm)
        val passConfirmBg: FrameLayout = findViewById(R.id.passConfirmBg)
        val playerSettingsButton1 : ImageButton = findViewById(R.id.playerSettings1)
        val playerSettingsButton2 : ImageButton = findViewById(R.id.playerSettings2)
        val playerSettingsButton3 : ImageButton = findViewById(R.id.playerSettings3)
        val playerSettingsButton4 : ImageButton = findViewById(R.id.playerSettings4)
        val playerSettingsButton5 : ImageButton = findViewById(R.id.playerSettings5)
        val playersSettingsLayout: LinearLayout = findViewById(R.id.playerSettings)
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

        var playerSettingsButtons = listOf(
            playerSettingsButton1,
            playerSettingsButton2,
            playerSettingsButton3,
            playerSettingsButton4,
            playerSettingsButton5
        )

        fun updateButtonStates() {
            for (chip in betChips)  {
                val isEnabled = playerChipsCount >= chip.chipValue
                val alpha = if (isEnabled) 1.0f else 0.5f
                chip.imageButton.isEnabled = isEnabled
                chip.imageButton.alpha = alpha
            }
        }

        fun createAlphaAnimation(view: View): ObjectAnimator {
            val alphaAnimation = ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1.0f)
            alphaAnimation
            alphaAnimation.duration = 200
            return alphaAnimation
        }

        fun toggleBetWindowEnabled() {
            for (chip in betChips) {
                chip.imageButton.isEnabled = false
                chip.imageButton.alpha = 0.5f
            }
        }

        fun toggleBottomButtons() {
            if (leftButton.isEnabled) {
                leftButton.isEnabled = false
                leftButton.alpha = 0.5f
                centerButton.isEnabled = false
                centerButton.alpha = 0.5f
                rightButton.isEnabled = false
                rightButton.alpha = 0.5f
            }
            else {
                leftButton.isEnabled = true
                leftButton.alpha = 1f
                centerButton.isEnabled = true
                centerButton.alpha = 1f
                rightButton.isEnabled = true
                rightButton.alpha = 1f
            }
            if (betWindow.isVisible) {
                toggleBetWindowEnabled()
                updateButtonStates()
            }
        }

        fun togglePassConfirmVisibility() {
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

        fun toggleGroupScreenVisibility() {
            if (groupScreen.visibility == View.VISIBLE) {
                // Если видимо, скрываем
                groupScreen.visibility = View.GONE
                playersSettingsLayout.visibility = View.GONE
                toggleBottomButtons()

            } else {
                // Если скрыто, показываем
                groupScreen.visibility = View.VISIBLE
                toggleBottomButtons()
            }
        }

        fun togglePlayersSettingsLayoutVisibility() {
            if (playersSettingsLayout.visibility == View.VISIBLE) {
                // Если видимо, скрываем
                playersSettingsLayout.visibility = View.GONE

            } else {
                // Если скрыто, показываем
                playersSettingsLayout.visibility = View.VISIBLE
            }
        }



        fun wasSwipedUp(event: MotionEvent): Boolean {
            val deltaY = event.y - event.rawY // Разница между вертикальной координатой касания и глобальной координатой касания
            val minSwipeDistance = 10 // Минимальное расстояние для определения свайпа вверх
            return deltaY < -minSwipeDistance
        }

        playerChipsCountText.text = playerChipsCount.toString()
        fun betChip(chipValue: Int) {
            currentBet += chipValue
            playerChipsCount -= chipValue


        }
        fun refreshTextAfterBet() {
            currentBetText.text = currentBet.toString()
            playerChipsCountText.text = playerChipsCount.toString()
            centerButton.text = "Райз $$currentBet"
        }

        fun setupButtonChipClickListeners() {
            try {
                for (chip in betChips) {
                    chip.imageButton.setOnTouchListener { _, event ->
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                chip.imageButton.alpha = 0.5f
                                return@setOnTouchListener true
                            }
                            MotionEvent.ACTION_MOVE -> {
                                // Палец движется по кнопке
                                chip.imageButton.alpha = 0.5f
                                return@setOnTouchListener true
                            }
                            MotionEvent.ACTION_UP -> {
                                // Палец отпущен от кнопки (конец касания)
                                if (wasSwipedUp(event)) {
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
                                        chip.imageButton.performClick()
                                    }

                                } else {
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
                                return@setOnTouchListener true
                            }
                            else -> return@setOnTouchListener false
                        }
                    }
                }
            } catch (nul: NullPointerException) {
                println(nul.message)
            }
        }
        setupButtonChipClickListeners()
        leftButton.setOnClickListener {
            playerChipsCount += currentBet
            currentBet = 0
            betChips.map { c: chip -> c.clearChip() }
            refreshTextAfterBet()
            updateButtonStates()
            refreshTextAfterBet()
            centerButton.text = "Колл"

        }

        fun setupPlayerSettningsButtonsClickListners() {
            for (playerSettingsButton in playerSettingsButtons) {
                playerSettingsButton.setOnClickListener{
                    togglePlayersSettingsLayoutVisibility()
                }
            }
        }
        setupPlayerSettningsButtonsClickListners()

        centerButton.setOnClickListener {

        }
        rightButton.setOnClickListener{
            togglePassConfirmVisibility()
            centerButton.isEnabled = false
            leftButton.isEnabled = false
            rightButton.isEnabled = false
            if (betWindow.isVisible) {
                toggleBetWindowEnabled()
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

        giveDealer.setOnClickListener {

        }

        changeName.setOnClickListener {

        }

        editChipCount.setOnClickListener {

        }

        kick.setOnClickListener {

        }

        groupButton.setOnClickListener {
            toggleGroupScreenVisibility()
        }

        changeNameButton.setOnClickListener {

        }


    }
}