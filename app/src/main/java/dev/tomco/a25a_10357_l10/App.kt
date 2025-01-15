package dev.tomco.a25a_10357_l10

import android.app.Application
import dev.tomco.a25a_10357_l10.utilities.ImageLoader

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        ImageLoader.init(this)
    }
}