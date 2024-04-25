import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mikepenz.markdown.m2.Markdown

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Markdown Sample") {
        SampleTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Markdown Sample") }
                    )
                }
            ) {
                val scrollState = rememberScrollState()

                val content = """
                ### Getting Started
                
                For multiplatform projects specify this single dependency:
                
                ```
                dependencies {
                    implementation("com.mikepenz:multiplatform-markdown-renderer:{version}")
                }
                ```
                
                You can find more information on [GitHub](https://github.com/mikepenz/multiplatform-markdown-renderer). More Text after this.
                
                ![Image](https://avatars.githubusercontent.com/u/1476232?v=4)
                
                There are many more things which can be experimented with like, inline `code`. 
                
                
                Title 1
                ======
                
                Title 2
                ------
                              
                [https://mikepenz.dev](https://mikepenz.dev)
                [https://github.com/mikepenz](https://github.com/mikepenz)
                [Mike Penz's Blog](https://blog.mikepenz.dev/)
                """.trimIndent()
                Markdown(
                    content,
                    modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(16.dp)
                )
            }
        }
    }
}
