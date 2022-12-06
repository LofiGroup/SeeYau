package com.lofigroup.seeyau.ui

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.ui.navigation.Screen
import com.sillyapps.core.ui.util.navigateToTopDestination

@Composable
fun BottomBar(navController: NavHostController) {
  val items = listOf(
    Screen.NavigatorScreen,
    Screen.ChatListScreen,
    Screen.SettingsScreen
  )

  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  BottomNavigation() {
    items.forEach { item ->
      BottomNavigationItem(
        selected = currentRoute == item.route,
        onClick = { navController.navigateToTopDestination(item.route) },
        icon = { Icon(imageVector = item.imageVector, contentDescription = null) }
      )
    }
  }
}

@Preview
@Composable
fun BottomBarPreview() {
  val navController = rememberNavController()
  
  AppTheme {
    BottomBar(navController = navController)
  }
}