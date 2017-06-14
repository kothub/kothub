package gitlin.kothub.accounts

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


class LoginWebView(val context: LoginWebViewActivity): WebViewClient() {


    @SuppressWarnings("deprecated")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        val result = shouldOverrideUrlLoading(Uri.parse(url))
        return if (result) result else super.shouldOverrideUrlLoading(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

        val result = shouldOverrideUrlLoading(request?.url)
        return if (result) result else super.shouldOverrideUrlLoading(view, request)
    }

    fun shouldOverrideUrlLoading(url: Uri?): Boolean {
        if (url != null) {
            if (url.scheme == "oauth" && url.host == "kothub") {
                val intent = Intent()
                intent.data = url
                context.setResult(Activity.RESULT_OK, intent)
                context.finish()
                return true
            }
        }

        return false
    }
}
