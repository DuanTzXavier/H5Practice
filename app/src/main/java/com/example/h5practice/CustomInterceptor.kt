package com.example.h5practice

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.example.cleanweb.CustomWebView
import com.example.cleanweb.OverrideURLPlugin
import com.example.h5practice.logic.CustomLogicActivity

class CustomInterceptor(private val context: Activity): OverrideURLPlugin() {
    override fun provideInterceptBaseURLs(): List<String> {
        return listOf("index2")
    }

    override fun onIntercept(webView: CustomWebView, url: String) {
        Log.i("CustomInterceptor", "onIntercept")

        val intent = Intent(context, CustomLogicActivity::class.java)
        intent.putExtra("requestData", url)
        context.startActivityForResult(intent, 1)
    }
}