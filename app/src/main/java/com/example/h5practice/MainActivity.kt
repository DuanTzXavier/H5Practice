package com.example.h5practice

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.webkit.ValueCallback
import android.webkit.WebView
import android.widget.Toast
import com.example.cleanweb.CustomWebView
import com.example.cleanweb.ICustomWebViewProvider
import com.example.cleanweb.plugin.BaseH5Plugin
import com.example.h5practice.h5plugin.CustomCameraPlugin
import com.example.h5practice.utils.Utils
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private var customWebView: CustomWebView? = null
    private val mHandle = Handler(Looper.getMainLooper())
    private var isResume = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        customWebView = findViewById<CustomWebView>(R.id.custom_web_view)
        customWebView?.let {
            initWebView(it)
        }
        customWebView?.loadUrl("https://000526367.production.codepen.plumbing")
    }

    private fun initWebView(webView: CustomWebView){
        val settings = webView.settings
        settings.javaScriptEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        webView.injectURLInterceptor(CustomInterceptor(this))
        webView.initFromProvider(object : ICustomWebViewProvider{
            override fun provideH5Plugins(): List<BaseH5Plugin> {
                return listOf(CustomCameraPlugin(this@MainActivity))
            }

            override fun provideUserAgent(): String {
                return "ABC"
            }

            override fun provideAppVersionCode(): String {
                return "1.1"
            }

        })
    }

    override fun onResume() {
        super.onResume()
        isResume = true
    }

    override fun onPause() {
        super.onPause()
        isResume = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        runOnResume{
            handleCallback(requestCode, resultCode, data)
        }
    }

    private fun handleCallback(requestCode: Int, resultCode: Int, data: Intent?){
        if (requestCode == 1){
            val responseData = data?.getStringExtra(Utils.CALL_BACK_DATA)
            customWebView?.loadUrl("https://000526367.production.codepen.plumbing/success.html?customdata=$responseData")
        }else if (requestCode == 2){
            val responseData = data?.getStringExtra(Utils.CALL_BACK_DATA)
            val sequenceID = data?.getStringExtra("sequenceID")
            val responseJson = JSONObject()
            responseJson.put("responseString", responseData.toString())
            responseJson.put("sequenceID", sequenceID)
            customWebView?.callbackToJS(responseJson.toString())
        }
    }

    private fun runOnResume(run: () -> Unit?){
        if (!isResume){
            mHandle.postDelayed({
                runOnResume(run)
            }, 100)
        }else{
            run.invoke()
        }
    }

}