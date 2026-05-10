# Compose Performance Audit — 2026-05-10 — multiplatform-markdown-renderer

Static-only audit. No Macrobenchmark / device measurement. Source review across all library modules using the 11 applicable static skills from the `compose-performance-skills` library. Scope: KMP library code (Android, Desktop/JVM, Web/WASM, iOS), all 6 published modules. Sample apps excluded.

## Environment

- Kotlin: 2.3.21 (Strong Skipping default ON)
- Compose Compiler: bundled with Kotlin 2.3.21
- Modules audited:
  - `multiplatform-markdown-renderer` (core)
  - `multiplatform-markdown-renderer-m2`
  - `multiplatform-markdown-renderer-m3`
  - `multiplatform-markdown-renderer-coil2`
  - `multiplatform-markdown-renderer-coil3`
  - `multiplatform-markdown-renderer-code`
- Method: 11 parallel read-only sub-agents, one per skill, source-review only.

## Baseline (Phase 1)

Skipped at user request — static analysis only. No Macrobenchmark numbers recorded. Severity rankings below are qualitative (frequency × cost) inferences from source.

---

## Diagnosis (Phase 2)

### Top-5 recomposition hotspots (ranked by frequency × cost on long-document rendering)

1. **`MarkdownSuccess` top-level `Column` + `forEach`** — `Markdown.kt:362`. N top-level blocks → N sibling recompositions on any `State.Success` change. Highest leverage.
2. **`MarkdownText` reads 8 CompositionLocals per text node** — `MarkdownText.kt:118-124`. ~8 local reads × 100 text nodes = 800 implicit dependencies per parse.
3. **`MarkdownComponentModel` carries raw `ASTNode`** — `MarkdownComponents.kt:38-43`. JetBrains `ASTNode` is not `@Stable`; disables strong skipping for every component receiver.
4. **`MarkdownTable` `BoxWithConstraints`** — `MarkdownTable.kt:91`. Subcomposition during measure; cells recomposed on each constraint pass. Justified for scrollable decision but high cost on many-cell tables.
5. **`MarkdownBlockQuote` recursive child traversal** — `MarkdownBlockQuote.kt:68-88`. O(depth) sibling recompositions on nested quotes; missing `key()` for stable identity.

### Stability findings (`stability/diagnosing-compose-stability` + `understanding-stability-inference`)

| Type | File | Issue | Recommended fix |
|------|------|-------|-----------------|
| `MarkdownState` interface w/ `StateFlow<State>` | `model/MarkdownState.kt:38,159,162` | Flow-typed interface props block skipping when `MarkdownState` is a composable param | Keep flows internal; expose stable snapshot props (`val node: ASTNode?`, `val content: String?`) for composable APIs |
| `MarkdownInlineContent.inlineContent: Map` | `compose/MarkdownInlineContent.kt:10` | Mutable `Map` interface → unstable parameter | Switch to `kotlinx.collections.immutable.ImmutableMap`; mark `@Stable` |
| `DefaultMarkdownAnimation.animateTextSize: Modifier.() -> Modifier` | `compose/MarkdownAnimations.kt:19` | Lambda field defeats equality | Wrap lambda in `@Stable` value class or replace with enum strategy |
| `MarkdownExtendedSpans.extendedSpans: @Composable () -> ExtendedSpans?` | `compose/extendedspans/MarkdownExtendedSpans.kt:8,14` | Composable lambda field, only `@Immutable` | Mark interface `@Stable`; cache lambda result |
| `DefaultMarkdownAnnotator.annotate` lambda | `model/MarkdownAnnotator.kt:24` | Nullable lambda field blocks skipping | Wrap in stable value class |
| `ReferenceLinkHandlerImpl.stored: MutableMap` | `model/ReferenceLinkHandler.kt:19` | `var` mutable map → compiler verdict `runtime` | Add `@Stable`; or hoist storage outside the holder |
| `ExtendedSpans.drawInstructions` mutable `var` | `compose/extendedspans/ExtendedSpans.kt:22` | `@Stable` annotation but reassigned mutable field violates contract | Replace with snapshot-backed list or drop `@Stable` |
| `MarkdownAnnotatorConfig` interface | `model/MarkdownAnnotator.kt:6,48` | Missing `@Stable` on interface + impls | Add `@Stable` |

### Strong Skipping findings (`recomposition/using-strong-skipping-correctly`)

Strong Skipping confirmed ON (Kotlin 2.3.21).

- **`MarkdownText.kt:170-201`**: `containerModifier` and `textSegment` `@Composable` lambdas capture `containerSize` (`MutableState<Size>`), `extendedSpans`, `animations`, `baseColor`, `style`, `resolvedInlineContent`, `layoutResult`, `onTextLayout`. Repeated calls (lines 204, 212, 222) — risk of unstable captures preventing skip.
- **`MarkdownCheckBox.kt:18`**: default `checkedIndicator` lambda captures `node`, `style`.
- **`MarkdownList.kt:44-45,201-202,225-226`**: `markerModifier` / `listModifier` (`RowScope.() -> Modifier`) re-allocated per item, never memoized.
- **`MarkdownCode.kt`, `MarkdownTable.kt` defaults**: large default composable lambdas (`block`, `headerBlock`, `rowBlock`) recreated per call.
- Consider `@NonRestartableComposable` / `@ReadOnlyComposable` markers on entry points (`Markdown`, `LazyMarkdownSuccess`, `MarkdownText`).

### Phase deferral findings (`recomposition/deferring-state-reads`)

Mostly green. `drawBehind` in `MarkdownText.kt:188` and `MarkdownBlockQuote.kt:50-56` correctly defers to draw phase. `MarkdownHighlightedCode.kt:103-108` correctly uses `produceState` + `Dispatchers.Default` for syntax highlighting.

Minor:
- `MarkdownCodeTopBar.kt:55,76` — `textColor.copy(alpha = 0.6f)` allocates Color per recompose. Replace with `Modifier.graphicsLayer { alpha = 0.6f }`.
- `MarkdownCode.kt:138` — same pattern on `dividerColor.copy(alpha = 0.3f)`. Move alpha to `Modifier.graphicsLayer`.

### `derivedStateOf` findings (`recomposition/choosing-derivedstateof`)

Zero `derivedStateOf` uses across all modules. Opportunities:

- **`MarkdownText.kt:140-168`**: `remember(node, inlineContent.inlineContent, content, containerSize.value, transformer, ...)` derives block-image ranges from high-frequency `imageSizeByLink` size state. Wrap in `remember { derivedStateOf { … } }` so size churn does not invalidate the segment tree.
- `MarkdownState.kt:70-83` snapshotFlow + conflate is acceptable as-is (background parsing trigger).

### Subcomposition findings (`recomposition/avoiding-subcomposition-pitfalls`)

One use only: `MarkdownTable.kt:91-120` `BoxWithConstraints` to decide horizontal scroll vs fill-width. **Justified** — drives a composition branch, not just a modifier value, and sits at table root (not inside a per-row lazy item). No other `BoxWithConstraints` / `SubcomposeLayout` / nested `Scaffold`. Keep.

### Lazy layout findings (`lists/optimizing-lazy-layouts`, `lists/configuring-lazy-prefetch`)

Single `LazyColumn` in `LazyMarkdown.kt:35-46`.
- `key = { node -> node.startOffset }` — stable. ✓
- No `contentType` — low priority but trivial to add (`contentType = { "element" }`).
- All other markdown lists/tables correctly use eager `Column` + `forEach` to avoid subcomposition cascades when the library is composed inside a parent `LazyColumn`. **Correct library design** — do not change.

### Modifier.Node findings (`modifiers/migrating-to-modifier-node`)

Zero `Modifier.composed { }`. Single custom extension `Modifier.drawBehind(spans: ExtendedSpans)` at `extendedspans/ExtendedSpans.kt:105-113` — thin delegate to built-in `drawBehind`. No migration value. **Keep.**

### Modifier ordering findings (`modifiers/ordering-modifier-chains`)

**One real bug:**

- **`MarkdownCodeTopBar.kt:60-69`** — `.clip(CircleShape)` placed before `.clickable(…)`. Click hit area becomes the rectangular bounds, not the circular visible region. Move `.clip()` after `.clickable()`:
  ```kotlin
  Modifier
      .size(24.dp)
      .semantics { … }
      .clickable(onClickLabel = …) { … }
      .clip(CircleShape)
  ```

Other chains reviewed (`MarkdownCodeBackground.kt:119-122`, `MarkdownDivider.kt:31-35`, table cells) — acceptable.

### Flow collection findings (`side-effects/collecting-flows-safely`)

Library uses `collectAsState()` — correct for KMP `commonMain`. Do **not** introduce `collectAsStateWithLifecycle` in core. `MarkdownState.kt:72-83,132-148` uses `snapshotFlow + conflate` with comment justifying the pattern — well done. Coil2/Coil3 painter-state collection correctly scoped. No `Flow<T>` composable parameters.

### Effects findings (`side-effects/using-efficient-effects`)

All `LaunchedEffect`, `DisposableEffect`, `SideEffect`, `produceState` uses keyed correctly.
- `MarkdownState.kt:76-83,132-148` uses `rememberUpdatedState` + `LaunchedEffect(Unit)` + `snapshotFlow` + `conflate` — exemplary.
- `MarkdownImageAltTooltip.kt:51-58`, `MarkdownText.kt:347` (`SideEffect`), `LogCompositions.kt:20`, both Coil providers, `MarkdownHighlightedCode.kt:151-161` (`produceState` keyed on `code` + `awaitDispose`) — all correct.

No issues.

---

## Fixes recommended (Phase 3)

One-PR-per-fix preferred. Ranked by leverage on long-document rendering.

| # | Skill | Change | Files |
|---|-------|--------|-------|
| 1 | `stability/stabilizing-compose-types` | Stabilize `MarkdownComponentModel` — decompose at entry, pass stable primitives, or wrap `ASTNode` access in a `@Stable` holder. Unblocks strong skipping for every component. | `components/MarkdownComponents.kt` |
| 2 | `recomposition/using-strong-skipping-correctly` + `stability/stabilizing-compose-types` | Replace `MarkdownInlineContent.inlineContent: Map` with `ImmutableMap`; mark interface `@Stable`. | `compose/MarkdownInlineContent.kt`, callers |
| 3 | `stability/stabilizing-compose-types` | Add `@Stable` to `MarkdownAnnotatorConfig`, `MarkdownExtendedSpans`, `ReferenceLinkHandlerImpl`. Wrap `MarkdownAnimations.animateTextSize` and `DefaultMarkdownAnnotator.annotate` lambdas in stable value classes. | `model/MarkdownAnnotator.kt`, `compose/MarkdownAnimations.kt`, `compose/extendedspans/MarkdownExtendedSpans.kt`, `model/ReferenceLinkHandler.kt` |
| 4 | `stability/stabilizing-compose-types` | Fix `ExtendedSpans.drawInstructions` — replace mutable `var` with snapshot list or drop `@Stable`. | `compose/extendedspans/ExtendedSpans.kt:22` |
| 5 | `recomposition/choosing-derivedstateof` | Wrap block-image-range computation in `remember { derivedStateOf { … } }` so image-size churn does not retrigger segment recomputation. | `elements/MarkdownText.kt:140-168` |
| 6 | `modifiers/ordering-modifier-chains` | Move `.clip(CircleShape)` after `.clickable(…)` in code top-bar copy button. | `elements/MarkdownCodeTopBar.kt:60-69` |
| 7 | `recomposition/using-strong-skipping-correctly` | Hoist / memoize default composable lambdas in `MarkdownCode`, `MarkdownTable`, `MarkdownList` markers; or document them as user-supplied (and assume strong skipping will memoize at call site). | `elements/MarkdownCode.kt`, `MarkdownTable.kt`, `MarkdownList.kt` |
| 8 | `recomposition/deferring-state-reads` | Replace `Color.copy(alpha = …)` in composition with `Modifier.graphicsLayer { alpha = … }`. | `elements/MarkdownCodeTopBar.kt:55,76`, `elements/MarkdownCode.kt:138` |
| 9 | `lists/optimizing-lazy-layouts` | Add `contentType` to `LazyMarkdownSuccess` items. | `compose/LazyMarkdown.kt:35-46` |
| 10 | (recomposition cascade) | Add stable `key(node.startOffset)` around `MarkdownBlockQuote` child loop. | `elements/MarkdownBlockQuote.kt:68-88` |

### Fixes deliberately not recommended

- **CompositionLocal hoisting in `MarkdownText`**: 8 locals are read; this is intrinsic to a themed library and the reads are cheap. Restructuring would break public API for marginal gain. Skip unless measurement proves cost.
- **`Modifier.composed` migration**: only one trivial extension exists; not worth the boilerplate.
- **`collectAsStateWithLifecycle` in core**: violates KMP `commonMain` constraint. Leave to consumers.
- **Switching internal lists/tables to lazy layouts**: would cause subcomposition cascades inside consumer `LazyColumn`s. Current eager design is correct.

---

## Verification (Phase 4)

Skipped — measurement out of scope per audit constraints. Recommended follow-up if measurement is later added:

- Add Macrobenchmark module to `sample/android` with cold-startup + long-doc scroll fixtures.
- Generate Baseline Profile; commit `baseline-prof.txt`.
- Wire `:stabilityCheck` (skydoves `compose-stability-analyzer` Gradle plugin) into CI; commit `.stability` baselines after fixes #1–#4 land. See `stability/enforcing-stability-in-ci`.
- Re-measure scroll P50/P90/P99 after fixes #1–#3 to confirm strong-skipping unlock.

---

## Open items / follow-ups

- Decide whether to make `LazyMarkdownSuccess` the documented default for documents above N top-level nodes (addresses hotspot #1 without breaking API).
- Audit downstream samples after fixes #2 + #3 — `ImmutableMap` shift may ripple into `sample/shared`.
- Re-run static sweep after Compose Compiler bump or Kotlin 2.4.x to capture any new stability inference behaviour.
