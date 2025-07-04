# Accessibility Support

The multiplatform-markdown-renderer library provides comprehensive accessibility support for Android and other platforms, making markdown content accessible to users with disabilities who rely on screen readers and other assistive technologies.

## Accessibility Features

### Semantic Annotations

The library uses Jetpack Compose's semantic properties to provide meaningful information to accessibility services:

#### Headings
- All markdown headers (H1-H6) are marked with `heading()` semantics
- Screen readers will properly announce these as headings

#### Lists
- Ordered and unordered lists use `Role.List` semantics
- Individual list items use `Role.ListItem` semantics
- Nested lists maintain proper hierarchical structure

#### Block Quotes
- Block quotes are marked with `Role.Group` semantics
- Screen readers will group quote content appropriately

#### Tables
- Tables use `Role.Table` semantics for the container
- Table rows use `Role.Row` semantics
- Table cells use `Role.Cell` semantics
- Headers are properly distinguished with bold text styling

#### Paragraphs
- Paragraphs use `Role.Paragraph` semantics
- Helps screen readers understand content structure

#### Checkboxes
- Task list checkboxes use `Role.Checkbox` semantics
- Checkbox state is indicated with `ToggleableState`
- Screen readers will announce the checked/unchecked state

#### Code Blocks
- Code blocks use `isTraversalGroup = true` semantics
- Keeps code content grouped together for accessibility navigation

#### Images
- Images support proper `contentDescription` through the image transformer
- Alt text from markdown is passed through to screen readers

## European Accessibility Act Compliance

These accessibility improvements help ensure compliance with the European Accessibility Act that comes into effect on June 28, 2025. The semantic annotations provide the foundation for screen reader compatibility and assistive technology support.

## Usage

All accessibility features are enabled by default when using the library. No additional configuration is required.

```kotlin
// Accessibility is automatically applied to all markdown elements
Markdown(
    content = """
    # Accessible Heading
    
    - Accessible list item
    - [ ] Accessible checkbox
    
    > Accessible block quote
    
    | Column 1 | Column 2 |
    |----------|----------|
    | Cell 1   | Cell 2   |
    """
)
```

## Testing Accessibility

To test accessibility on Android:

1. Enable TalkBack or other screen readers
2. Navigate through your markdown content
3. Verify that elements are properly announced
4. Test with different types of markdown content

## Contributing

If you find accessibility issues or have suggestions for improvements, please:

1. Test with actual screen readers and assistive technologies
2. File an issue with specific accessibility concerns
3. Consider contributing fixes or enhancements

Accessibility is an ongoing effort, and we welcome contributions to make the library even more inclusive.