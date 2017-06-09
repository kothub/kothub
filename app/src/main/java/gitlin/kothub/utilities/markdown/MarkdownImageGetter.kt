package gitlin.kothub.utilities.markdown

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import gitlin.kothub.R
import android.os.AsyncTask.execute
import android.os.AsyncTask
import android.text.Html.ImageGetter
import android.view.View
import java.io.InputStream


class MarkdownImageGetter(val context: Context) : Html.ImageGetter {
    override fun getDrawable(source: String): Drawable {

        val response = Fuel.get(source).response()
        val data = response.third.get()

        Log.i("MarkdownImageGetter", source)
        Log.i("MarkdownImageGetter", data.contentToString())

        val bitmap = BitmapFactory.decodeStream(data.inputStream())

        if (bitmap == null) {
            return context.resources.getDrawable(R.drawable.abc_spinner_mtrl_am_alpha)
        }

        val drawable = BitmapDrawable(context.resources, bitmap)
        drawable.setBounds(0, 0, bitmap.width, bitmap.height)
        return drawable
    }
}


class UrlImageParser(internal var container: View, internal var c: Context) : ImageGetter {

    override fun getDrawable(source: String): Drawable {
        val urlDrawable = UrlDrawable()

        // get the actual source
        val asyncTask = ImageGetterAsyncTask(urlDrawable)

        asyncTask.execute(source)

        // return reference to URLDrawable where I will change with actual image from
        // the src tag
        return urlDrawable
    }

    inner class ImageGetterAsyncTask(internal var urlDrawable: UrlDrawable) : AsyncTask<String, Void, Drawable>() {

        override fun doInBackground(vararg params: String): Drawable? {
            val source = params[0]
            return fetchDrawable(source)
        }

        override fun onPostExecute(result: Drawable?) {

            if (result == null) {
                urlDrawable.drawable = null
            }

            else {

                // set the correct bound according to the result from HTTP call
                urlDrawable.setBounds(0, 0, 0 + result!!.intrinsicWidth, 0 + result!!.intrinsicHeight)

                // change the reference of the current drawable to the result
                // from the HTTP call
                urlDrawable.drawable = result

                // redraw the image by invalidating the container
                this@UrlImageParser.container.invalidate()
            }
        }

        /***
         * Get the Drawable from URL
         * @param urlString
         * *
         * @return
         */
        fun fetchDrawable(urlString: String): Drawable? {
            try {
                val `is` = fetch(urlString)
                val drawable = Drawable.createFromStream(`is`, "src")
                drawable.setBounds(0, 0, 0 + drawable.intrinsicWidth, 0 + drawable.intrinsicHeight)
                return drawable
            } catch (e: Exception) {
                return null
            }

        }

        private fun fetch(urlString: String): InputStream {
            return Fuel.get(urlString).response().third.get().inputStream()
        }
    }

    inner class UrlDrawable : BitmapDrawable() {
        // the drawable that you need to set, you could set the initial drawing
        // with the loading image if you need to
        var drawable: Drawable? = null

        override fun draw(canvas: Canvas) {
            // override the draw to facilitate refresh function later
            if (drawable != null) {
                drawable?.draw(canvas)
            }
        }
    }
}

