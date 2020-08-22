package com.example.h5practice.logic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.h5practice.R
import com.example.h5practice.utils.Utils

class CustomLogicActivity: Activity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_logic)

        val textView = findViewById<TextView>(R.id.text_from_js)
        val callbackdata = intent.getStringExtra("requestData")
        textView.text = "callback from url : $callbackdata"

        findViewById<Button>(R.id.center_button).setOnClickListener {
            val callbackEditText = findViewById<EditText?>(R.id.call_back_text)
            val text = callbackEditText?.text?.toString()
            val result = Intent()
            result.putExtra(Utils.CALL_BACK_DATA, text)
            setResult(1, result)
            finish()
        }

    }
}