package com.android.clearscore.main

import android.app.Application
import com.android.clearscore.di.appModule
import com.android.di.dataModule
import com.android.di.domainModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Napier.base(DebugAntilog())

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(dataModule, domainModule, appModule)
        }
    }
}
