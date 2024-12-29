package com.example.musicknob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            MusicKnob(
//                modifier = Modifier.fillMaxSize(),
//                onValueChange = { rotation ->
//                    // Handle rotation value change (e.g. update some UI element)
//                }
//            )
//        }
//    }
//}

@Composable
fun demo(
    modifier: Modifier = Modifier,
    limitingAngle: Float = 25f,
    onValueChange: (Float) -> Unit
) {
    var rotation by remember { mutableStateOf(0f) }
    var centerX by remember { mutableStateOf(0f) }
    var centerY by remember { mutableStateOf(0f) }

    Image(
        painter = painterResource(id = R.drawable.musicknob),
        contentDescription = "Music Knob",
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                val size: IntSize = coordinates.size
                centerX = size.width / 2f
                centerY = size.height / 2f
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    val angle = calculateRotationAngle(centerX, centerY, pan)
                    rotation = angle.coerceIn(-limitingAngle, limitingAngle)
                    onValueChange(rotation)
                }
            }
            .graphicsLayer(rotationZ = rotation)
    )
}

fun calculateRotationAngle(centerX: Float, centerY: Float, pan: androidx.compose.ui.geometry.Offset): Float {
    val deltaX = pan.x - centerX
    val deltaY = pan.y - centerY
    return Math.toDegrees(Math.atan2(deltaY.toDouble(), deltaX.toDouble())).toFloat()
}
