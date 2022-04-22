package co.rikin.fidget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.rikin.fidget.ui.theme.DreamyGradient
import co.rikin.fidget.ui.theme.FidgetTheme
import co.rikin.fidget.ui.theme.Obsidian

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      FidgetTheme {
        Background {
          BoxWithConstraints(
            modifier = Modifier
              .width(310.dp)
              .height(210.dp)
              .background(brush = DreamyGradient, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
          ) {
            with(LocalDensity.current) {
              FidgetCard(width = maxWidth.toPx(), height = maxHeight.toPx())
            }
          }
        }
      }
    }
  }
}

@Composable
fun FidgetCard(
  width: Float,
  height: Float
) {
  var transformAngleX by remember { mutableStateOf(0f) }
  var transformAngleY by remember { mutableStateOf(0f) }

  fun translateOffsetToCenterOrigin(
    width: Float,
    height: Float,
    offset: Offset
  ): Offset {
    val xOffset = width / 2
    val yOffset = height / 2
    return Offset(offset.x - xOffset, offset.y - yOffset)
  }

  fun calculateTransformationOutput(
    maxOffset: Float,
    maxTransformation: Float,
    input: Float
  ): Float {
    return (maxTransformation / maxOffset) * input
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .padding(2.dp)
      .graphicsLayer {
        rotationX = transformAngleX
        rotationY = transformAngleY
      }
      .clip(RoundedCornerShape(16.dp))
      .background(color = Color.Black)
      .pointerInput(Unit) {
        detectTransformGestures { centroid, _, _, _ ->
          val coercedOffset = Offset(
            x = centroid.x.coerceIn(0F, width),
            y = centroid.y.coerceIn(0F, height)
          )
          val tapOffset = translateOffsetToCenterOrigin(width, height, coercedOffset)
          transformAngleY = calculateTransformationOutput(width, 4F, tapOffset.x)
          transformAngleX = calculateTransformationOutput(height, 10F, -tapOffset.y)
        }
      },
    contentAlignment = Alignment.Center
  ) {
    FidgetCardContent()
  }
}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
  FidgetTheme {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black),
      contentAlignment = Alignment.Center
    ) {
      BoxWithConstraints(
        modifier = Modifier
          .width(310.dp)
          .height(210.dp)
          .clip(
            RoundedCornerShape(18.dp)
          )
          .background(brush = DreamyGradient),
        contentAlignment = Alignment.Center
      ) {
        with(LocalDensity.current) {
          FidgetCard(maxWidth.toPx(), maxHeight.toPx())
        }
      }
    }
  }
}

@Composable
fun FidgetCardContent() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.Bottom
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Box(
        modifier = Modifier
          .size(50.dp)
          .clip(CircleShape)
          .background(color = Obsidian)
      )
      Column(
        modifier = Modifier
          .weight(1f)
          .height(50.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth(.6f)
            .height(20.dp)
            .clip(CircleShape)
            .background(color = Obsidian)
        )

        Box(
          modifier = Modifier
            .fillMaxWidth(.4f)
            .height(20.dp)
            .clip(CircleShape)
            .background(color = Obsidian)
        )
      }
    }
  }
}

@Composable
fun Background(content: @Composable BoxScope.() -> Unit) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(color = Color.Black),
    contentAlignment = Alignment.Center,
    content = content
  )
}
