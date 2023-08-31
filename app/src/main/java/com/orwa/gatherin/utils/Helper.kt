package com.orwa.gatherin.utils

import android.view.View


/**
 * Hide the current view
 */
fun View.hide(){
    visibility = View.INVISIBLE
}

/**
 * Hide the current view
 */
fun View.hideGone(){
    visibility = View.GONE
}

/**
 * Show the current view
 */
fun View.show(){
    visibility = View.VISIBLE
}