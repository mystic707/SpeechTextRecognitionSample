package com.mysticagit.speechtextrecognition

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast

class SpeechRecogManager {

    private lateinit var speechRecognizer: SpeechRecognizer
    private var resultListener: SampleDataManager.RecognitionResultListener? = null


    fun setResultListener(listener: SampleDataManager.RecognitionResultListener) {
        resultListener = listener
    }

    /**
     * 음성 인식을 위한 인텐트 구성 및 인식 동작 시작
     */
    fun listenSpeech() {
        var intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, MainActivity.context.packageName)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.context)
        speechRecognizer.setRecognitionListener(recogListener)
        speechRecognizer.startListening(intent)
    }

    private val recogListener: RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            // 말하기 시작할 준비가 되었을 때
            Toast.makeText(MainActivity.context, "음성인식 시작", Toast.LENGTH_SHORT).show()
        }

        override fun onBeginningOfSpeech() {
            // 말하기 시작 시
            Toast.makeText(MainActivity.context, "듣는중", Toast.LENGTH_SHORT).show()
        }

        override fun onRmsChanged(rmsdB: Float) {
            // 입력받는 소리의 크기
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            // 말을 시작하고 인식된 단어를 반환
        }

        override fun onEndOfSpeech() {
            // 말하기 중지 시
        }

        override fun onError(error: Int) {
            // 에러
            resultListener?.let {
                it.onResult(SampleDataManager.RecognitionResult(
                        SampleDataManager.RecognitionResult.Result.ErrorCB,
                null,
                        error))
            }
        }

        override fun onResults(results: Bundle?) {
            // 인식 결과 준비되었을 때
            // (말한 단어가 리스트에 쌓여 인식 직전 준비 시)
            resultListener?.let {
                it.onResult(SampleDataManager.RecognitionResult(
                        SampleDataManager.RecognitionResult.Result.ResultCB,
                        getAllSpeechToText(results),
                    -1))
            }

            /**
            ERROR_NETWORK = 2 : Other network related errors
            ERROR_AUDIO = 3 : Audio recording error
            ERROR_SERVER = 4 : Server sends error status
            ERROR_CLIENT = 5 : Other client side errors
            ERROR_SPEECH_TIMEOUT = 6 : No speech input
            ERROR_NO_MATCH = 7 : No recognition result matched
            ERROR_RECOGNIZER_BUSY = 8 : RecognitionService busy
            ERROR_INSUFFICIENT_PERMISSIONS = 9 : Insufficient permissions
            ERROR_TOO_MANY_REQUESTS = 10 : Too many requests from the same client
            ERROR_SERVER_DISCONNECTED = 11 : Server has been disconnected, e.g. because the app has crashed
            ERROR_LANGUAGE_NOT_SUPPORTED = 12 : Requested language is not available to be used with the current recognizer
            ERROR_LANGUAGE_UNAVAILABLE = 13 : Requested language is supported, but not available currently (e.g. not downloaded yet)
            ERROR_CANNOT_CHECK_SUPPORT = 14 : The service does not allow to check for support
             */
        }

        override fun onPartialResults(partialResults: Bundle?) {
            // 부분 인식 결과를 사용할 수 있을 때
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            // 향후 이벤트를 추가하기 위한 예약
        }
    }

    private fun getAllSpeechToText(results: Bundle?): String {
        var allText = ""

        results?.let {
            var matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            matches?.let {
                for(match in it) {
                    allText += match
                    Log.d(SampleDataManager.LOG_TAG, "match : $match")
                }
            }
        }

        return allText
    }
}