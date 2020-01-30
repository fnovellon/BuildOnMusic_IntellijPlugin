package com.fnovellon.buildonmusic

import com.intellij.openapi.vfs.VfsUtil
import com.intellij.util.ResourceUtil
import javazoom.jl.player.advanced.AdvancedPlayer
import javazoom.jl.player.advanced.PlaybackEvent
import javazoom.jl.player.advanced.PlaybackListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.lang.IllegalArgumentException

class MusicPlayer {

    companion object {
        private const val SOUNDS_DIR = "sounds"
        private const val ELEVATOR_MUSIC_PATH = "e1.mp3"
    }

    var switch: Boolean by switchable(false) { _, _, isTurnedOn ->
        if (isTurnedOn) {
            GlobalScope.launch {
                try {
                    playMusicFromFile(getInputStream())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        } else {
            player?.close()
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

    private fun getInputStream(): InputStream {
        val url = ResourceUtil.getResource(MusicPlayer::class.java, SOUNDS_DIR, ELEVATOR_MUSIC_PATH)
        val virtualFile = VfsUtil.findFileByURL(url)
        return BufferedInputStream(virtualFile!!.inputStream)
    }

}
