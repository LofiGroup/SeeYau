package com.lofigroup.seeyau.features.chat.ui.components

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.lofigroup.seeyau.features.chat.R
import com.sillyapps.core.ui.components.ImageButton
import com.sillyapps.core.ui.theme.LocalSpacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun BoxScope.SayHelloButton(
  onSayHelloButtonClick: () -> Unit
) {
  val scope = rememberCoroutineScope()
  var atEnd by remember {
    mutableStateOf(false)
  }
  val vectorDrawable = AnimatedImageVector.animatedVectorResource(id = R.drawable.anim_shaking_hand)
  val animation = rememberAnimatedVectorPainter(animatedImageVector = vectorDrawable, atEnd = atEnd)

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.align(Alignment.Center)
  ) {
    Text(
      text = stringResource(id = R.string.say_hello),
      style = MaterialTheme.typography.h5
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.large))

    ImageButton(
      painter = animation,
      onClick = {
        atEnd = true
        scope.launch {
          delay(vectorDrawable.totalDuration.toLong() + 400L)
          onSayHelloButtonClick()
        }
      }
    )
  }
}