package com.example.pokerchips

import android.app.Application
import com.example.pokerchips.model.UsersService

class App : Application() {

    val usersService = UsersService()
}