package com.homelaunch.assist

import android.content.Intent
import android.speech.RecognitionService

/**
 * We don't do any voice recognition - this class exists only because
 * Android requires a RecognitionService to be declared for the app to
 * show up under Settings > Apps > Default apps > Digital assistant app.
 */
class StubRecognitionService : RecognitionService() {
    override fun onStartListening(recognizerIntent: Intent?, listener: Callback?) {
        // Not used.
    }

    override fun onCancel(listener: Callback?) {
        // Not used.
    }

    override fun onStopListening(listener: Callback?) {
        // Not used.
    }
}
