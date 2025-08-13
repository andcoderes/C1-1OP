package cl.jacevedo.droidcontroller.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import cl.jacevedo.droidcontroller.R

@Composable
fun DroidTextView(text:String, overflow: TextOverflow = TextOverflow.Visible, modifier: Modifier = Modifier, color : Color = Color.Black, maxlines: Int = 99){
    if(!LocalInspectionMode.current) {
        val anakimonoFont =    FontFamily(
            Font(R.font.anakinmono)
        )
        Text(text = text, maxLines = maxlines, color = color, overflow = overflow, modifier = modifier, fontFamily  = anakimonoFont, fontWeight = FontWeight.Light)

    } else {
        Text(text = text, maxLines = maxlines, color = color,  overflow = overflow, modifier = modifier)
    }

}