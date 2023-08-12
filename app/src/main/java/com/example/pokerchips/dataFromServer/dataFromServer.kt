package com.example.pokerchips.dataFromServer

fun getPlayerName(inputName: String): String {
    return inputName
    // пользователь после подключения отправляет своё имя на сервер, что бы там создался элемент списка с этим именем и вернул пользователю для рендера этого списка
    // Пока нет сервера, имена искуственно расставлены в UserServis.NAMES
}

fun getPlayerChipCount(playerChipCount: Int): Int {
    return playerChipCount
    // пользователь после изменения количества фишек отправляет своё количество фишек на сервер, что бы сервер обновил данные и вернул их пользователю для отображения в списках
    // Пока нет сервера, имена искуственно расставлены в UserServis.CHIPCOUNTS
}

fun getPlayerChipBet() {

}

fun getSumPlayerChipBet() {

}