package com.hadisormeyli.marketyaab.ui.features.auth.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import com.hadisormeyli.marketyaab.R
import com.hadisormeyli.marketyaab.ui.base.BaseActivity
import com.hadisormeyli.marketyaab.ui.base.RequestState
import com.hadisormeyli.marketyaab.ui.features.common.components.LocationItem
import com.hadisormeyli.marketyaab.ui.features.common.components.LocationShimmerList
import com.hadisormeyli.marketyaab.ui.features.common.components.SearchTextFiled
import com.hadisormeyli.marketyaab.ui.navigation.sharedViewModel
import com.hadisormeyli.marketyaab.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import org.neshan.common.model.LatLng

@Composable
fun SearchLocationScreenRoute(
    navController: NavController,
) {
    val viewModel = navController.sharedViewModel<AddLocationViewModel>()
    val state by viewModel.state
    SearchLocationScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::setEvent,
        onNavigationRequested = {
            when (it) {
                is AddLocationContract.SideEffect.Navigation.Back -> {
                    navController.popBackStack()
                }

                else -> {}
            }
        }
    )
}

@Composable
fun SearchLocationScreen(
    state: AddLocationContract.State,
    effectFlow: Flow<AddLocationContract.SideEffect>?,
    onEventSent: (event: AddLocationContract.Event) -> Unit,
    onNavigationRequested: (AddLocationContract.SideEffect.Navigation) -> Unit
) {
    val context = LocalContext.current as BaseActivity

    LaunchedEffect(effectFlow) {
        effectFlow?.collect {
            when (it) {
                is AddLocationContract.SideEffect.Navigation -> onNavigationRequested(it)
                is AddLocationContract.SideEffect.ShowMessage -> context.showSnackBar(it.message)
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = AppTheme.dimensions.paddingSmall)
    ) {
        SearchTextFiled(query = state.query, onValueChange = {
            onEventSent(AddLocationContract.Event.SearchQueryChanged(it))
        }, onBackButtonClicked = dropUnlessResumed {
            onEventSent(AddLocationContract.Event.Back)
        })

        when (state.locations) {
            is RequestState.Loading -> {
                LocationShimmerList()
            }

            is RequestState.Success -> {
                if (state.locations.data.isEmpty()) {
                    Text(
                        modifier = Modifier.padding(AppTheme.dimensions.paddingMedium),
                        text = stringResource(id = R.string.location_not_found),
                        color = Color.Gray
                    )
                } else {
                    LazyColumn {
                        itemsIndexed(state.locations.data) { index, location ->
                            Column {
                                LocationItem(address = location.address, onLocationItemClick = dropUnlessResumed {
                                    onEventSent(
                                        AddLocationContract.Event.LocationChanged(
                                            LatLng(
                                                location.latitude,
                                                location.longitude
                                            )
                                        )
                                    )
                                    onEventSent(AddLocationContract.Event.Back)
                                })

                                if (index != state.locations.data.lastIndex)
                                    HorizontalDivider()
                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }
}