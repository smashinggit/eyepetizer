package com.cs.common.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.toastLong(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Fragment.toast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.toastLong(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
}