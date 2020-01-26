package com.fnovellon.buildonmusic

import com.intellij.openapi.compiler.CompilerManager
import com.intellij.openapi.project.Project
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CompilerTrigger(
    project: Project
) {

    private val compilerManager = CompilerManager.getInstance(project)
    private var isCompiling = compilerManager.isCompilationActive

    private val compilationStartedListeners: MutableList<() -> Unit> = mutableListOf()
    private val compilationFinishedListeners: MutableList<() -> Unit> = mutableListOf()

    init {

        GlobalScope.launch {
            while (true) {
                compilerManager.isCompilationActive.let {
                    if (it != isCompiling) {
                        if (it) {
                            compilationStartedListeners.forEach{
                                it()
                            }
                        } else {
                            compilationFinishedListeners.forEach {
                                it()
                            }
                        }
                        isCompiling = it
                    }
                }
                delay(100)
            }
        }

    }

    fun addOnStartedListener(callback: () -> Unit) {
        compilationStartedListeners.add(callback)
    }

    fun addOnFinishedListener(callback: () -> Unit) {
        compilationFinishedListeners.add(callback)
    }
}