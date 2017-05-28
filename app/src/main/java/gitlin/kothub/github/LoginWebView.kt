package gitlin.kothub.github

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


class LoginWebView(val context: LoginWebViewActivity): WebViewClient() {

    // dialog = ProgressDialog(context)

    init {
        //  dialog.setTitle("Loading...")
        //dialog.progress = 0
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        //dialog.show()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        //dialog.dismiss()
    }


    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

        val result = shouldOverrideUrlLoading(view, Uri.parse(url))
        return if (result) result else super.shouldOverrideUrlLoading(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

        val result = shouldOverrideUrlLoading(view, request?.url)
        return if (result) result else super.shouldOverrideUrlLoading(view, request)
    }

    fun shouldOverrideUrlLoading(view: WebView?, url: Uri?): Boolean {
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
