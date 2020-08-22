package com.example.cleanweb

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Base64
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.cleanweb.plugin.BaseH5Plugin

class CustomWebView : WebView {

    private val mHandler = Handler(Looper.getMainLooper())

    private val JS_CALLBACK_FUNCTION = "javascript:window.__callback('%s')"

    private var mInterceptor: OverrideURLPlugin? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributes: AttributeSet?) : this(context, attributes, 0)

    constructor(context: Context, attributes: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributes,
        defStyleAttr
    )

    fun initFromProvider(provider: ICustomWebViewProvider) {
        addJavascriptInterface(provider.provideH5Plugins())
        setUserAgent(provider.provideUserAgent(), provider.provideAppVersionCode())
        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (view == null || url == null) return false
                var isShouldOverrideUrlLoading: Boolean? = false
                mInterceptor?.provideInterceptBaseURLs()?.run {
                    for (item in this) {
                        isShouldOverrideUrlLoading = !TextUtils.isEmpty(item) && url.contains(item, false)
                        if (isShouldOverrideUrlLoading == true) {
                            mInterceptor?.onIntercept(this@CustomWebView, url)
                            break
                        }
                    }
                }
                return isShouldOverrideUrlLoading ?: false
            }
        }
    }

    fun injectURLInterceptor(interceptor: OverrideURLPlugin) {
        mInterceptor = interceptor
    }

    fun callbackToJS(responseData: String){
        mHandler.post {
            processJSFunction(String.format(JS_CALLBACK_FUNCTION, Base64.encodeToString(responseData.toByteArray(), Base64.NO_WRAP)))
        }
    }

    private fun processJSFunction(responseData: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(responseData) { }
        }else{
            loadUrl(responseData)
        }
    }

    @SuppressLint("AddJavascriptInterface")
    private fun addJavascriptInterface(plugins: List<BaseH5Plugin>?) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            plugins?.forEach {
                addJavascriptInterface(it, it.pluginTag())
            }
        }
    }

    private fun setUserAgent(customUserAgent: String?, versionCode: String?) {
        if (customUserAgent == "" || customUserAgent == null) return
        var currentUserAgent = settings.userAgentString
        if (!currentUserAgent.contains(customUserAgent)) {
            currentUserAgent += "_${customUserAgent}_$versionCode"
            settings.userAgentString = currentUserAgent
        }
    }
}
