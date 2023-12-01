package com.mikepenz.markdown.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.Markdown

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
   val isSystemInDarkMode = isSystemInDarkTheme()
   var darkMode by remember { mutableStateOf(isSystemInDarkMode) }

    SampleTheme(darkMode) {

        val markdown = """
            # Title 1
        
            To get started with this library, just provide some Markdown.
           
            Usually Markdown will contain different characters *or different styles*, which can **change** just as you ~write~ different text. 
           
            Sometimes it will even contain images within the text
            
            ![Image](https://avatars.githubusercontent.com/u/1476232?v=4)
             
             Images can also be of different sizes
             
             ![Image](https://placehold.co/1000x200/png)
             
             ![Image](https://placehold.co/200x1000/png)
             
            After installing GPG Suite (or your preferred solution) first create a new key.
            
            Supports reference links:
            [Reference Link Test][1] 
            
            But can also be a auto link: https://mikepenz.dev
   
            
            Some `inline` code is also supported!
            
            ## Title 2
            
            ### Title 3 test
            
            Title 1
            ======
            
            Title 2
            ------
            
            [1]: https://mikepenz.dev/
            
            Unordered List
            - unordered list item 1
            - unordered list item 2
            - unordered list item 3
            
            Ordered List
            1. List item 1
            2. List item 2
            3. List item 3
            
            ```
            Code block test
            ```
            
            
            Links with links as label are also handled:
            [https://mikepenz.dev](https://mikepenz.dev)
            [https://github.com/mikepenz](https://github.com/mikepenz)
            [Mike Penz's Blog](https://blog.mikepenz.dev/)
        """.trimIndent()


        LazyColumn(
            modifier = Modifier
                .fillMaxSize().background(MaterialTheme.colors.background).padding(horizontal = 16.dp),
        ) {

                item {
                    Spacer(modifier = Modifier.height(56.dp))
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Dark mode enabled", color = MaterialTheme.colors.onBackground)
                        Switch(checked = darkMode, onCheckedChange = { darkMode = !darkMode })
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Markdown(markdown)
                }
                item {
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }

    }

