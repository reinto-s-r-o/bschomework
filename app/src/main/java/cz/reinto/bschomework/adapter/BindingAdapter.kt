package cz.reinto.bschomework.adapter

import android.view.View
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("app:visible")
fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("app:swipe_on_refresh")
fun SwipeRefreshLayout.refreshListener(listener: SwipeRefreshLayout.OnRefreshListener) {
    this.setOnRefreshListener(listener)
}

@BindingAdapter("app:swipe_refreshing")
fun SwipeRefreshLayout.refreshing(refreshing: Boolean) {
    this.isRefreshing = refreshing
}

@BindingAdapter("app:swipe_refresh_color")
fun SwipeRefreshLayout.refreshColor(@ColorInt color: Int) {
    this.setColorSchemeColors(color)
}
