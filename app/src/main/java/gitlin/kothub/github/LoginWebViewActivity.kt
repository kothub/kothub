package gitlin.kothub.github

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.webkit.WebView


class LoginWebViewActivity: AppCompatActivity() {

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