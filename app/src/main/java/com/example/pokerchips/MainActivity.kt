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
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokerchips.databinding.ActivityMainBinding
import com.example.pokerchips.model.UserActionListener
import com.example.pokerchips.model.UsersAdapter
import com.example.pokerchips.model.UsersService
import com.example.pokerchips.model.User
import com.example.pokerchips.model.UsersListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter

    private val usersService: UsersService
        get() = (applicationContext as App).usersService
    //Лошара
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var transferTurnButton: Button = findViewById(R.id.transferTurn)
        var changePlayerNumberButton: Button = findViewById(R.id.changePlayerNumber)
        var changePlayerNameButton: Button = findViewById(R.id.changeName)
        var editPlayerChipCountButton: Button = findViewById(R.id.editChipCount)
        var giveDealerButton: Button = findViewById(R.id.giveDealer)
        var kickButton: Button = findViewById(R.id.kick)
        var backFromPlayerSettingsButton: Button = findViewById(R.id.backFromPlayerSettings)
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
        val groupButton: ImageButton = findViewById(R.id.groupButton)
        val cLayout = findViewById<LinearLayout>(R.id.chips)
        val groupScreen: FrameLayout = findViewById(R.id.groupScreen)
        var currentBetText: TextView = findViewById(R.id.currentBetText)
        var playerChipsCountText: TextView = findViewById(R.id.playerChipsCountText)
        val passConfirm: LinearLayout = findViewById(R.id.passConfirm)
        val passConfirmBg: FrameLayout = findViewById(R.id.passConfirmBg)
        val playersSettingsLayout: LinearLayout = findViewById(R.id.playerSettings)
        val playerNames = Array(5) { index ->
            getString(resources.getIdentifier("playerName${index + 1}", "string", packageName))
        }
        val playerSettingsButtons: Array<ImageButton> = Array(5) { index ->
            findViewById<ImageButton>(resources.getIdentifier("playerSettings${index + 1}", "id", packageName))
        }

        var player1 = player(1, getString(R.string.playerName1), getString(R.string.player1ChipCount).toInt(), true, true)
        var player2 = player(2, getString(R.string.playerName2), getString(R.string.player2ChipCount).toInt(), false, false)
        var player3 = player(3, getString(R.string.playerName3), getString(R.string.player3ChipCount).toInt(), false, false)
        var player4 = player(4, getString(R.string.playerName4), getString(R.string.player4ChipCount).toInt(), false, false)
        var player5 = player(5, getString(R.string.playerName5), getString(R.string.player5ChipCount).toInt(), false, false)
        var player6 = player(6, getString(R.string.playerName6), getString(R.string.player6ChipCount).toInt(), false, false)
        var activeSettingsPlayer: player = player1

        val playerList = listOf(player1, player2, player3, player4, player5, player6)



        var chip5 = Chip(findViewById(R.id.bet5), this, 5, cLayout)
        var chip10 = Chip((findViewById(R.id.bet10)), this, 10, cLayout)
        var chip25 = Chip(findViewById(R.id.bet25), this, 25, cLayout)
        var chip50 = Chip(findViewById(R.id.bet50), this, 50, cLayout)
        var chip100 = Chip(findViewById(R.id.bet100), this, 100, cLayout)

        val betChips = listOf(
            chip5,
            chip10,
            chip25,
            chip50,
            chip100
        )

        var playerSettingsFunctionsButtons =  listOf(transferTurnButton, changePlayerNumberButton, changePlayerNameButton, editPlayerChipCountButton, giveDealerButton, kickButton, backFromPlayerSettingsButton)

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

        fun setupButtonChangeNameListeners() {
            var index = 0
            for (playerName in playerNames){

            }

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
            betChips.map { c: Chip -> c.clearChip() }
            refreshTextAfterBet()
            updateButtonStates()
            refreshTextAfterBet()
            centerButton.text = "Колл"

        }

        fun setupPlayerSettingsButtonsClickListeners() {
            for ((index, playerSettingsButton) in playerSettingsButtons.withIndex()) {
                playerSettingsButton.setOnClickListener {
                    activeSettingsPlayer = playerList[index] //При нажатии на кнопку приложение запоминает номер игрока, возле которого эти настройки нажали. Все кнопки в настройках получают номер этого игрока и работают с ним
                    togglePlayersSettingsLayoutVisibility()
                }
            }
        }

        fun setupButtonPlayerSettingsFunctionClickListeners() {
            transferTurnButton.setOnClickListener {
                activeSettingsPlayer.setPlayerCurrentTurn(true)
                // TODO: Убрать активный ход у активного игрока
            }
            changePlayerNameButton.setOnClickListener {
                // TODO: Сделать всплывающее окно, в котором можно выбрать имя игрока и вызвать функцию activeSettingsPlayer.setPlayerName(новое имя)
            }
            changePlayerNumberButton.setOnClickListener {
                // TODO: Сделать всплывающее окно, в котором можно выбрать номер игрока и вызвать функцию activeSettingsPlayer.setPlayerNumber(выбранный номер)
            }
            editChipCount.setOnClickListener {
                // TODO: Сделать всплывающее окно, в котором можно выбрать количество фишек игрока и вызвать функцию activeSettingsPlayer.setPlayerChipCount(новое количество фишек)
            }
            giveDealerButton.setOnClickListener {
                toggleGroupScreenVisibility()
                togglePlayersSettingsLayoutVisibility()
                // TODO: Убрать диллера у игрока
            }
            kickButton.setOnClickListener {
                // TODO: Реплизовать удаление игрока из лобби
            }
            backFromPlayerSettingsButton.setOnClickListener {
                activeSettingsPlayer = player1
                togglePlayersSettingsLayoutVisibility()
            }
        }
        setupButtonPlayerSettingsFunctionClickListeners()



        setupPlayerSettingsButtonsClickListeners()

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

        fun changePlayerName(player: player) {

        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UsersAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) {
                usersService.moveUser(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                usersService.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                Toast.makeText(this@MainActivity, "User: ${user.name}", Toast.LENGTH_SHORT).show()
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        usersService.addListener(usersListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        usersService.removeListener(usersListener)
    }

    private val usersListener: UsersListener = {
        adapter.users = it
    }
}
