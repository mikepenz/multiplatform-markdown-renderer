package com.mikepenz.markdown.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
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
        ProvideWindowInsets {
            Scaffold(
                modifier = Modifier.background(MaterialTheme.colors.primarySurface),
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.systemBars,
                    applyTop = false,
                    applyBottom = true,
                )
            ) {
                val markdown = """
                    ### Getting Started
                    
                    To get started you will need GPG on your computer to create a new key pair. 
                    
                    Usually I go with the [GPG Suite](https://gpgtools.org/), which offers a nice UI to manage your keys. (You may want to use advanced installation to only install the parts you need)
                    But you can also use any other solution to create your key pair.
                    
                    After installing GPG Suite (or your prefered solution) first create a new key.
                    
                    Using GPG Suite, start the GPG Keychain, which shows all current known GPG keys on your system.
                    
                    Click on `New` and you will be offered to enter your name, e-mail and password. Ensure to pick a secure password to protect your key. 
                """.trimIndent()

                Markdown(markdown, Modifier.fillMaxSize())
            }
        }
    }
}