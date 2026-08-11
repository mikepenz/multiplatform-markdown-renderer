#!/usr/bin/env python3
"""Generates art/hero-light.svg and art/hero-dark.svg from one source of truth."""
import pathlib

W, H = 1200, 420

SANS = "-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif"
MONO = "ui-monospace,SFMono-Regular,Menlo,monospace"

LIME, GREEN, BLUE = "#c7ff1e", "#00ff6a", "#00b9ff"

THEMES = {
    # `dot` is the only accent that carries meaning at 5px, so it needs real contrast on white.
    "dark": dict(bg="#1a1a1a", fg="#ffffff", muted="#9a9a9a", card="#242424", stroke="#3a3a3a",
                 dot=GREEN),
    "light": dict(bg="#ffffff", fg="#1a1a1a", muted="#5f5f5f", card="#f4f4f4", stroke="#e0e0e0",
                  dot="#00a44a"),
}

# label, sub-label, accent
STAGES = [
    ("Markdown", "String", LIME),
    ("AST", "JetBrains Markdown", GREEN),
    ("Components", "MarkdownComponents", BLUE),
    ("Compose UI", "@Composable", BLUE),
]

PLATFORMS = ["Android", "iOS", "Desktop (JVM)", "Web (Wasm / JS)", "macOS"]

CARD_W, CARD_H, GAP = 248, 108, 26
PIPE_Y = 196
PIPE_X = 64


def esc(s):
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")


def build(theme_name):
    t = THEMES[theme_name]
    o = []
    a = o.append
    a(f'<svg xmlns="http://www.w3.org/2000/svg" width="{W}" height="{H}" '
      f'viewBox="0 0 {W} {H}" role="img" aria-labelledby="heroTitle heroDesc">')
    a('<title id="heroTitle">multiplatform-markdown-renderer</title>')
    a('<desc id="heroDesc">A markdown String is parsed by JetBrains Markdown into an AST, '
      'mapped through overridable MarkdownComponents, and rendered as Compose Multiplatform UI '
      'on Android, iOS, Desktop, Web and macOS.</desc>')
    a(f'<rect width="{W}" height="{H}" rx="24" fill="{t["bg"]}"/>')

    # --- title block ---
    a(f'<g id="title-block" transform="translate({PIPE_X} 0)">')
    a(f'<text x="0" y="72" font-family="{MONO}" font-size="19" letter-spacing="2.6" '
      f'fill="{t["muted"]}">KOTLIN MULTIPLATFORM · COMPOSE MULTIPLATFORM</text>')
    a(f'<text x="0" y="126" font-family="{SANS}" font-size="52" font-weight="700" '
      f'fill="{t["fg"]}">multiplatform-markdown-renderer</text>')
    a(f'<text x="0" y="164" font-family="{SANS}" font-size="24" fill="{t["muted"]}">'
      f'Render Markdown as native Compose UI — one API, every platform.</text>')
    a('</g>')

    # --- pipeline ---
    a(f'<g id="pipeline" transform="translate({PIPE_X} {PIPE_Y})">')
    for i, (label, sub, accent) in enumerate(STAGES):
        x = i * (CARD_W + GAP)
        a(f'<g transform="translate({x} 0)">')
        a(f'<rect width="{CARD_W}" height="{CARD_H}" rx="14" fill="{t["card"]}" '
          f'stroke="{t["stroke"]}"/>')
        a(f'<rect width="{CARD_W}" height="4" rx="2" fill="{accent}"/>')
        a(f'<text x="20" y="46" font-family="{SANS}" font-size="24" font-weight="600" '
          f'fill="{t["fg"]}">{esc(label)}</text>')
        a(f'<text x="20" y="78" font-family="{MONO}" font-size="18" '
          f'fill="{t["muted"]}">{esc(sub)}</text>')
        a('</g>')
        if i < len(STAGES) - 1:
            cx = x + CARD_W
            a(f'<path d="M{cx + 4} {CARD_H / 2} H{cx + GAP - 8}" stroke="{t["stroke"]}" '
              f'stroke-width="2"/>')
            a(f'<path d="M{cx + GAP - 13} {CARD_H / 2 - 5} l6 5 -6 5" fill="none" '
              f'stroke="{t["muted"]}" stroke-width="2" stroke-linecap="round" '
              f'stroke-linejoin="round"/>')
    a('</g>')

    # --- platform row ---
    a(f'<g id="platforms" transform="translate({PIPE_X} 366)">')
    x = 0
    for name in PLATFORMS:
        a(f'<circle cx="{x + 6}" cy="-6" r="5" fill="{t["dot"]}"/>')
        a(f'<text x="{x + 22}" y="0" font-family="{SANS}" font-size="20" '
          f'fill="{t["muted"]}">{esc(name)}</text>')
        x += 26 + int(len(name) * 10.6) + 30
    a('</g>')

    a('</svg>')
    return "\n".join(o) + "\n"


out = pathlib.Path(__file__).resolve().parent
for name in THEMES:
    p = out / f"hero-{name}.svg"
    p.write_text(build(name))
    print(p)
