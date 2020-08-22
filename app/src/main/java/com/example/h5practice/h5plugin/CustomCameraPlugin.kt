package com.example.h5practice.h5plugin

import android.app.Activity
import android.content.Intent
import android.webkit.JavascriptInterface
import com.example.cleanweb.plugin.BaseH5Plugin
import com.example.h5practice.camera.CustomCameraActivity

class CustomCameraPlugin(private val context: Activity): BaseH5Plugin() {
    override fun pluginTag(): String {
        return "CustomCameraPlugin"
    }

    @JavascriptInterface
    fun startCustomCamera(requestData: String){
        val intent = Intent(context, CustomCameraActivity::class.java)
        intent.putExtra("requestData", requestData)
        context.startActivityForResult(intent, 2)
    }
}