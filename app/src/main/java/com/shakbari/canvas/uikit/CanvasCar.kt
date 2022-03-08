package com.shakbari.canvas.uikit

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import android.view.MotionEvent
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle


@ExperimentalAnimationApi
@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun CanvasCar() {

    var counterSpeed: Float by remember {
        mutableStateOf(240f)
    }
    var isSpeed by remember {
        mutableStateOf(false)
    }

    var isBrake by remember {
        mutableStateOf(false)
    }

    var isFastSpeed by remember {
        mutableStateOf(true)
    }

    val counterAnimation by animateFloatAsState(
        targetValue = if (isSpeed) {
            if (counterSpeed < 480) {
                counterSpeed += when {
                    counterSpeed >= 320 && counterSpeed < 380 -> {
                        0.2f
                    }
                    counterSpeed >= 380 && counterSpeed < 420f -> {
                        0.1f
                    }
                    counterSpeed >= 420f -> {
                        0.05f
                    }
                    else -> {
                        0.5f
                    }
                }
                counterSpeed
            } else counterSpeed
        } else {
            if (!isBrake) {
                if (counterSpeed > 240) {
                    counterSpeed -= 0.1f
                    counterSpeed
                } else 240f
            } else {
                if (counterSpeed > 240) {
                    counterSpeed -= 1.0f
                    counterSpeed
                } else 240f
            }
        },
    )


    val fadeAnimation by animateFloatAsState(
        targetValue = if (isFastSpeed) 0.0f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        )
    )


    Surface {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            isFastSpeed = counterSpeed < 420
            if (!isFastSpeed) {
                Text(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 100.dp)
                        .alpha(fadeAnimation),
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Red,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp
                            )
                        ) {
                            append("بووق، بووق")
                        }
                    })
            }

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 30.sp
                        )
                    ) {
                        append(" " + (counterAnimation.toInt() - 240))
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontSize = 15.sp
                        )
                    ) {
                        append(" KM")
                    }
                },
                fontSize = 10.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 60.dp)
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Canvas(modifier = Modifier.size(300.dp)) {
                    drawSemiCircle()
                    drawCounter(counterAnimation)
                    drawKilometers()
                }
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier.pointerInteropFilter {
                            if (it.action == MotionEvent.ACTION_DOWN) {
                                isBrake = true
                            } else if (it.action == MotionEvent.ACTION_UP) {
                                isBrake = false
                            }
                            true
                        },
                    ) {
                        Text(text = "ترمز کن")
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.pointerInteropFilter {
                            if (it.action == MotionEvent.ACTION_DOWN) {

                                isSpeed = true
                            } else if (it.action == MotionEvent.ACTION_UP) {
                                isSpeed = false
                            }
                            true
                        },
                    ) {
                        Text(text = "گاز بده")
                    }

                }

            }
        }
    }
}

@Composable
fun setBooghUi(isFastSpeed: Boolean,fadeAnimation:Float) {

}

fun DrawScope.drawSemiCircle() {
    val componentSize = size / 0.99f
    carView(
        componentSize = componentSize,
        startAngle = 150f,
        sweepAngle = 240f,
        color = Color.Gray,
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
    carView(
        componentSize = componentSize,
        startAngle = 330f,
        sweepAngle = 60f,
        color = Color.Red,
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}


fun DrawScope.carView(
    componentSize: Size,
    startAngle: Float,
    sweepAngle: Float,
    color: Color,
    topLeft: Offset
) {

    drawArc(

        size = componentSize,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        color = color,
        useCenter = false,
        style = Stroke(
            width = componentSize.width * 0.02f,
            cap = StrokeCap.Round
        ),
        topLeft = topLeft
    )
}

fun DrawScope.drawCounter(animCounter: Float) {
    drawCircle(
        color = Color.Black,
        style = Stroke(width = size.width * 0.1f),
        radius = 3.dp.value
    )
    drawCircle(
        color = Color.Red,
        radius = 10.dp.value
    )
    rotate(animCounter) {
        drawLine(
            color = Color.Red,
            start = this.center,
            end = Offset(
                x = size.width / 2,
                y = 1.dp.value
            ),
            strokeWidth = 6.dp.value
        )
    }
}

fun DrawScope.drawKilometers() {
    for (angle in 240..480 step 3) {
        rotate(angle.toFloat()) {
            drawLine(
                color = if (angle < 420) Color.Black else Color.Red,
                start = Offset(size.width / 2, 10f),
                end = Offset(
                    size.width / 2,
                    if (angle % 10 == 0)
                        60.dp.value
                    else
                        30.dp.value
                ),
                strokeWidth = if (angle % 5 == 0) 5.dp.value else 2.dp.value,
            )
        }
    }
}




