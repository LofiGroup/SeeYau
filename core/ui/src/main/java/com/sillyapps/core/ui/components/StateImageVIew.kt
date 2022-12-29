package com.sillyapps.core.ui.components

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.widget.ImageView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun StateImageView(
  state: StateTransitions,
  currentState: Int,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val imageView = remember {
    ImageView(context).apply {
      setImageResource(currentState)
    }
  }
  var lastState by remember {
    mutableStateOf(currentState)
  }

  LaunchedEffect(currentState) {
    val transition = state.getTransition(from = lastState, to = currentState)
    if (transition == null) {
      imageView.setImageResource(currentState)
    } else {
      imageView.setImageResource(transition)
      val animation = (imageView.drawable as AnimatedVectorDrawable)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        animation.reset()
      }
      animation.start()
    }
    lastState = currentState
  }

  AndroidView(
    factory = {
      imageView
    },
    modifier = modifier
  )
}

@Composable
fun rememberStateImageView(transitions: Map<String, Int>): StateTransitions {
  return remember {
    StateTransitions(transitions = transitions)
  }
}

class StateTransitions(
  private val transitions: Map<String, Int>,
) {

  fun getTransition(from: Int, to: Int): Int? {
    return transitions["${from}_${to}"]
  }

  class TransitionBuilder {
    private val map = HashMap<String, Int>()

    fun add(from: Int, to: Int, anim: Int): TransitionBuilder {
      map["${from}_${to}"] = anim
      return this
    }

    fun build(): Map<String, Int> {
      return map
    }
  }

}