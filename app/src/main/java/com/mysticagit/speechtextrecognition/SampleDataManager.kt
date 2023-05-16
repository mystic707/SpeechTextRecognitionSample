package com.mysticagit.speechtextrecognition

object SampleDataManager {

    const val LOG_TAG = "mysticagit"

    data class RecognitionResult(val result: Result, val recogText: String?, val error: Int) {

        enum class Result(value: Int) {
            ResultCB(0),
            ErrorCB(1),
        }
    }

    interface RecognitionResultListener {

        fun onResult(result: RecognitionResult)
    }
}