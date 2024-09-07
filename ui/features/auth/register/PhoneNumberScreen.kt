package com.hadisormeyli.marketyaab.ui.features.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import com.hadisormeyli.marketyaab.R
import com.hadisormeyli.marketyaab.ui.base.BaseActivity
import com.hadisormeyli.marketyaab.ui.features.common.components.CustomOutlinedTextField
import com.hadisormeyli.marketyaab.ui.features.common.components.LoadingDialog
import com.hadisormeyli.marketyaab.ui.features.common.components.SimpleAppBar
import com.hadisormeyli.marketyaab.ui.features.common.rememberDebounceClick
import com.hadisormeyli.marketyaab.ui.navigation.Screen
import com.hadisormeyli.marketyaab.ui.navigation.navigateSafely
import com.hadisormeyli.marketyaab.ui.navigation.popBackStackSafely
import com.hadisormeyli.marketyaab.ui.navigation.sharedViewModel
import com.hadisormeyli.marketyaab.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun PhoneNumberScreenRoute(
    navController: NavController,
) {
    val viewModel = navController.sharedViewModel<RegisterViewModel>()
    val state by viewModel.state

    PhoneNumberScreen(
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
fun PhoneNumberScreen(
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

    PhoneNumberContent(user = state.user, onEventSent = onEventSent)

    LoadingDialog(isLoading = state.isLoading)
}

@Composable
fun PhoneNumberContent(
    user: RegisterContract.User,
    onEventSent: (event: RegisterContract.Event) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = AppTheme.dimensions.paddingMedium,
                vertical = AppTheme.dimensions.paddingSmall,
            ),
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
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .aspectRatio(1f),
                painter = painterResource(id = R.drawable.mobile_illustration),
                contentDescription = null
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = user.phoneNumber,
                onValueChange = {
                    onEventSent(RegisterContract.Event.PhoneNumberChanged(it))
                },
                label = stringResource(R.string.phone_number),
                isError = !user.phoneNumberValidation.isValid && user.phoneNumber.isNotEmpty(),
                errorText = user.phoneNumberValidation.errorMessage?.asString(),
                leadingIcon = Icons.Filled.Phone,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
                maxLength = 11,
            )

            Spacer(modifier = Modifier.height(AppTheme.dimensions.marginSmall))

            FilledIconButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTheme.dimensions.buttonHeight),
                shape = MaterialTheme.shapes.small,
                enabled = user.phoneNumberValidation.isValid,
                onClick = rememberDebounceClick {
                    onEventSent(RegisterContract.Event.ConfirmPhoneNumber)
                }
            ) {
                Text(text = stringResource(R.string.confirm))
            }
        }

        TextButton(onClick = dropUnlessResumed {
            onEventSent(RegisterContract.Event.Back)
        }) {
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append(stringResource(R.string.do_have_account))
                }
                append(stringResource(R.string.login))
            })
        }
    }
}