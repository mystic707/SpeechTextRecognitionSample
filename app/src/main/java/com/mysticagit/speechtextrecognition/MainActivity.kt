package com.mysticagit.speechtextrecognition

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var srManager: SpeechRecogManager
    private lateinit var trManager: TextRecogManager

    companion object {
        lateinit var context: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this
        srManager = SpeechRecogManager()
        trManager = TextRecogManager()

        // UI 설정
        initUI()

        // 음성 인식 결과 리스너 등록
        srManager.setResultListener(resultListener)
    }

    /**
     * 메인 액티비티 UI 구성
     */
    private fun initUI() {
        var btnRequestPermission = findViewById<Button>(R.id.btn_request_permission)
        var btnListenSpeech = findViewById<Button>(R.id.btn_listen_speech)
        var btnSpeakText = findViewById<Button>(R.id.btn_speak_text)
        var etSpeakText = findViewById<EditText>(R.id.et_speak_text)

        btnRequestPermission.setOnClickListener {
            requestPermission()
        }

        btnListenSpeech.setOnClickListener {
            srManager.listenSpeech()
        }

        btnSpeakText.setOnClickListener {
            if(etSpeakText.text.toString().isEmpty()) {
                Toast.makeText(MainActivity.context, "text is empty", Toast.LENGTH_SHORT).show()
            } else {
                trManager.speakWithText(etSpeakText.text.toString())
            }
        }
    }

    private val permissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
        //
    }

    /**
     * 권한 요청
     */
    private fun requestPermission() {
        permissionRequest.launch(arrayOf(
            android.Manifest.permission.RECORD_AUDIO))
    }

    /**
     * 음성 인식 결과 처리
     */
    private val resultListener = object : SampleDataManager.RecognitionResultListener {
        override fun onResult(result: SampleDataManager.RecognitionResult) {
            when(result.result) {
                SampleDataManager.RecognitionResult.Result.ResultCB -> {
                    var tvSpeechData = findViewById<TextView>(R.id.tv_listen_speech)
                    tvSpeechData.text = result.recogText
                }
                SampleDataManager.RecognitionResult.Result.ErrorCB -> {
                    Toast.makeText(MainActivity.context, "error : ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}