package com.hadisormeyli.marketyaab.ui.features.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.hadisormeyli.marketyaab.R
import com.hadisormeyli.marketyaab.domain.models.store.StoreWorkTime
import com.hadisormeyli.marketyaab.ui.base.BaseActivity
import com.hadisormeyli.marketyaab.ui.features.common.Utils
import com.hadisormeyli.marketyaab.ui.features.common.components.SimpleAppBar
import com.hadisormeyli.marketyaab.ui.features.common.components.StoreWorkTimeBottomSheet
import com.hadisormeyli.marketyaab.ui.navigation.popBackStackSafely
import com.hadisormeyli.marketyaab.ui.navigation.sharedViewModel
import com.hadisormeyli.marketyaab.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun StoreWorkTimeScreenRoute(
    navController: NavController,
) {
    val viewModel = navController.sharedViewModel<RegisterViewModel>()
    val state by viewModel.state
    StoreWorkTimeScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::setEvent,
        onNavigationRequested = {
            when (it) {
                is RegisterContract.SideEffect.Navigation.Back -> {
                    navController.popBackStackSafely()
                }

                else -> {}
            }
        }
    )
}

@Composable
fun StoreWorkTimeScreen(
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

    StoreWorkTimeContent(
        state.store.workTimes,
        state.store.selectedWorkTime,
        state.store.showWorkTimeBottomSheet,
        onConfirmWorkTime = { workTime, isForAllDays ->
            onEventSent(
                RegisterContract.Event.UpdateWorkTime(
                    workTime,
                    isForAllDays
                )
            )
        },
        onShowBottomSheet = {
            onEventSent(RegisterContract.Event.ShowBottomSheet(it))
        },
        onDismiss = {
            onEventSent(RegisterContract.Event.HideBottomSheet)
        },
        onBack = {
            onEventSent(RegisterContract.Event.Back)
        }
    )
}

@Composable
fun StoreWorkTimeContent(
    workTimes: List<StoreWorkTime>,
    selectedWorkTime: StoreWorkTime?,
    showWorkTimeBottomSheet: Boolean,
    onConfirmWorkTime: (workTime: StoreWorkTime, isForAllDays: Boolean) -> Unit,
    onShowBottomSheet: (StoreWorkTime) -> Unit,
    onDismiss: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        SimpleAppBar(
            modifier = Modifier.padding(
                horizontal = AppTheme.dimensions.paddingMedium,
                vertical = AppTheme.dimensions.paddingSmall
            ),
            title = stringResource(id = R.string.store_work_time),
            onBackClick = onBack
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(workTimes.size) {
                workTimes[it].let { workTime ->
                    Row(
                        modifier = Modifier
                            .clickable(onClick = {
                                onShowBottomSheet(workTime)
                            })
                            .padding(AppTheme.dimensions.paddingMedium),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val day = Utils.toCompleteDayOfWeek(workTime.dayOfWeek).asString()
                        Text(text = day)

                        Spacer(modifier = Modifier.width(AppTheme.dimensions.marginSmall))

                        Text(
                            text = if (workTime.isHoliday) stringResource(id = R.string.is_holiday) else if (workTime.shiftTimes.isEmpty())
                                stringResource(id = R.string.not_set) else Utils.getShiftTimes(
                                workTime.shiftTimes
                            ),
                            color = if (workTime.isHoliday) Color.Red else Color.Gray,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.width(AppTheme.dimensions.marginSmall))

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = day
                        )
                    }

                    HorizontalDivider()
                }
            }
        }
    }

    if (showWorkTimeBottomSheet && selectedWorkTime != null) {
        StoreWorkTimeBottomSheet(
            onDismiss = onDismiss,
            day = selectedWorkTime,
            onConfirmWorkTime = onConfirmWorkTime
        )
    }
}


