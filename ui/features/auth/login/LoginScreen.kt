package com.hadisormeyli.marketyaab.ui.features.auth.login

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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.navigation.NavController
import com.hadisormeyli.marketyaab.R
import com.hadisormeyli.marketyaab.ui.base.BaseActivity
import com.hadisormeyli.marketyaab.ui.base.RequestState
import com.hadisormeyli.marketyaab.ui.features.common.components.CustomOutlinedTextField
import com.hadisormeyli.marketyaab.ui.features.common.components.LoadingDialog
import com.hadisormeyli.marketyaab.ui.features.common.components.PasswordTextFiled
import com.hadisormeyli.marketyaab.ui.features.common.rememberDebounceClick
import com.hadisormeyli.marketyaab.ui.navigation.Screen
import com.hadisormeyli.marketyaab.ui.navigation.navigateSafely
import com.hadisormeyli.marketyaab.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoute(
    navController: NavController,
) {
    val viewModel = koinViewModel<LoginViewModel>()
    val state by viewModel.state
    LoginScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::setEvent,
        onNavigationRequested = {
            when (it) {
                is LoginContact.SideEffect.Navigation.ToRegister -> {
                    navController.navigateSafely(Screen.Unauthenticated.RegisterRoute.route)
                }
            }
        }
    )
}

@Composable
fun LoginScreen(
    state: LoginContact.State,
    effectFlow: Flow<LoginContact.SideEffect>?,
    onEventSent: (event: LoginContact.Event) -> Unit,
    onNavigationRequested: (LoginContact.SideEffect.Navigation) -> Unit,
) {
    val context = LocalContext.current as BaseActivity

    LaunchedEffect(effectFlow) {
        effectFlow?.collect {
            when (it) {
                is LoginContact.SideEffect.Navigation -> onNavigationRequested(it)
                is LoginContact.SideEffect.ShowMessage -> context.showSnackBar(it.message)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = AppTheme.dimensions.paddingMedium,
                vertical = AppTheme.dimensions.paddingSmall,
            ),
        verticalArrangement = Arrangement.spacedBy(
            AppTheme.dimensions.marginSmall, Alignment.CenterVertically
        ),
    ) {
        Box(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .aspectRatio(1f),
                painter = painterResource(id = R.drawable.login_illustration),
                contentDescription = stringResource(id = R.string.mobile_image),
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                CustomOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.phoneNumber,
                    onValueChange = {
                        onEventSent(LoginContact.Event.PhoneNumberChanged(it))
                    },
                    label = stringResource(R.string.phone_number),
                    isError = !state.phoneNumberValidation.isValid && state.phoneNumber.isNotEmpty(),
                    errorText = state.phoneNumberValidation.errorMessage?.asString(),
                    leadingIcon = Icons.Filled.Phone,
                    keyboardType = KeyboardType.Number,
                    maxLength = 11,
                )

                PasswordTextFiled(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.password,
                    onValueChange = { onEventSent(LoginContact.Event.PasswordChanged(it)) },
                    label = stringResource(R.string.password),
                    isError = !state.passwordValidation.isValid && state.password.isNotEmpty(),
                    errorText = state.passwordValidation.errorMessage?.asString(),
                    imeAction = ImeAction.Done
                )

                FilledIconButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppTheme.dimensions.buttonHeight),
                    shape = MaterialTheme.shapes.small,
                    enabled = state.phoneNumberValidation.isValid && state.passwordValidation.isValid
                            && state.loginState is RequestState.Empty,
                    onClick = rememberDebounceClick {
                        onEventSent(
                            LoginContact.Event.Login(
                                state.phoneNumber,
                                state.password
                            )
                        )
                    }
                ) {
                    Text(text = stringResource(R.string.login))
                }

                Spacer(modifier = Modifier.height(AppTheme.dimensions.marginSmall))

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onEventSent(LoginContact.Event.ToRegister) }) {
                    Text(text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                            append(stringResource(R.string.do_not_have_account) + " ")
                        }
                        append(stringResource(R.string.register))
                    })
                }
            }
        }
    }

    LoadingDialog(isLoading = state.loginState is RequestState.Loading)
}
