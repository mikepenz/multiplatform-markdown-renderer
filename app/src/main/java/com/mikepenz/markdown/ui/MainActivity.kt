package com.mikepenz.markdown.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.model.MarkdownColors
import com.mikepenz.markdown.model.MarkdownPadding
import com.mikepenz.markdown.model.MarkdownTypography

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainLayout()
        }
    }
}

@Composable
fun MainLayout() {
    SampleTheme {
        val markdown = """
            # Title 1
        
            To get started with this library, just provide some Markdown.
           
            Usually Markdown will contain different characters *or different styles*, which can **change** just as you ~write~ different text. 
           
            Sometimes it will even contain images within the text
            
            ![Image](https://avatars.githubusercontent.com/u/1476232?v=4)
                
            After installing GPG Suite (or your preferred solution) first create a new key.
            
            Supports reference links:
            [Reference Link Test][1] 
            
            But can also be a auto link: https://mikepenz.dev
            
            Links with links as label are also handled:
            [https://mikepenz.dev](https://mikepenz.dev)
            
            Using GPG Suite, start the GPG Keychain, which shows all current known GPG keys on your system.
            
            Some `inline` code is also supported!
            
            ## Title 2
            
            ### Title 3
            
            [1]: https://mikepenz.dev/
            
            - [Text] Some text
        """.trimIndent()

        val scrollState = rememberScrollState()

        Markdown(
            markdown,
            typography = object : MarkdownTypography {
                override val text: TextStyle
                    get() = TextStyle(color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.W500)
                override val code: TextStyle
                    get() = TextStyle(color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.W500, fontStyle = FontStyle.Normal)
                override val h1: TextStyle
                    get() = TextStyle(color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.W600)
                override val h2: TextStyle
                    get() = TextStyle(color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.W600)
                override val h3: TextStyle
                    get() = TextStyle(color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.W600)
                override val h4: TextStyle
                    get() = TextStyle(color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.W600)
                override val h5: TextStyle
                    get() = TextStyle(color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.W600)
                override val h6: TextStyle
                    get() = TextStyle(color = Color.Black, fontSize = 17.sp, fontWeight = FontWeight.W600)
                override val quote: TextStyle
                    get() = TextStyle(color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.W500, fontStyle = FontStyle.Normal)
                override val paragraph: TextStyle
                    get() = TextStyle(color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.W600)
                override val ordered: TextStyle
                    get() = TextStyle(color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.W500)
                override val bullet: TextStyle
                    get() = TextStyle(color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.W500)
                override val list: TextStyle
                    get() = TextStyle(color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.W500)
            },
            colors = object : MarkdownColors {
                override val text: Color
                    get() = Color.Black
                override val backgroundCode: Color
                    get() = Color.Black
            },
            padding = object : MarkdownPadding {
                override val block: Dp
                    get() = 2.dp
                override val list: Dp
                    get() = 1.dp
                override val indentList: Dp
                    get() = 4.dp
            },
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 48.dp, top = 56.dp)
        )
    }
}