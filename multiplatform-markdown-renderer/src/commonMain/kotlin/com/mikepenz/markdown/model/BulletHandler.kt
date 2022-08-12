package com.mikepenz.markdown.model

/** An interface of providing use case specific un/ordered list handling.*/
fun interface BulletHandler {
    fun transform(bullet: CharSequence?): String
}
