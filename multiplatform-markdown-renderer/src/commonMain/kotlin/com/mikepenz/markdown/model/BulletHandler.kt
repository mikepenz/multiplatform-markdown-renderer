package com.mikepenz.markdown.model

interface BulletHandler {
    fun transform(bullet: CharSequence?): String
}
