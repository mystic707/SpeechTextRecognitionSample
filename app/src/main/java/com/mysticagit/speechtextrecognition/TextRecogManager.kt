package com.mysticagit.speechtextrecognition

import android.speech.tts.TextToSpeech
import java.util.*

class TextRecogManager {

    lateinit var tts: TextToSpeech

    init {
        tts = TextToSpeech(MainActivity.context, object : TextToSpeech.OnInitListener {
            override fun onInit(status: Int) {
                if(status != android.speech.tts.TextToSpeech.ERROR) {
                    tts.language = Locale.KOREAN
                }
            }
        })
    }

    @Synchronized
    fun speakWithText(text: String?) {
        text?.let {
            tts.setPitch(1.0f)  // 목소리 톤
            tts.setSpeechRate(1.0f) // 목소리 속도
            tts.speak(it, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }
}