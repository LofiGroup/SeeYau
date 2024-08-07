package com.lofigroup.seeyau.features.chat_screen.ui.components

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.widget.ImageView
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lofigroup.seeyau.common.ui.components.SwipeableBox
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.profile.model.User
import com.lofigroup.seeyau.domain.profile.model.getUserPreviewModel
import com.lofigroup.seeyau.common.ui.R as CommonR
import com.lofigroup.seeyau.features.chat_screen.R
import com.sillyapps.core.ui.components.*
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun CloseUserProfileDialog(
  chat: ChatBrief?,
  onDismiss: () -> Unit,
  onChatButtonClick: (Long) -> Unit,
  likeUser: (Boolean, Long) -> Unit
) {
  val coroutineScope = rememberCoroutineScope()
  val swipeableState = rememberSwipeableState(initialValue = 1)

  if (chat != null) {
    Dialog(
      properties = DialogProperties(
        usePlatformDefaultWidth = false
      ),
      onDismissRequest = { coroutineScope.launch { swipeableState.animateTo(1) } }
    ) {
      SwipeableBox(
        onDismiss = onDismiss,
        swipeableState = swipeableState,
        height = LocalConfiguration.current.screenHeightDp.dp
      ) {
        Column(
          modifier = Modifier
            .clip(MaterialTheme.shapes.large)
        ) {
          ProfilePicture(
            user = chat.partner,
            hasNewMessages = chat.newMessagesCount > 0,
            likeUser = likeUser
          )

          TextButton(
            onClick = {
              onChatButtonClick(chat.id)
              coroutineScope.launch { swipeableState.snapTo(1) }
              onDismiss()
            },
            modifier = Modifier
              .padding(LocalSpacing.current.small)
              .fillMaxWidth()
          ) {
            Text(
              text = stringResource(id = R.string.chat_),
              style = MaterialTheme.typography.h2,
              color = MaterialTheme.colors.onSurface,
              modifier = Modifier
                .padding(LocalSpacing.current.medium)
            )
          }
        }
      }
    }
  }
}

@Composable
fun ProfilePicture(
  user: User,
  hasNewMessages: Boolean,
  likeUser: (Boolean, Long) -> Unit
) {
  Box() {
    RemoteImage(
      model = user.imageUrl,
      shape = MaterialTheme.shapes.large,
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
    )

    if (hasNewMessages) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(LocalSpacing.current.extraSmall)
      ) {
        Icon(
          painter = painterResource(id = CommonR.drawable.ic_interaction),
          contentDescription = null
        )
        Text(text = stringResource(id = R.string.sent_you_message))
      }
    }

    val isLiked = user.likedAt != null
    LikeButton(
      isPressed = isLiked,
      likesCount = user.likesCount,
      onClick = { likeUser(it, user.id) },
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(LocalSpacing.current.small)
    )
  }
}

@Composable
fun LikeButton(
  isPressed: Boolean,
  likesCount: Int,
  onClick: (Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  val animationState = rememberStateImageView(
    transitions = StateTransitions.TransitionBuilder()
      .add(
        from = R.drawable.ic_like_disabled,
        to = R.drawable.ic_like_enabled_new,
        anim = R.drawable.anim_like
      )
      .build()
  )

  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    StateImageView(
      state = animationState,
      modifier = Modifier
        .clip(CircleShape)
        .clickable { onClick(!isPressed) },
      currentState = if (isPressed) R.drawable.ic_like_enabled_new else R.drawable.ic_like_disabled
    )

    Text(
      text = likesCount.toString(),
      style = MaterialTheme.typography.h6
    )
  }
}

@Preview
@Composable
fun CloseUserProfileDialogPreview() {
  val context = LocalContext.current
  AppTheme {
    Surface(modifier = Modifier.fillMaxSize()) {
      CloseUserProfileDialog(
        chat = ChatBrief(
          0,
          getUserPreviewModel(),
          createdIn = 0L,
          lastMessage = null,
          newMessagesCount = 1
        ),
        onDismiss = { showToast(context, "Dismissed!") },
        onChatButtonClick = {},
        likeUser = { _, _ -> }
      )
    }
  }
}