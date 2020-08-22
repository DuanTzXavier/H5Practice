package com.example.h5practice.camera

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.h5practice.R
import com.example.h5practice.utils.Utils
import org.json.JSONObject

class CustomCameraActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_camera)

        val textView = findViewById<TextView>(R.id.text_from_js)
        val callbackdata = intent.getStringExtra("requestData")
        textView.text = "callback from url : $callbackdata"
        val jsonData = JSONObject(callbackdata)
        val sequenceID = jsonData.optString("sequenceID")
        findViewById<Button>(R.id.center_button).setOnClickListener {
            val callbackEditText = findViewById<EditText?>(R.id.call_back_text)
            val text = callbackEditText?.text?.toString()
            val result = Intent()
            result.putExtra(Utils.CALL_BACK_DATA, text)
            result.putExtra("sequenceID", sequenceID.toString())
            setResult(2, result)
            finish()
        }
    }
}