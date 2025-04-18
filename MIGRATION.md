### Upgrade Notes

#### Version 0.33.0

- **Dependency Upgrade**: Kotlin 2.1.20
- **Breaking Change**: The provided `Markdown` (String) is now parsed asynchronously.
    - This results in a `loading` state being displayed prior to the parsing result.
        - While the `rememberMarkdownState` offers the ability to require `immediate` parsing as
          before, this is not advised as it might block the composition of the UI.
    - A new `loading` and `error` composable can be provided to handle the different states
    - A new `success` composable was also introduced to modify success states (See more on the lazy
      handling)
    - The `linkDefinition` component was deprecated in favor of the new link lookup ahead of time
    - Accompanying this change, a new `rememberMarkdownState` was introduced to retrieve a hoisted
      observable state from the markdown.
        - This offers greater flexibility and allows custom state handling.

```kotlin
val markdownState = rememberMarkdownState(MARKDOWN)
Markdown(state = markdownState)

// exposes the general state:
markdownState.state

// exposes found link definitions and links
markdownState.links
```

- **Breaking Change**: The `MarkdownComponent` composable function no longer passes on a
  `ColumnScope`
    - This change was made to allow for more flexibility in the layout of the markdown components.
    - The `ColumnScope` wasn't used by the library before, and prevented use cases like
      `LazyColumn`.
    - If you have implemented your own `MarkdownComponent` depending on this, you will need to
      remove the `ColumnScope` parameter from your implementation, and wrap the component in a
      `Column {}` directly.
    - Thanks to this change a new `LazyMarkdownSuccess` composable was introduced to allow
      rendering very long markdown content as `LazyColumn` instead of a `Column`

```kotlin
Markdown(
    state = markdownState,
    success = { state, components, modifier ->
        LazyMarkdownSuccess(state, components, modifier, contentPadding = PaddingValues(16.dp))
    },
    modifier = Modifier.fillMaxSize()
)
```

- **Breaking Change**: The `level` argument for `MarkdownOrderedList` and `MarkdownBulletList` were
  renamed to `depth`.
- **Breaking Change**: The `MarkdownListItems` component was refactored rendering nested
  Ordered/Unordered lists using the `MarkdownComponents` as defined.
    - As a result the component spec now needs to handle depth.
      See [Source](https://github.com/keta1/multiplatform-markdown-renderer/blob/develop/multiplatform-markdown-renderer/src/commonMain/kotlin/com/mikepenz/markdown/compose/components/MarkdownComponents.kt#L201-L208)
      for details.
- **Breaking Change**: The `MarkdownListItems` component now takes 2 optional lambdas
    - These lambdas offer the ability to provide custom `Modifier`s for the marker and the list
      content.
    - This can be helpful for example to adjust the baseline.
      See: https://github.com/mikepenz/multiplatform-markdown-renderer/issues/329
- **Breaking Change**: The `BulletHandler` adds a new argument in the function `listNumber: Int`.
    - This was introduced to allow for more flexibility in the rendering of the bullet points.
    - But also specifically to enable list rendering according to spec:
        - https://spec.commonmark.org/0.31.2/#start-number
    - The `index` argument was retained to allow custom implementations adjust this behavior.

- **Behavior Change**: To account for the behavior change introduced in v0.32.0 on EOL handling a
  new API was introduced to bring back prior behavior

```kotlin
annotator = markdownAnnotator(
    config = markdownAnnotatorConfig(
        eolAsNewLine = true
    )
)
```

#### Version 0.32.0

- **Breaking Change**: The `MarkdownTypography` interface introduces the `table` property.
    - This property is used to define the typography for tables in markdown.
    - If you have implemented your own `MarkdownTypography`, you will need to add the `table`
      property to your implementation.
- **Breaking Change**: The `BulletHandler` function has been updated to include another argument
  `depth`
    - This property can be used to use a different `bullet style` for different levels of depth.
- **Behavior Change**: `EOL`is now considered a `' '` whitespace character instead of a line break.
    - This change was made to align more properly with the markdown standard
    - More details: https://github.com/mikepenz/multiplatform-markdown-renderer/issues/337
- **Behavior Change**: The primary definition for text color of components is now in the
  `MarkdownTypography` text styles.
    - This change was made to have a more consistent definition of text colors across all
      components.

#### Prior release

Unfortunately no upgrade notes were provided for the releases prior to 0.32.0.
See the release notes available on GitHub for more
details: https://github.com/mikepenz/multiplatform-markdown-renderer/releases