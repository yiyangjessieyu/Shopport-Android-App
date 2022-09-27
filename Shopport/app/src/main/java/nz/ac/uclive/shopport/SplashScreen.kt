package nz.ac.uclive.shopport

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import nz.ac.uclive.shopport.ui.theme.FascinateFontFamily

@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 2000
        )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2500)
        navController.popBackStack()
        navController.navigate(ShopportScreens.WISHLIST.route)
    }


    Splash(alpha = alphaAnim.value)
}

@Composable
fun Splash(alpha: Float) {
    val expanded = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        delay(1000)
        expanded.value = true
    }
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)  {
            Image(
                painter = painterResource(id = R.drawable.shopport_logo_only),
                contentDescription = null,
                modifier = Modifier
                    .alpha(alpha)
                    .size(120.dp)
            )
            AnimatedVisibility(visible = expanded.value) {
                Text(text = stringResource(R.string.shopport), modifier = Modifier.alpha(alpha = alpha),
                    fontFamily = FascinateFontFamily,
                    fontSize = 40.sp,
                    color = MaterialTheme.colorScheme.primary)
            }

        }

    }
}
