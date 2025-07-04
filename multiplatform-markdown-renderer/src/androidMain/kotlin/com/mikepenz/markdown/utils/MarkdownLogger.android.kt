package com.mikepenz.markdown.utils

import android.util.Log

internal actual fun platformLog(tag: String, message: String) {
    Log.d(tag, message)
}