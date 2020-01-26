package com.fnovellon.buildonmusic


import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ApplicationComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener


class BuildOnMusicApp : ApplicationComponent, ProjectManagerListener {

    private val musicPlayer = MusicPlayer()

    override fun getComponentName(): String = "Build on music component"

    override fun disposeComponent() {
        musicPlayer.switch.turnOff()
    }

    override fun initComponent() {
        val messageBus = ApplicationManager.getApplication().messageBus.connect()

        messageBus.subscribe(ProjectManager.TOPIC, this)
    }

    override fun projectOpened(project: Project) {
        CompilerTrigger(project).apply {
            addOnStartedListener {
                musicPlayer.switch.turnOn()
            }
            addOnFinishedListener {
                musicPlayer.switch.turnOff()
            }
        }
    }

}


