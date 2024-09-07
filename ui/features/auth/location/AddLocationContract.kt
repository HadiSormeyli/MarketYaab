package com.hadisormeyli.marketyaab.ui.features.auth.location

import com.hadisormeyli.marketyaab.constant.Constants.CENTER_LOCATION_LAT
import com.hadisormeyli.marketyaab.constant.Constants.CENTER_LOCATION_LONG
import com.hadisormeyli.marketyaab.domain.models.location.SearchLocation
import com.hadisormeyli.marketyaab.ui.base.RequestState
import com.hadisormeyli.marketyaab.ui.base.ViewEvent
import com.hadisormeyli.marketyaab.ui.base.ViewSideEffect
import com.hadisormeyli.marketyaab.ui.base.ViewState
import com.hadisormeyli.marketyaab.ui.features.common.UiText
import org.neshan.common.model.LatLng

class AddLocationContract {

    sealed class Event : ViewEvent {
        data class ConfirmLocation(val location: LatLng) : Event()
        data class LocationChanged(val location: LatLng) : Event()
        data class SearchQueryChanged(val query: String) : Event()
        data object LocationPermissionDenied : Event()
        data object MyLocationPermissionGranted : Event()
        data object MyLocationClicked : Event()
        data object ToSearch : Event()
        data object Back : Event()
    }

    data class State(
        val selectedLocation: LatLng = LatLng(CENTER_LOCATION_LAT, CENTER_LOCATION_LONG),
        val query: String = "",
        val locations: RequestState<List<SearchLocation>> = RequestState.Empty(),
        val isLoading: Boolean = false
    ) : ViewState

    sealed class SideEffect : ViewSideEffect {
        sealed class Navigation : SideEffect() {
            data object ToSearch : Navigation()
            data object Back : Navigation()
            data class BackWithArgs(val location: LatLng) : Navigation()
        }

        data class ShowMessage(val message: UiText) : SideEffect()
        data object RequestMyLocationPermission : SideEffect()
    }
}