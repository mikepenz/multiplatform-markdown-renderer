package com.mikepenz.markdown.accessibility.test

/**
 * Manual test to verify accessibility semantics are properly applied to markdown elements.
 * 
 * To test accessibility:
 * 1. Enable TalkBack on Android or a screen reader on your platform
 * 2. Navigate through the markdown content
 * 3. Verify that elements are properly announced with their semantic roles
 * 
 * Expected announcements:
 * - Headers should be announced as "Heading"
 * - Lists should be announced as "List" with items as "List item"
 * - Tables should be announced with "Table", "Row", and "Cell" semantics
 * - Block quotes should be grouped appropriately
 * - Checkboxes should announce their checked/unchecked state
 * - Paragraphs should be properly structured
 */

val accessibilityTestMarkdown = """
# Main Heading (H1)

This is a paragraph that should be announced as a paragraph by screen readers.

## Subheading (H2)

### Lists with Accessibility

- First list item
- Second list item
  - Nested list item
  - Another nested item
- Third list item

1. Ordered list item one
2. Ordered list item two
3. Ordered list item three

### Task Lists

- [x] Completed task (should announce as checked)
- [ ] Incomplete task (should announce as unchecked)
- [x] Another completed task

### Block Quote

> This is a block quote that should be grouped properly for screen readers.
> It contains multiple lines of quoted text.

### Table

| Header 1 | Header 2 | Header 3 |
|----------|----------|----------|
| Cell 1   | Cell 2   | Cell 3   |
| Data A   | Data B   | Data C   |

### Code Block

```kotlin
// This code block should be traversed as a group
fun example() {
    println("Accessibility test")
}
```

This demonstrates all the accessibility features implemented in the library.
""".trimIndent()

/**
 * Example usage in a Composable:
 * 
 * @Composable
 * fun AccessibilityTestScreen() {
 *     Markdown(
 *         content = accessibilityTestMarkdown,
 *         modifier = Modifier.fillMaxSize().padding(16.dp)
 *     )
 * }
 */