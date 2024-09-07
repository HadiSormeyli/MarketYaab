package com.hadisormeyli.marketyaab.ui.features.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hadisormeyli.marketyaab.R
import com.hadisormeyli.marketyaab.constant.Constants
import com.hadisormeyli.marketyaab.domain.models.user.UserRole
import com.hadisormeyli.marketyaab.ui.base.BaseActivity
import com.hadisormeyli.marketyaab.ui.features.common.Utils
import com.hadisormeyli.marketyaab.ui.features.common.components.CustomOutlinedTextField
import com.hadisormeyli.marketyaab.ui.features.common.components.ImagePicker
import com.hadisormeyli.marketyaab.ui.features.common.components.LoadingDialog
import com.hadisormeyli.marketyaab.ui.features.common.components.LocationImage
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
fun StoreInfoRoute(
    navController: NavController,
) {
    val viewModel = navController.sharedViewModel<RegisterViewModel>()
    val state by viewModel.state

    StoreInfoScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::setEvent,
        onNavigationRequested = {
            when (it) {
                is RegisterContract.SideEffect.Navigation.Back -> {
                    navController.popBackStackSafely()
                }

                is RegisterContract.SideEffect.Navigation.ToStoreWorkTime -> {
                    navController.navigateSafely(Screen.Unauthenticated.StoreWorkTime.route)
                }

                is RegisterContract.SideEffect.Navigation.ToLocation -> {
                    navController.navigateSafely(
                        Screen.Unauthenticated.Location.withArgs(
                            (-2).toString(),
                            it.latitude.toString(),
                            it.longitude.toString()
                        )
                    )
                }

                else -> {}
            }
        }
    )

    LaunchedEffect(navController) {
        if (state.userRole == UserRole.STORE) {
            navController.currentBackStackEntryFlow.collect { backStackEntry ->
                if (backStackEntry.destination.route == Screen.Unauthenticated.StoreInfo.route) {
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
        } else {
            navController.navigate(Screen.Unauthenticated.UserRole.route) {
                popUpTo(Screen.Unauthenticated.RegisterRoute.route) {
                    inclusive = true
                }
            }
        }
    }
}

@Composable
fun StoreInfoScreen(
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

    StoreInfoContent(
        userRole = state.userRole,
        location = state.location,
        store = state.store,
        onEventSent = onEventSent
    )

    LoadingDialog(isLoading = state.isLoading)
}

@Composable
fun StoreInfoContent(
    userRole: UserRole?,
    location: LatLng,
    store: RegisterContract.Store,
    onEventSent: (event: RegisterContract.Event) -> Unit,
) {
    val showError = remember {
        mutableStateOf(!store.workTimes.all {
            it.isHoliday || Utils.validateShiftTimes(it.shiftTimes)
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                vertical = AppTheme.dimensions.paddingSmall,
                horizontal = AppTheme.dimensions.paddingMedium,
            ),
        verticalArrangement = Arrangement.spacedBy(
            AppTheme.dimensions.marginSmall
        ),
    ) {
        SimpleAppBar(
            title = stringResource(id = R.string.store_info),
            onBackClick = { onEventSent(RegisterContract.Event.Back) }
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f),
        ) {
            item {
                ImagePicker(
                    modifier = Modifier
                        .size(AppTheme.dimensions.profileImageSize)
                        .clip(CircleShape),
                    uri = store.profileImage,
                    onProfileChanged = {
                        onEventSent(RegisterContract.Event.StoreProfileChanged(it))
                    },
                    onCameraPermissionDenied = {
                        onEventSent(RegisterContract.Event.CameraPermissionDenied)
                    },
                )

                Spacer(modifier = Modifier.height(AppTheme.dimensions.marginMedium))

                CustomOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = store.storeName,
                    onValueChange = {
                        onEventSent(RegisterContract.Event.StoreNameChanged(it))
                    },
                    label = stringResource(R.string.first_name),
                    isError = !store.storeNameValidation.isValid && store.storeName.isNotEmpty(),
                    errorText = store.storeNameValidation.errorMessage?.asString(),
                    leadingIcon = Icons.Filled.Person,
                    keyboardType = KeyboardType.Text,
                    maxLength = 20,
                    imeAction = ImeAction.Done,
                )

                Spacer(modifier = Modifier.height(AppTheme.dimensions.marginSmall))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = MaterialTheme.shapes.small
                        )
                        .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.small)
                        .clickable { onEventSent(RegisterContract.Event.ToStoreWorkTime) }
                        .padding(AppTheme.dimensions.paddingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_timer_24),
                        contentDescription = stringResource(
                            id = R.string.store_work_time
                        ),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(AppTheme.dimensions.marginMedium))

                    Text(
                        text = stringResource(id = R.string.store_work_time),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(AppTheme.dimensions.marginSmall))
                    Spacer(modifier = Modifier.weight(1f))

                    if (showError.value)
                        Text(
                            text = stringResource(id = R.string.not_set_error),
                            color = Color.Red,
                            style = MaterialTheme.typography.labelMedium
                        )
                    else Text(
                        text = stringResource(id = R.string.all_days_set),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Spacer(modifier = Modifier.height(AppTheme.dimensions.marginSmall))

                if (userRole == UserRole.STORE)
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
                                contentDescription = stringResource(id = R.string.store_location),
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
            enabled = store.storeNameValidation.isValid && !showError.value,
            onClick = rememberDebounceClick {
                onEventSent(RegisterContract.Event.ConfirmStoreInfo)
            }) {
            Text(text = stringResource(R.string.confirm))
        }
    }
}

