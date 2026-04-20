# Markdown Playground

---

# This is an H1

## This is an H2

### This is an H3

#### This is an H4

##### This is an H5

###### This is an H6

This is a paragraph with some *italic* and **bold** text.

This is a paragraph with some `inline code`.

This is a paragraph with a [link](https://www.jetbrains.com/).

This is a code block:

```kotlin
fun main() {
    println("Hello, world!")
}
```

> This is a block quote.

This is a divider

---

The above was supposed to be a divider.

~~This is strikethrough with two tildes~~

~This is strikethrough~

This is an ordered list:

1. Item 1
2. Item 2
3. Item 3

This is an unordered list with dashes:

- Item 1
- Item 2
- Item 3

This is an unordered list with asterisks:

* Item 1
* Item 2
* Item 3

This is an ordered list with task list items:

1. [ ] foo
2. [x] bar

This is an unordered list with task list items:

- [ ] foo
- [x] bar

-------- 

### Inline Math

The quadratic formula is $x = \frac{-b \pm \sqrt{b^2 - 4ac}}{2a}$, where $a \neq 0$.

Euler's identity: $e^{i\pi} + 1 = 0$.

### Block Math

$$
\int_{-\infty}^{\infty} e^{-x^2} \, dx = \sqrt{\pi}
$$

$$
\sum_{n=1}^{\infty} \frac{1}{n^2} = \frac{\pi^2}{6}
$$

### Math Code Fence

```math
\begin{pmatrix} a & b \\ c & d \end{pmatrix} \cdot \begin{pmatrix} e \\ f \end{pmatrix} = \begin{pmatrix} ae + bf \\ ce + df \end{pmatrix}
```

--------

### Getting Started

For multiplatform projects specify this single dependency:

```
dependencies {
    implementation("com.mikepenz:multiplatform-markdown-renderer:{version}")
}
```

You can find more information
on [GitHub](https://github.com/mikepenz/multiplatform-markdown-renderer). More Text after this.

![Image](https://avatars.githubusercontent.com/u/1476232?v=4)

There are many more things which can be experimented with like, inline `code`.

Title 1
======

Title 2
------

[https://mikepenz.dev](https://mikepenz.dev)  
[https://github.com/mikepenz](https://github.com/mikepenz)  
[Mike Penz's Blog](https://blog.mikepenz.dev/)  
<https://blog.mikepenz.dev/>  