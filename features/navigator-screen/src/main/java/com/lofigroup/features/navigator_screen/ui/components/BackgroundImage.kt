package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.commit451.coiltransformations.BlurTransformation
import com.lofigroup.features.navigator_screen.model.UserItemUIModel
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalExtendedColors

@Composable
fun BackgroundImage(
  selectedUser: UserItemUIModel?,
  isInFullScreenMode: Boolean
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(LocalExtendedColors.current.backgroundGradient)
  ) {
    if (selectedUser != null && isInFullScreenMode) {
      BackgroundCrop(url = selectedUser.imageUrl)
    }
  }
}

@Composable
fun BackgroundFitBlack(
  url: String?
) {
  Box(
    Modifier.fillMaxSize().background(MaterialTheme.colors.background)
  ) {
    RemoteImage(
      model = url,
      shape = RectangleShape,
      modifier = Modifier
        .fillMaxSize(),
      contentScale = ContentScale.Fit
    )
  }
}

@Composable
fun BackgroundFitTransparent(
  url: String?
) {
  RemoteImage(
    model = url,
    shape = RectangleShape,
    modifier = Modifier
      .fillMaxSize(),
    contentScale = ContentScale.Fit
  )
}

@Composable
fun BackgroundFitBlur(
  url: String?
) {
  RemoteImage(
    model = url,
    shape = RectangleShape,
    modifier = Modifier
      .fillMaxSize(),
    contentScale = ContentScale.FillHeight,
    transformations = listOf(BlurTransformation(LocalContext.current, radius = 24f))
  )
  RemoteImage(
    model = url,
    shape = RectangleShape,
    modifier = Modifier
      .fillMaxSize(),
    contentScale = ContentScale.Fit
  )
}

@Composable
fun BackgroundCrop(
  url: String?
) {
  RemoteImage(
    model = url,
    shape = RectangleShape,
    modifier = Modifier
      .fillMaxSize(),
    contentScale = ContentScale.Crop
  )
}
