import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.model.MarkdownColors
import com.mikepenz.markdown.model.MarkdownPadding
import com.mikepenz.markdown.model.MarkdownTypography

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
                
                To get started you will need GPG on your computer to create a new key pair. 
                
                Usually I go with the [GPG Suite](https://gpgtools.org/), which offers a nice UI to manage your keys. (You may want to use advanced installation to only install the parts you need)
                But you can also use any other solution to create your key pair.
                
                ![Image](https://avatars.githubusercontent.com/u/1476232?v=4)
                
                After installing GPG Suite (or your prefered solution) first create a new key.
                
                Using GPG Suite, start the GPG Keychain, which shows all current known GPG keys on your system.
                
                Click on `New` and you will be offered to enter your name, e-mail and password. Ensure to pick a secure password to protect your key. 
                """.trimIndent()
                Markdown(
                    content,
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
                    modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState)
                )
            }
        }
    }
}
