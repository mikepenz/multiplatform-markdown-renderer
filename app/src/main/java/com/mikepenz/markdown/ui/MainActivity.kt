package com.mikepenz.markdown.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.Markdown

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainLayout()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
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
        """.trimIndent()

        val scrollState = rememberScrollState()

        Markdown(
            markdown,
            Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 48.dp, top = 56.dp)
        )
    }
}