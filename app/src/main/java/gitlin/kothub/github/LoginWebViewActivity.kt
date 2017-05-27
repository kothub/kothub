package gitlin.kothub.github

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView

/**
 * Created by Florian on 27/05/2017.
 */
class LoginWebViewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webview = WebView(this)
        webview.clearCache(true)
        webview.loadUrl(intent.getStringExtra("url"))
        webview.setWebViewClient(LoginWebView(this))
        setContentView(webview)
    }

}