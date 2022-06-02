package ru.jandroid.mysql_kotlin

// Точки входа JSON - для формирования запросов к серверу
object EndPoints {
    private val URL_ROOT = "https://a.jandroid.ru/mysql_artist/?op="
    val URL_ADD_ARTIST = URL_ROOT + "addartist"
    val URL_GET_ARTIST = URL_ROOT + "getartists"
    val URL_DEL_ARTIST = URL_ROOT + "delartist"
    val URL_UPD_ARTIST = URL_ROOT + "updartist"
}