package com.example.cleanweb

abstract class OverrideURLPlugin {

    abstract fun provideInterceptBaseURLs(): List<String>

    abstract fun onIntercept(webView: CustomWebView, url: String)
}