package gitlin.kothub.github

import android.app.Activity
import android.os.Bundle
import android.webkit.WebView


class LoginWebViewActivity: Activity() {

    lateinit var webview: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webview = WebView(this)
        webview.settings.javaScriptEnabled = true
        webview.loadUrl(intent.getStringExtra("url"))
        webview.setWebViewClient(LoginWebView(this))
        setContentView(webview)
    }

    override fun onDestroy() {
        super.onDestroy()
        webview.clearCache(true)
        webview.clearHistory()
    }

}