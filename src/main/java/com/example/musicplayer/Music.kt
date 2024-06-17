package com.example.musicplayer

import java.util.concurrent.TimeUnit

data class Music(
    val id: String,
    val title: String,
    val album: String,
    val artist: String,
    val duration: Long = 0,
    val path: String,
    val artUri: String
)

fun formateDuration(duration: Long):String{

    var minute = TimeUnit.MINUTES.convert(duration,TimeUnit.MILLISECONDS)
    var second = (TimeUnit.SECONDS.convert(duration,TimeUnit.MILLISECONDS) - minute*TimeUnit.SECONDS.convert(1,TimeUnit.MINUTES))

    return  String.format("%2d: %2d",minute,second)
}