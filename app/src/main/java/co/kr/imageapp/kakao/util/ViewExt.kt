package co.kr.imageapp.kakao.util

import android.app.Service
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import co.kr.imageapp.kakao.R
import com.squareup.picasso.Picasso

fun ImageView.loadImage(@DrawableRes resId: Int) = Picasso.get().load(resId).into(this)
fun ImageView.loadVideo(url: String) {
    Picasso.get().load(url).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(this)
}

fun ImageView.loadImage(url: String, width: Int, height: Int) {
    if (width < 500) {
        this.layoutParams = ViewGroup.LayoutParams(500, 600)
        Picasso.get().load(url).resize(500, 600).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(this)
    } else {
        this.layoutParams = ViewGroup.LayoutParams(width, height)
        Picasso.get().load(url).resize(width, height).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(this)
    }
}

fun View.showKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.GONE
}

fun View.showToast(
    lifecycleOwner: LifecycleOwner,
    ToastEvent: LiveData<SingleEvent<Any>>,
    timeLength: Int
) {

    ToastEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            when (it) {
                is String -> Toast.makeText(this.context, it, timeLength).show()
                is Int -> Toast.makeText(this.context, this.context.getString(it), timeLength).show()
                else -> {
                }
            }
        }
    })
}
