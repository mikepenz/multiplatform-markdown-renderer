package com.mikepenz.markdown.latex.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import com.mikepenz.markdown.latex.generated.resources.KaTeX_AMS_Regular
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Caligraphic_Bold
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Caligraphic_Regular
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Fraktur_Bold
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Fraktur_Regular
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Main_Bold
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Main_BoldItalic
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Main_Italic
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Main_Regular
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Math_BoldItalic
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Math_Italic
import com.mikepenz.markdown.latex.generated.resources.KaTeX_SansSerif_Bold
import com.mikepenz.markdown.latex.generated.resources.KaTeX_SansSerif_Italic
import com.mikepenz.markdown.latex.generated.resources.KaTeX_SansSerif_Regular
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Script_Regular
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Size1_Regular
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Size2_Regular
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Size3_Regular
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Size4_Regular
import com.mikepenz.markdown.latex.generated.resources.KaTeX_Typewriter_Regular
import com.mikepenz.markdown.latex.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun rememberKaTeXFontMap(): Map<String, FontFamily> {
    val amsRegular = FontFamily(Font(Res.font.KaTeX_AMS_Regular))
    val caligraphicBold = FontFamily(Font(Res.font.KaTeX_Caligraphic_Bold))
    val caligraphicRegular = FontFamily(Font(Res.font.KaTeX_Caligraphic_Regular))
    val frakturBold = FontFamily(Font(Res.font.KaTeX_Fraktur_Bold))
    val frakturRegular = FontFamily(Font(Res.font.KaTeX_Fraktur_Regular))
    val mainBold = FontFamily(Font(Res.font.KaTeX_Main_Bold))
    val mainBoldItalic = FontFamily(Font(Res.font.KaTeX_Main_BoldItalic))
    val mainItalic = FontFamily(Font(Res.font.KaTeX_Main_Italic))
    val mainRegular = FontFamily(Font(Res.font.KaTeX_Main_Regular))
    val mathBoldItalic = FontFamily(Font(Res.font.KaTeX_Math_BoldItalic))
    val mathItalic = FontFamily(Font(Res.font.KaTeX_Math_Italic))
    val sansSerifBold = FontFamily(Font(Res.font.KaTeX_SansSerif_Bold))
    val sansSerifItalic = FontFamily(Font(Res.font.KaTeX_SansSerif_Italic))
    val sansSerifRegular = FontFamily(Font(Res.font.KaTeX_SansSerif_Regular))
    val scriptRegular = FontFamily(Font(Res.font.KaTeX_Script_Regular))
    val size1Regular = FontFamily(Font(Res.font.KaTeX_Size1_Regular))
    val size2Regular = FontFamily(Font(Res.font.KaTeX_Size2_Regular))
    val size3Regular = FontFamily(Font(Res.font.KaTeX_Size3_Regular))
    val size4Regular = FontFamily(Font(Res.font.KaTeX_Size4_Regular))
    val typewriterRegular = FontFamily(Font(Res.font.KaTeX_Typewriter_Regular))

    return remember {
        mapOf(
            "AMS-Regular" to amsRegular,
            "Caligraphic-Bold" to caligraphicBold,
            "Caligraphic-Regular" to caligraphicRegular,
            "Fraktur-Bold" to frakturBold,
            "Fraktur-Regular" to frakturRegular,
            "Main-Bold" to mainBold,
            "Main-BoldItalic" to mainBoldItalic,
            "Main-Italic" to mainItalic,
            "Main-Regular" to mainRegular,
            "Math-BoldItalic" to mathBoldItalic,
            "Math-Italic" to mathItalic,
            "SansSerif-Bold" to sansSerifBold,
            "SansSerif-Italic" to sansSerifItalic,
            "SansSerif-Regular" to sansSerifRegular,
            "Script-Regular" to scriptRegular,
            "Size1-Regular" to size1Regular,
            "Size2-Regular" to size2Regular,
            "Size3-Regular" to size3Regular,
            "Size4-Regular" to size4Regular,
            "Typewriter-Regular" to typewriterRegular,
        )
    }
}
