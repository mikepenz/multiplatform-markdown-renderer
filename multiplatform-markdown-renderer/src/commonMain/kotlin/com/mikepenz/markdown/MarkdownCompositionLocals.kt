package com.mikepenz.markdown

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * The CompositionLocal to provide functionality related to transforming the bullet of an ordered list
 */
val LocalBulletListHandler = staticCompositionLocalOf {
    return@staticCompositionLocalOf BulletHandler { "• " }
}

/**
 * The CompositionLocal to provide functionality related to transforming the bullet of an ordered list
 */
val LocalOrderedListHandler = staticCompositionLocalOf {
    return@staticCompositionLocalOf BulletHandler { "$it " }
}


/** An interface of providing use case specific un/ordered list handling.*/
fun interface BulletHandler {
    /** Transforms the bullet icon */
    fun transform(bullet: CharSequence?): String
}