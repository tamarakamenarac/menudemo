package com.example.menupracticaltestapp.helpers

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.widget.EditText
import androidx.annotation.ColorRes

fun EditText.setBackgroundTint(@ColorRes colorResource: Int) {
    val color = resources.getColor(colorResource)
    val drawable: Drawable = this.background
    drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    this.setBackgroundDrawable(drawable)
}