package com.homelaunch.assist

import android.service.voice.VoiceInteractionService

/**
 * Marker service that tells Android this app can act as a
 * Digital Assistant. All real work happens in [HomeLaunchSession].
 */
class HomeLaunchAssistantService : VoiceInteractionService()
