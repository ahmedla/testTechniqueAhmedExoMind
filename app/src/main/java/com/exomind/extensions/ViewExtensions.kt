package com.exomind.extensions

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * View is shown
 **/
internal fun View.show() {
    this.visibility = View.VISIBLE
}

/**
 * View doesn't take any space for layout purposes
 **/
internal fun View.hide() {
    this.visibility = View.INVISIBLE
}

/**
 * Setup item decoration for recycler view while defining orientation too
 **/
internal fun RecyclerView.setupItemDecoration(context: Context, layoutOrientation: Int) {
    val linearLayoutManager = LinearLayoutManager(context, layoutOrientation, false)
    val itemDecoration = DividerItemDecoration(
        context,
        layoutOrientation
    )
    layoutManager = linearLayoutManager
    addItemDecoration(itemDecoration)
}