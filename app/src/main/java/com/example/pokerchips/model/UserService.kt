package com.example.pokerchips.model

import com.github.javafaker.Faker
import java.util.Collections

typealias UsersListener = (users: List<User>) -> Unit

class UsersService() {

    private var users = mutableListOf<User>()

    private val listeners = mutableSetOf<UsersListener>()


    init {
        val faker = Faker.instance()
        users = (0..100).map { User(
            id = it.toLong(),
            name = NAMES[it % NAMES.size],
            chipCount = SUM_CHIP_BET[it % SUM_CHIP_BET.size],
            photo = IMAGES[it % IMAGES.size],
            selectWinner = false
        ) }.toMutableList()
    }

    fun getUsers(): List<User> {
        return users
    }

    fun deleteUser(user: User) {
        val indexToDelete = users.indexOfFirst { it.id == user.id }
        if (indexToDelete != -1) {
            users.removeAt(indexToDelete)
            notifyChanges()
        }
    }

    fun moveUser(user: User, moveBy: Int) {
        val oldIndex = users.indexOfFirst { it.id == user.id }
        if (oldIndex == -1) return
        val newIndex = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= users.size) return
        Collections.swap(users, oldIndex, newIndex)
        notifyChanges()
    }

    fun addListener(listener: UsersListener) {
        listeners.add(listener)
        listener.invoke(users)
    }

    fun removeListener(listener: UsersListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(users) }
    }

    companion object {
        private val IMAGES = mutableListOf(
            "https://sun9-37.userapi.com/impg/0Uz1EptJBhYkU_oB-dFImMSD9GEnqu8V2Fpyow/aIP4ssI8teM.jpg?size=810x1080&quality=95&sign=197ff71d5e7e9eb2e7b887f5fe571372&type=album",
            "https://sun9-43.userapi.com/impg/HuzLUpQkmkk_bRn8urS2FrhJS_dqA6lDJyvBzw/j8AWku-sTHs.jpg?size=720x1280&quality=95&sign=17077586cf95ad5fe3e9ffad20ce5c31&type=album",
            "https://sun9-77.userapi.com/impf/c856124/v856124044/126f55/j1Z6YWlSnDw.jpg?size=2560x1920&quality=96&sign=c403f4efba0596689f9641bc0c8e9b2e&type=album",
            "https://sun1-30.userapi.com/impg/_4utiCqu-x1--QxpGa4CRRk2BfL_YnZC-eXzAA/q2SCj7pSnZI.jpg?size=640x640&quality=96&sign=360794e9661728f750601cbe0eb4def9&type=album",
            "https://sun1-30.userapi.com/impg/c853624/v853624586/187f36/i4f2GspN-8g.jpg?size=1280x960&quality=96&sign=d6041af79cb57adb934306b8de455339&type=album",
            "https://sun1-56.userapi.com/impg/oaOldmkPs-f22uaPLyAShwsC2OHdgSeNwip5og/6LSKHpKA9F8.jpg?size=972x2160&quality=95&sign=233bb51ccdc14bf0af10a9045d77f294&type=album",
            "https://images.unsplash.com/photo-1545996124-0501ebae84d0?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzY1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/flagged/photo-1568225061049-70fb3006b5be?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0Nzcy&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1567186937675-a5131c8a89ea?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODYx&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1546456073-92b9f0a8d413?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODY1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800"
        )

        private val NAMES = mutableListOf<String>(
            "Костя",
            "Даня",
            "Серёга",
            "Саня",
            "Лёха",
            "Настя",
            "Ника",
            "Гуриков",
            "Хатабыч",
            "Ибрагим"
        )

        private val SUM_CHIP_BET = mutableListOf<Int>(
            100,
            200,
            300,
            400,
            500,
            600,
            700,
            800,
            900,
            1000
        )
    }
}