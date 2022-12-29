package com.lofigroup.seeyau.features.chat_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.lofigroup.seeyau.features.chat_screen.R
import com.lofigroup.seeyau.common.ui.R as CommonR
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core.ui.util.conditional

private val notFoldable: (Boolean) -> Unit = {}

fun LazyListScope.FolderItem(
  title: String,
  imageResId: Int,
  content: LazyListScope.() -> Unit,
  expanded: Boolean = true,
  setExpanded: (Boolean) -> Unit = notFoldable,
) {
  item {
    FolderHeader(
      isExpanded = expanded,
      text = title,
      painter = painterResource(id = imageResId),
      setExpanded = setExpanded
    )
  }

  if (expanded) {
    content()
  }

}

@Composable
fun FolderHeader(
  isExpanded: Boolean,
  text: String,
  painter: Painter,
  setExpanded: (Boolean) -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.conditional(setExpanded != notFoldable) {
      clickable(onClick = { setExpanded(!isExpanded) })
    }
      .padding(top = LocalSpacing.current.small)
  ) {
    Icon(
      painter = painter,
      contentDescription = null,
    )

    Text(
      text = text,
      style = MaterialTheme.typography.subtitle2
    )

    Spacer(modifier = Modifier.weight(1f))

    if (setExpanded != notFoldable) {
      Icon(
        painter = painterResource(id = if (!isExpanded) CommonR.drawable.ic_arrow_up else CommonR.drawable.ic_arrow_down),
        contentDescription = null
      )
    }
  }
}

@Composable
fun EmptyFolderContent() {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = LocalSpacing.current.medium),
    contentAlignment = Alignment.Center
  ) {
    Image(
      painter = painterResource(id = R.drawable.ic_empty_folder),
      contentDescription = null,
      modifier = Modifier.size(LocalSize.current.verySmall)
    )
  }
}