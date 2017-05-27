package gitlin.kothub.github

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


class LoginWebView(val context: LoginWebViewActivity): WebViewClient() {

    val dialog = ProgressDialog(context)

    init {
        dialog.setTitle("Loading")
        dialog.progress = 0
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        //dialog.show()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        dialog.dismiss()
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (request != null) {
            val url = request.url

            if (url.scheme == "oauth" && url.host == "kothub") {
                val data = Intent()
                data.data = url
                context.setResult(Activity.RESULT_OK, data)
                context.finish()
                return true
            }
        }

        return super.shouldOverrideUrlLoading(view, request)
    }
}