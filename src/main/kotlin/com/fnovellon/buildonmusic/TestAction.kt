package com.fnovellon.buildonmusic

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.compiler.CompilationStatusListener
import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompilerManager
import com.intellij.openapi.compiler.CompilerTopics
import com.intellij.util.messages.MessageBusListener
import javazoom.jl.player.advanced.AdvancedPlayer
import javazoom.jl.player.advanced.PlaybackEvent
import javazoom.jl.player.advanced.PlaybackListener
import kotlinx.coroutines.*
import java.io.*
import java.nio.channels.FileChannel


class TestAction : AnAction() {

    companion object {
        private const val MUSIC_PATH = "D:\\buildonmusic\\src\\main\\sounds\\e1.mp3"
    }

    private var isMusicPlaying = false
        set(value) {
            if (field != value) {
                field = value
                setMusic(value)
            }
        }

    private var player: AdvancedPlayer? = null

    private fun setMusic(shouldPlay: Boolean) {
        if (shouldPlay) {
            GlobalScope.launch {
                try {
                    playMusicFromFile(getInputStream())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        } else {
            player?.stop()
            player?.close()
        }
    }

    private fun playMusicFromFile(file: InputStream) {
        player = AdvancedPlayer(file).apply {
            playBackListener = object : PlaybackListener() {
                override fun playbackFinished(evt: PlaybackEvent?) {
                    if (isMusicPlaying) {
                        close()
                        playMusicFromFile(getInputStream())
                    }
                }
            }
            play(0, 100)
        }
    }

    private fun getInputStream(): InputStream = BufferedInputStream(FileInputStream(MUSIC_PATH))

    override fun update(event: AnActionEvent) {
        val project = event.project
        event.presentation.isEnabledAndVisible = project != null
    }

    override fun actionPerformed(event: AnActionEvent) {
        val compilerManager = CompilerManager.getInstance(event.project)
        val isCompiling = compilerManager.isCompilationActive
        isMusicPlaying = isCompiling



    }

}
