package com.app.notely

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp(Application::class)
class NotelyApplication : Hilt_NotelyApplication()