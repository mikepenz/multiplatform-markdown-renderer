import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mikepenz.markdown.Markdown

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        SampleTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Markdown Sample") },
                    )
                }
            ) {
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

                Markdown(content, Modifier.fillMaxSize().padding(16.dp))
            }
        }
    }
}
