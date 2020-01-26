package com.fnovellon.buildonmusic

import com.fnovellon.buildonmusic.SwitchOnOff.Companion.OFF
import javazoom.jl.player.advanced.AdvancedPlayer
import javazoom.jl.player.advanced.PlaybackEvent
import javazoom.jl.player.advanced.PlaybackListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.InputStream

class MusicPlayer {

    companion object {
        private const val MUSIC_PATH = "D:\\lab\\IntelliJ\\src\\main\\sounds\\e1.mp3"
    }

    val switch: SwitchOnOff = SwitchOnOff(OFF)

    init {
        switch.addOnValueChangedListener { isTurnedOn ->
            println("SWITCHED : $isTurnedOn")
            if (isTurnedOn) {
                GlobalScope.launch {
                    try {
                        playMusicFromFile(getInputStream())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.start()
            } else {
                //player?.stop()
                player?.close()
            }
        }
    }

    private var player: AdvancedPlayer? = null

    private fun playMusicFromFile(file: InputStream) {
        player = AdvancedPlayer(file)
        player?.apply {
            playBackListener = object : PlaybackListener() {
                override fun playbackFinished(evt: PlaybackEvent?) {
                    close()
                    playMusicFromFile(getInputStream())
                }
            }
            play()
        }
    }

    private fun getInputStream(): InputStream = BufferedInputStream(FileInputStream(MUSIC_PATH))

}


