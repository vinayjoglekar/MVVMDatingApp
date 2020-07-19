package com.jovinz.datingapp.utils

import android.view.View
import android.widget.EditText


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun EditText.isValid(): Boolean {
    return text.isNullOrEmpty() && text?.isBlank()!!
}

fun EditText.disable() {
    isClickable = false
    isFocusable = false
}