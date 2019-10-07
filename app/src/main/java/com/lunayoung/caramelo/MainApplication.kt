package com.lunayoung.caramelo

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import timber.log.Timber


class MainApplication : Application(){

    private val PRINT_STACK_COUNT : Int =  5

    init {
        instance = this
    }

    companion object {
        private var instance: MainApplication? = null

        fun applicationContext() : MainApplication {
            return instance as MainApplication
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Logger Stack trace을 PRINT_STACK_COUNT 개로 조절
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .methodCount(PRINT_STACK_COUNT)
            .build()

        // Release 빌드 시 로그 제거
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
}