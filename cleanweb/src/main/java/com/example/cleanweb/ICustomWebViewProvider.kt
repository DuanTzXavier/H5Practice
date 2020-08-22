package com.example.cleanweb

import com.example.cleanweb.plugin.BaseH5Plugin

interface ICustomWebViewProvider {

    fun provideH5Plugins(): List<BaseH5Plugin>

    fun provideUserAgent(): String

    fun provideAppVersionCode(): String
}