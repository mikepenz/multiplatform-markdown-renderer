<h1 align="center">
  Kotlin Multiplatform Markdown Renderer
</h1>

<p align="center">
    ... a Kotlin Multiplatform Markdown Renderer. (Android, Desktop, ...) powered by Compose Multiplatform
</p>

<div align="center">
  <a href="https://github.com/mikepenz/multiplatform-markdown-renderer/actions">
		<img src="https://github.com/mikepenz/multiplatform-markdown-renderer/workflows/CI/badge.svg"/>
	</a>
</div>
<br />

-------

<p align="center">
    <a href="#whats-included-">What's included 🚀</a> &bull;
    <a href="#setup">Setup 🛠️</a> &bull;
    <a href="#usage">Usage 🛠️</a> &bull;
    <a href="#license">License 📓</a>
</p>

-------

### What's included 🚀

- Super simple setup
- Cross-platform ready
- Lightweight

-------

## Setup
### Using Gradle

<details open><summary><b>Multiplatform</b></summary>
<p>

For multiplatform projects specify this single dependency:

```kotlin
dependencies {
    implementation("com.mikepenz:multiplatform-markdown-renderer:${version}")
}
```

</p>
</details>

<details><summary><b>JVM</b></summary>
<p>

To use the library on JVM, you have to include:
```kotlin
dependencies {
    implementation("com.mikepenz:multiplatform-markdown-renderer-jvm:${version}")
}
```

</p>
</details>

<details><summary><b>Android</b></summary>
<p>

For Android a special dependency is available:
```kotlin
dependencies {
    implementation("com.mikepenz:multiplatform-markdown-renderer-android:${version}")
}
```

</p>
</details>

-------

## Usage

```Kotlin
val markdown = """
### What's included 🚀

- Super simple setup
- Cross-platform ready
- Lightweight
""".trimIndent()

//
Markdown(markdown, Modifier.fillMaxSize())
```

## Dependency

This project uses JetBrains [markdown](https://github.com/JetBrains/markdown/) Multiplatform Markdown processor as dependency to parse the markdown content.

## Developed By

* Mike Penz
 * [mikepenz.com](http://mikepenz.com) - <mikepenz@gmail.com>
 * [paypal.me/mikepenz](http://paypal.me/mikepenz)

## Contributors

This free, open source software was also made possible by a group of volunteers that put many hours of hard work into it. See the [CONTRIBUTORS.md](CONTRIBUTORS.md) file for details.

## Credits

Big thanks to [Erik Hellman](https://twitter.com/ErikHellman) and his awesome article on [Rendering Markdown with Jetpack Compose](https://www.hellsoft.se/rendering-markdown-with-jetpack-compose/)

## License

    Copyright 2021 Mike Penz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
