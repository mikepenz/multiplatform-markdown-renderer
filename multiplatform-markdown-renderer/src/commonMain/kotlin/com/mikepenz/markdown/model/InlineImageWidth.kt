package com.mikepenz.markdown.model

/**
 * This enum is required as an inline image is not a separate composable and cannot rely on Compose measurements.
 * Instead, an inline image size should be reported as an inline placeholder size.
 * See [ImageTransformer.placeholderConfig]
 * */
enum class InlineImageWidth {
    IMAGE_WIDTH,
    MAX_WIDTH,
}
