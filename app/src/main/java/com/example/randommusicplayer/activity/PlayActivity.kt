package com.example.randommusicplayer.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.randommusicplayer.R

class PlayActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        webView = findViewById(R.id.webView)
        val url = intent.getStringExtra("URL")
        url?.let { play(it) }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun play(url: String){
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient =  object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                val resources = request?.resources
                for (i in resources?.indices!!) {
                    if (PermissionRequest.RESOURCE_PROTECTED_MEDIA_ID == resources[i]) {
                        request.grant(resources)
                        return
                    }
                }
                super.onPermissionRequest(request)
            }
        }
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if(webView.canGoBack()) webView.goBack()
        else {
            super.onBackPressed()
            finish()
        }
    }

    override fun onDestroy() {
        webView.clearHistory()
        webView.clearFormData()
        webView.clearMatches()
        webView.destroy()
        super.onDestroy()
    }
}

