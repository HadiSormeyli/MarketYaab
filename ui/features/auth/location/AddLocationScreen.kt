package com.hadisormeyli.marketyaab.ui.features.auth.location

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.hadisormeyli.marketyaab.R
import com.hadisormeyli.marketyaab.constant.Constants
import com.hadisormeyli.marketyaab.ui.base.BaseActivity
import com.hadisormeyli.marketyaab.ui.features.common.Utils
import com.hadisormeyli.marketyaab.ui.features.common.components.AddLocationMap
import com.hadisormeyli.marketyaab.ui.features.common.components.SimpleAppBar
import com.hadisormeyli.marketyaab.ui.navigation.Screen
import com.hadisormeyli.marketyaab.ui.navigation.navigateSafely
import com.hadisormeyli.marketyaab.ui.navigation.popBackStackSafely
import com.hadisormeyli.marketyaab.ui.navigation.sharedViewModel
import com.hadisormeyli.marketyaab.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun AddLocationScreenRoute(
    navController: NavController,
) {
    val viewModel = navController.sharedViewModel<AddLocationViewModel>()
    val state by viewModel.state

    AddLocationScreen(state = state,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::setEvent,
        onNavigationRequested = { navigation ->
            when (navigation) {
                is AddLocationContract.SideEffect.Navigation.Back -> navController.popBackStackSafely()

                is AddLocationContract.SideEffect.Navigation.ToSearch -> {
                    navController.navigateSafely(Screen.Unauthenticated.SearchLocation.route)
                }

                is AddLocationContract.SideEffect.Navigation.BackWithArgs -> {
                    navController.previousBackStackEntry?.savedStateHandle?.let {
                        it[Constants.LATITUDE_ARG] = navigation.location.latitude
                        it[Constants.LONGITUDE_ARG] = navigation.location.longitude
                    }
                    navController.popBackStackSafely()
                }
            }
        })
}

@Composable
fun AddLocationScreen(
    state: AddLocationContract.State,
    effectFlow: Flow<AddLocationContract.SideEffect>?,
    onEventSent: (event: AddLocationContract.Event) -> Unit,
    onNavigationRequested: (AddLocationContract.SideEffect.Navigation) -> Unit
) {
    val context = LocalContext.current as BaseActivity

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Utils.showFindMyLocationDialog(
                    context as Activity,
                    findMyLocation = {
                        onEventSent(AddLocationContract.Event.MyLocationPermissionGranted)
                    }
                )
            } else {
                onEventSent(AddLocationContract.Event.LocationPermissionDenied)
            }
        }

    LaunchedEffect(effectFlow) {
        effectFlow?.collect {
            when (it) {
                is AddLocationContract.SideEffect.Navigation -> onNavigationRequested(it)
                is AddLocationContract.SideEffect.ShowMessage -> context.showSnackBar(it.message)
                AddLocationContract.SideEffect.RequestMyLocationPermission -> {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Constants.LOCATION_PERMISSION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Utils.showFindMyLocationDialog(
                            context as Activity,
                            findMyLocation = {
                                onEventSent(AddLocationContract.Event.MyLocationPermissionGranted)
                            }
                        )
                    } else {
                        launcher.launch(Constants.LOCATION_PERMISSION)
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AddLocationMap(
            modifier = Modifier.fillMaxSize(),
            location = state.selectedLocation,
            onConfirmLocation = {
                onEventSent(AddLocationContract.Event.ConfirmLocation(it))
            },
            onMyLocationClicked = {
                onEventSent(AddLocationContract.Event.MyLocationClicked)
            }
        )

        Box(
            modifier = Modifier
                .padding(
                    vertical = AppTheme.dimensions.paddingSmall,
                    horizontal = AppTheme.dimensions.paddingMedium,
                )
                .clickable {
                    onEventSent(AddLocationContract.Event.ToSearch)
                }
        ) {
            SimpleAppBar(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.medium
                    )
                    .border(1.dp, Color.Gray.copy(alpha = 0.4f), MaterialTheme.shapes.medium),
                title = state.query.ifEmpty { stringResource(id = R.string.search) },
                onBackClick = {
                    onEventSent(AddLocationContract.Event.Back)
                },
            )
        }
    }
}