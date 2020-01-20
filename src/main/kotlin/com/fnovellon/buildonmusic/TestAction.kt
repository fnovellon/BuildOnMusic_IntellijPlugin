package com.fnovellon.buildonmusic

import android.app.NotificationManager
import com.android.tools.idea.project.AndroidProjectBuildNotifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.compiler.CompilationStatusListener
import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompilerManager
import com.intellij.openapi.compiler.CompilerTopics
import com.intellij.openapi.components.ApplicationComponent
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.task.ProjectTaskContext
import com.intellij.task.ProjectTaskListener
import com.intellij.task.ProjectTaskResult
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.messages.MessageBusListener
import javazoom.jl.player.advanced.AdvancedPlayer
import javazoom.jl.player.advanced.PlaybackEvent
import javazoom.jl.player.advanced.PlaybackListener
import kotlinx.coroutines.*
import org.jetbrains.android.compiler.tools.AndroidApkBuilder
import java.awt.Point
import java.io.*
import java.nio.channels.FileChannel
import javax.management.Notification


class MusicPlayer {

    companion object {
        private const val MUSIC_PATH = "D:\\buildonmusic\\src\\main\\sounds\\e1.mp3"
    }

    public var isMusicPlaying = false
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
            play()
        }
    }

    private fun getInputStream(): InputStream = BufferedInputStream(FileInputStream(MUSIC_PATH))

}

class BuildOnMusicApp : ApplicationComponent {

    private val musicPlayer = MusicPlayer()

    override fun getComponentName(): String = "Build on music component"

    override fun disposeComponent() {
        println("goodbye")
    }

    override fun initComponent() {
        println("Hello")
        JBPopupFactory.getInstance().createMessage("Hello").show(RelativePoint(Point(100, 100)))
        val project = ProjectManager.getInstance().defaultProject
        val compilerManager = CompilerManager.getInstance(project)

        compilerManager.addBeforeTask {
            println("AVANT")
            JBPopupFactory.getInstance().createMessage("AVANT").show(RelativePoint(Point(100, 100)))
            true
        }

        compilerManager.addAfterTask {
            println("APRES")
            JBPopupFactory.getInstance().createMessage("APRES").show(RelativePoint(Point(100, 100)))
            true
        }

        GlobalScope.launch {
            spyCompilation()
        }


    }

    private suspend fun spyCompilation() {
        while (true) {
            val project = ProjectManager.getInstance().defaultProject
            val compilerManager = CompilerManager.getInstance(project)
            println(compilerManager.isCompilationActive)
            delay(500)
        }
    }



}
