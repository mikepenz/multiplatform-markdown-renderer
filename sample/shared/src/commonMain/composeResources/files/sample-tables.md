# Tables Showcase

GFM tables support the full set of inline content. Each cell below
demonstrates a different inline construct.

## Inline content

| Feature             | Example                                |
| ------------------- | -------------------------------------- |
| plain text          | hello world                            |
| **bold**            | **bold** text                          |
| *italic* (asterisk) | *italic* text                          |
| _italic_ (under)    | _italic_ text                          |
| ~~strikethrough~~   | ~~strike~~ here                        |
| inline `code`       | `val x = 1`                            |
| inline link         | [JetBrains](https://www.jetbrains.com) |
| autolink            | <https://mikepenz.dev>                 |
| GFM autolink        | https://github.com                     |
| escaped pipe        | a \| b                                 |
| mixed               | **bold** and `code` and [link](https://x) |

## Inline placeholders (issue #511)

Demonstrates a custom `MarkdownAnnotator` + `MarkdownInlineContent` that
substitutes the `⦃check⦄` token with a ✅ glyph — both outside and
inside table cells.

Outside table: ⦃check⦄ Complete

| Feature       | Status            |
| ------------- | ----------------- |
| **Headers**   | ⦃check⦄ Complete |
| **Links**     | ⦃check⦄ Complete |
| **Bold/Italic** | ⦃check⦄ Complete |
| **Tables**    | ⦃check⦄ Complete |

## Inline images

Images inside cells render inline (per GFM spec — no block content in cells):

| Avatar | Description |
| --- | --- |
| ![mikepenz](https://avatars.githubusercontent.com/u/1476232?v=4&s=128) | profile image, sized via URL |
| Mixed: ![logo](https://avatars.githubusercontent.com/u/1476232?v=4&s=128) text | image with surrounding text |

## Reference links

| Source | Link |
| ------ | ---- |
| Repo   | [GitHub repo][repo] |
| Author | [mikepenz][author] |
| Short  | [docs] |

[repo]: https://github.com/mikepenz/multiplatform-markdown-renderer
[author]: https://github.com/mikepenz
[docs]: https://github.com/mikepenz/multiplatform-markdown-renderer#readme

## Alignment

Column alignment markers are parsed but not yet rendered visually:

| Left-aligned | Center-aligned | Right-aligned |
| :---         |     :---:      |          ---: |
| git status   | git status     | git status    |
| git diff     | git diff       | git diff      |

## Edge cases

| Name     | Character |
| ---      | ---       |
| Backtick | `         |
| Pipe     | \|        |

| abc | def |
| --- | --- |
| bar |
| bar | baz | boo |

| Header 1     | Header 2 |
| ------------ | -------- |
| Longer text  | Ab       |
| Lorem        | CD 45    |
| Ipsum dd     | EF 67    |
