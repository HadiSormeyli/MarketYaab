package com.hadisormeyli.marketyaab.ui.features.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.hadisormeyli.marketyaab.domain.models.user.UserRole
import com.hadisormeyli.marketyaab.ui.base.BaseActivity
import com.hadisormeyli.marketyaab.ui.features.common.components.SimpleAppBar
import com.hadisormeyli.marketyaab.ui.navigation.Screen
import com.hadisormeyli.marketyaab.ui.navigation.navigateSafely
import com.hadisormeyli.marketyaab.ui.navigation.popBackStackSafely
import com.hadisormeyli.marketyaab.ui.navigation.sharedViewModel
import com.hadisormeyli.marketyaab.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun UserRoleScreenRoute(
    navController: NavController,
) {
    val viewModel = navController.sharedViewModel<RegisterViewModel>()
    UserRoleScreen(
        effectFlow = viewModel.effect,
        onEventSent = viewModel::setEvent,
        onNavigationRequested = {
            when (it) {
                is RegisterContract.SideEffect.Navigation.Back -> {
                    navController.popBackStackSafely()
                }

                is RegisterContract.SideEffect.Navigation.ToPhoneNumber -> {
                    navController.navigateSafely(Screen.Unauthenticated.PhoneNumber.route)
                }

                else -> {}
            }
        })
}

@Composable
fun UserRoleScreen(
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                horizontal = AppTheme.dimensions.paddingMedium,
                vertical = AppTheme.dimensions.paddingSmall
            ),
        verticalArrangement = Arrangement.spacedBy(
            AppTheme.dimensions.marginSmall, Alignment.CenterVertically
        ),
    ) {
        SimpleAppBar(
            title = stringResource(id = R.string.user_role),
            onBackClick = { onEventSent(RegisterContract.Event.Back) }
        )

        LazyColumn {
            item {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            onEventSent(RegisterContract.Event.SelectUserRole(UserRole.STORE))
                        })
                ) {
                    Column(
                        modifier = Modifier.padding(AppTheme.dimensions.paddingMedium),
                    ) {
                        Text(text = stringResource(id = R.string.i_own_store))

                        Image(
                            modifier = Modifier
                                .height(AppTheme.dimensions.profileImageSize)
                                .fillMaxWidth(),
                            painter = painterResource(id = R.drawable.store_illustration),
                            contentDescription = stringResource(id = R.string.mobile_image),
                            alignment = Alignment.CenterEnd
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AppTheme.dimensions.marginSmall))

                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            onEventSent(RegisterContract.Event.SelectUserRole(UserRole.CUSTOMER))
                        })
                ) {
                    Column(
                        modifier = Modifier.padding(AppTheme.dimensions.paddingMedium),
                    ) {
                        Text(text = stringResource(id = R.string.i_am_customer))

                        Image(
                            modifier = Modifier
                                .height(AppTheme.dimensions.profileImageSize)
                                .fillMaxWidth(),
                            painter = painterResource(id = R.drawable.customer_illustration),
                            contentDescription = stringResource(id = R.string.mobile_image),
                            alignment = Alignment.CenterEnd
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = dropUnlessResumed {
            onEventSent(RegisterContract.Event.Back)
        }) {
            Text(
                modifier = Modifier.fillMaxWidth(), text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                        append(stringResource(R.string.do_have_account))
                    }
                    append(stringResource(R.string.login))
                }, textAlign = TextAlign.Center
            )
        }
    }
}