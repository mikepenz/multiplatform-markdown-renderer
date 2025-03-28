package com.mikepenz.markdown.ui.m2

import androidx.compose.runtime.Composable
import com.mikepenz.markdown.ui.annotation.DarkLightPreview
import com.mikepenz.markdown.ui.m2.util.TestMarkdown

/**
 * Covers links and reference behavior as documented
 * https://github.com/adam-p/markdown-here/wiki/markdown-cheatsheet#links
 */
class LinkTests {
    @DarkLightPreview
    @Composable
    fun LinkTest() = TestMarkdown(
        """
        [I'm an inline-style link](https://www.google.com)

        [I'm an inline-style link with title](https://www.google.com "Google's Homepage")
        
        [I'm a reference-style link][Arbitrary case-insensitive reference text]
        
        [I'm a relative reference to a repository file](../blob/master/LICENSE)
        
        [You can use numbers for reference-style link definitions][1]
        
        Or leave it empty and use the [link text itself].
        
        URLs and URLs in angle brackets will automatically get turned into links. 
        http://www.example.com or <http://www.example.com> and sometimes 
        example.com (but not on Github, for example).
        
        Some text to show that the reference links can follow later.
        
        [arbitrary case-insensitive reference text]: https://www.mozilla.org
        [1]: http://slashdot.org
        [link text itself]: http://www.reddit.com
        """.trimIndent()
    )
}