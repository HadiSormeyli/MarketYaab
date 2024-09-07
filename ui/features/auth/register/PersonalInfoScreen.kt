package com.hadisormeyli.marketyaab.ui.features.auth.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.hadisormeyli.marketyaab.R
import com.hadisormeyli.marketyaab.constant.Constants
import com.hadisormeyli.marketyaab.domain.models.user.UserRole
import com.hadisormeyli.marketyaab.ui.base.BaseActivity
import com.hadisormeyli.marketyaab.ui.features.common.components.CustomOutlinedTextField
import com.hadisormeyli.marketyaab.ui.features.common.components.LoadingDialog
import com.hadisormeyli.marketyaab.ui.features.common.components.LocationImage
import com.hadisormeyli.marketyaab.ui.features.common.components.PasswordTextFiled
import com.hadisormeyli.marketyaab.ui.features.common.components.SimpleAppBar
import com.hadisormeyli.marketyaab.ui.features.common.rememberDebounceClick
import com.hadisormeyli.marketyaab.ui.navigation.Screen
import com.hadisormeyli.marketyaab.ui.navigation.navigateSafely
import com.hadisormeyli.marketyaab.ui.navigation.popBackStackSafely
import com.hadisormeyli.marketyaab.ui.navigation.sharedViewModel
import com.hadisormeyli.marketyaab.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import org.neshan.common.model.LatLng

@Composable
fun PersonalInfoRoute(
    navController: NavController,
) {
    val viewModel = navController.sharedViewModel<RegisterViewModel>()
    val state by viewModel.state

    PersonalInfoScreen(state = state,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::setEvent,
        onNavigationRequested = {
            when (it) {
                is RegisterContract.SideEffect.Navigation.Back -> {
                    navController.popBackStackSafely()
                }

                is RegisterContract.SideEffect.Navigation.ToStoreInfo -> {
                    navController.navigateSafely(Screen.Unauthenticated.StoreInfo.route)
                }

                is RegisterContract.SideEffect.Navigation.ToLocation -> {
                    navController.navigateSafely(
                        Screen.Unauthenticated.Location.withArgs(
                            (-2),
                            it.latitude,
                            it.longitude
                        )
                    )
                }

                else -> {}
            }
        })

    if (state.userRole == UserRole.CUSTOMER)
        LaunchedEffect(navController) {
            navController.currentBackStackEntryFlow.collect { backStackEntry ->
                if (backStackEntry.destination.route == Screen.Unauthenticated.PersonalInfo.route) {
                    val latitude: Double =
                        backStackEntry.savedStateHandle.get<Double>(Constants.LATITUDE_ARG)
                            ?: Constants.CENTER_LOCATION_LAT
                    val longitude: Double =
                        backStackEntry.savedStateHandle.get<Double>(Constants.LONGITUDE_ARG)
                            ?: Constants.CENTER_LOCATION_LONG


                    val location = LatLng(latitude, longitude)
                    viewModel.setEvent(RegisterContract.Event.LocationChanged(location))
                }
            }
        }
    else if(state.userRole == null) {
        navController.navigate(Screen.Unauthenticated.UserRole.route) {
            popUpTo(Screen.Unauthenticated.RegisterRoute.route) {
                inclusive = true
            }
        }
    }
}

@Composable
fun PersonalInfoScreen(
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

    PersonalInfoContent(
        userRole = state.userRole,
        location = state.location,
        user = state.user,
        onEventSent = onEventSent
    )

    LoadingDialog(isLoading = state.isLoading)
}

@Composable
fun PersonalInfoContent(
    userRole: UserRole?,
    location: LatLng,
    user: RegisterContract.User,
    onEventSent: (event: RegisterContract.Event) -> Unit
) {
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
        SimpleAppBar(title = stringResource(id = R.string.personal_info),
            onBackClick = { onEventSent(RegisterContract.Event.Back) })

        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            item {
                CustomOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = user.firstName,
                    onValueChange = {
                        onEventSent(RegisterContract.Event.FirstNameChanged(it))
                    },
                    label = stringResource(R.string.first_name),
                    isError = !user.firstNameValidation.isValid && user.firstName.isNotEmpty(),
                    errorText = user.firstNameValidation.errorMessage?.asString(),
                    leadingIcon = Icons.Filled.Person,
                    keyboardType = KeyboardType.Text,
                    maxLength = 15,
                )

                CustomOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = user.lastName,
                    onValueChange = {
                        onEventSent(RegisterContract.Event.LastNameChanged(it))
                    },
                    label = stringResource(R.string.last_name),
                    isError = !user.lastNameValidation.isValid && user.lastName.isNotEmpty(),
                    errorText = user.lastNameValidation.errorMessage?.asString(),
                    leadingIcon = Icons.Filled.Info,
                    keyboardType = KeyboardType.Text,
                    maxLength = 15,
                )

                PasswordTextFiled(
                    modifier = Modifier.fillMaxWidth(),
                    value = user.password,
                    onValueChange = {
                        onEventSent(RegisterContract.Event.PasswordChanged(it))
                    },
                    label = stringResource(R.string.password),
                    isError = !user.passwordValidation.isValid && user.password.isNotEmpty(),
                    errorText = user.passwordValidation.errorMessage?.asString(),
                    leadingIcon = Icons.Filled.Lock,
                    imeAction = ImeAction.Done,
                )

                if (userRole == UserRole.CUSTOMER)
                    Column(
                        modifier = Modifier.clickable(onClick = {
                            onEventSent(RegisterContract.Event.ToLocation)
                        })
                    ) {
                        Row(
                            modifier = Modifier.padding(start = AppTheme.dimensions.paddingMedium),
                            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.marginMedium)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = stringResource(id = R.string.location),
                            )

                            Text(text = stringResource(id = R.string.location))
                        }

                        Spacer(modifier = Modifier.height(AppTheme.dimensions.marginSmall))

                        LocationImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(AppTheme.dimensions.cardImageHeight),
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                    }
            }
        }

        FilledIconButton(modifier = Modifier
            .fillMaxWidth()
            .height(AppTheme.dimensions.buttonHeight),
            shape = MaterialTheme.shapes.small,
            enabled = user.firstNameValidation.isValid && user.lastNameValidation.isValid && user.passwordValidation.isValid,
            onClick = rememberDebounceClick {
                onEventSent(RegisterContract.Event.ConfirmPersonalInfo)
            }) {
            Text(text = stringResource(R.string.confirm))
        }
    }
}