package com.hadisormeyli.marketyaab.ui.features.auth.location

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hadisormeyli.marketyaab.R
import com.hadisormeyli.marketyaab.constant.Constants
import com.hadisormeyli.marketyaab.domain.usecases.common.SearchLocationUseCase
import com.hadisormeyli.marketyaab.domain.utils.Resource
import com.hadisormeyli.marketyaab.ui.base.BaseViewModel
import com.hadisormeyli.marketyaab.ui.base.RequestState
import com.hadisormeyli.marketyaab.ui.features.common.LocationHelper
import com.hadisormeyli.marketyaab.ui.features.common.UiText
import com.hadisormeyli.marketyaab.ui.features.customer.map.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.neshan.common.model.LatLng

@OptIn(FlowPreview::class)
class AddLocationViewModel(
    savedStateHandle: SavedStateHandle,
    private val locationRepository: LocationRepository?,
    private val locationHelper: LocationHelper,
    private val searchLocation: SearchLocationUseCase
) : BaseViewModel<AddLocationContract.Event, AddLocationContract.State, AddLocationContract.SideEffect>() {
    override fun setInitialState(): AddLocationContract.State = AddLocationContract.State()

    private val addMode = savedStateHandle.get<Int>(Constants.ADD_MODE_ARG)?.toInt() ?: -2

    private val searchQueryFlow = MutableSharedFlow<String>()

    init {
        val latitude: Double =
            savedStateHandle.get<Float>(Constants.LATITUDE_ARG)?.toDouble()
                ?: state.value.selectedLocation.latitude
        val longitude: Double =
            savedStateHandle.get<Float>(Constants.LONGITUDE_ARG)?.toDouble()
                ?: state.value.selectedLocation.longitude

        val location = LatLng(latitude, longitude)
        setState { copy(selectedLocation = location) }

        viewModelScope.launch {
            searchQueryFlow.debounce(Constants.SEARCH_INTERVAL_DELAY)
                .distinctUntilChanged()
                .flowOn(Dispatchers.IO)
                .collectLatest { query ->
                    performSearch(query)
                }
        }
    }

    override fun onTriggerEvent(event: AddLocationContract.Event) {
        when (event) {
            is AddLocationContract.Event.ConfirmLocation -> confirmLocation(event.location)
            is AddLocationContract.Event.LocationChanged -> setState { copy(selectedLocation = event.location) }
            is AddLocationContract.Event.SearchQueryChanged -> changeSearchQuery(event.query)
            is AddLocationContract.Event.ToSearch -> setEffect { AddLocationContract.SideEffect.Navigation.ToSearch }
            is AddLocationContract.Event.Back -> setEffect { AddLocationContract.SideEffect.Navigation.Back }
            AddLocationContract.Event.LocationPermissionDenied -> setEffect {
                AddLocationContract.SideEffect.ShowMessage(
                    UiText.StringResource(
                        R.string.location_permission_denied
                    )
                )
            }

            AddLocationContract.Event.MyLocationPermissionGranted -> findMyLocation()
            AddLocationContract.Event.MyLocationClicked -> setEffect { AddLocationContract.SideEffect.RequestMyLocationPermission }
        }
    }

    private fun findMyLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            locationHelper.getLastLocation()?.let {
                withContext(Dispatchers.Main) {
                    setState { copy(selectedLocation = LatLng(it.latitude, it.longitude)) }
                }
            }
        }
    }

    private fun confirmLocation(latLng: LatLng) {
        // addMode = -2 -> default : for sign up
        // addMode = -1 -> add new location
        // addMode = 0 -> edit location
        Log.d("TAG", "confirmLocation: ${locationRepository}")
        if (addMode != -2) {
            viewModelScope.launch {
                locationRepository?.addEditLocation(
                    addMode = addMode,
                    latLng,
                    onError = {
                        setState { copy(isLoading = false) }
                        setEffect { AddLocationContract.SideEffect.ShowMessage(it.message) }
                    },
                    onLoading = {
                        setState { copy(isLoading = true) }
                    },
                    onSuccess = {
                        setEffect { AddLocationContract.SideEffect.Navigation.Back }
                    }
                )
            }
        } else
            setEffect { AddLocationContract.SideEffect.Navigation.BackWithArgs(latLng) }
    }

    private fun changeSearchQuery(query: String) {
        setState { copy(query = query) }
        viewModelScope.launch { searchQueryFlow.emit(query) }
    }

    private suspend fun performSearch(query: String) {
        if (query.isEmpty()) {
            setState {
                copy(locations = RequestState.Empty())
            }
            return
        }

        searchLocation(query).collectLatest {
            when (it) {
                is Resource.Error -> {
                    setEffect { AddLocationContract.SideEffect.ShowMessage(it.error.message) }
                    setState { copy(locations = RequestState.Empty()) }
                }

                is Resource.Loading -> setState {
                    copy(locations = RequestState.Loading())
                }

                is Resource.Success -> setState {
                    copy(locations = RequestState.Success(it.data))
                }
            }
        }
    }
}