package com.hadisormeyli.marketyaab.ui.features.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import com.hadisormeyli.marketyaab.R
import com.hadisormeyli.marketyaab.ui.base.BaseActivity
import com.hadisormeyli.marketyaab.ui.features.common.components.LoadingDialog
import com.hadisormeyli.marketyaab.ui.features.common.components.SimpleAppBar
import com.hadisormeyli.marketyaab.ui.navigation.Screen
import com.hadisormeyli.marketyaab.ui.navigation.navigateSafely
import com.hadisormeyli.marketyaab.ui.navigation.popBackStackSafely
import com.hadisormeyli.marketyaab.ui.navigation.sharedViewModel
import com.hadisormeyli.marketyaab.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun ConfirmPhoneNumberRoute(
    navController: NavController,
) {
    val viewModel = navController.sharedViewModel<RegisterViewModel>()
    val state by viewModel.state

    ConfirmPhoneNumberScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::setEvent,
        onNavigationRequested = {
            when (it) {
                is RegisterContract.SideEffect.Navigation.Back -> {
                    navController.popBackStackSafely()
                }

                RegisterContract.SideEffect.Navigation.ToPersonalInfo -> {
                    navController.navigateSafely(Screen.Unauthenticated.PersonalInfo.route)
                }

                else -> {}
            }
        }
    )
}

@Composable
fun ConfirmPhoneNumberScreen(
    state: RegisterContract.State,
    effectFlow: Flow<RegisterContract.SideEffect>?,
    onEventSent: (event: RegisterContract.Event) -> Unit,
    onNavigationRequested: (RegisterContract.SideEffect.Navigation) -> Unit
) {
    val context = LocalContext.current as BaseActivity

    LaunchedEffect(effectFlow) {
        effectFlow?.collect {
            when (it) {
                is RegisterContract.SideEffect.Navigation -> onNavigationRequested(it)
                is RegisterContract.SideEffect.ShowMessage -> context.showSnackBar(it.message)
            }
        }
    }

    ConfirmPhoneNumberContent(user = state.user, onEventSent = onEventSent)

    LoadingDialog(isLoading = state.isLoading)
}

@Composable
fun ConfirmPhoneNumberContent(
    user: RegisterContract.User,
    onEventSent: (event: RegisterContract.Event) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                PaddingValues(
                    end = AppTheme.dimensions.paddingMedium,
                    start = AppTheme.dimensions.paddingMedium,
                    top = AppTheme.dimensions.paddingSmall,
                    bottom = AppTheme.dimensions.paddingSmall
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            AppTheme.dimensions.marginSmall, Alignment.CenterVertically
        ),
    ) {
        SimpleAppBar(
            title = stringResource(id = R.string.phone_number),
            onBackClick = { onEventSent(RegisterContract.Event.Back) }
        )
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.mobile_illustration),
                contentDescription = null
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.phone_number))
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(user.phoneNumber)
                    }
                },
                textAlign = TextAlign.Start
            )

            FilledIconButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTheme.dimensions.buttonHeight),
                shape = MaterialTheme.shapes.small,
                enabled = true,
                onClick = { onEventSent(RegisterContract.Event.ConfirmCode) }
            ) {
                Text(text = stringResource(R.string.confirm))
            }
        }

        TextButton(onClick = dropUnlessResumed {
            onEventSent(RegisterContract.Event.Back)
        }) {
            Text(text = stringResource(R.string.edit_phone_number))
        }
    }
}