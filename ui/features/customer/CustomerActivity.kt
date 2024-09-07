package com.hadisormeyli.marketyaab.ui.features.customer

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hadisormeyli.marketyaab.constant.Constants
import com.hadisormeyli.marketyaab.di.auth_module
import com.hadisormeyli.marketyaab.di.customer_module
import com.hadisormeyli.marketyaab.di.store_module
import com.hadisormeyli.marketyaab.ui.base.BaseActivity
import com.hadisormeyli.marketyaab.ui.features.auth.AuthActivity
import com.hadisormeyli.marketyaab.ui.features.common.components.CustomSnackBar
import com.hadisormeyli.marketyaab.ui.features.common.components.CustomerBottomNavigationBar
import com.hadisormeyli.marketyaab.ui.features.common.toActivity
import com.hadisormeyli.marketyaab.ui.features.customer.cart.CartScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.home.HomeScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.map.MapMainScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.map.MapScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.map.MarkerDetailsScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.map.RoutingScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.map.SearchLocationScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.map.SearchOriginDestScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.orderDetails.QrCodeScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.profile.ProfileScreenRoute
import com.hadisormeyli.marketyaab.ui.navigation.Screen
import com.hadisormeyli.marketyaab.ui.navigation.commonNavGraph
import com.hadisormeyli.marketyaab.ui.navigation.locationGraph
import com.hadisormeyli.marketyaab.ui.session.SessionContract
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class CustomerActivity : BaseActivity() {

    @Composable
    override fun CreateView() {
        val bottomNavHostController = rememberNavController()

        val navBackStackEntry by bottomNavHostController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Scaffold(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding(),
            snackbarHost = {
                SnackbarHost(snackBarHostState, snackbar = { snackBarData ->
                    CustomSnackBar(snackBarData = snackBarData)
                })
            },
            bottomBar = {
                CustomerBottomNavigationBar(navController = bottomNavHostController)
            },
        ) { paddingValues ->
            MapScreenRoute(navController = bottomNavHostController)

            Box(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .let {
                    if (currentRoute == Screen.Customer.Map.route
                        || currentRoute?.contains(Screen.Customer.MarkerDetails.route) == true
                        || currentRoute?.contains(Screen.Customer.Routing.route) == true
                    ) {
                        it.background(Color.Transparent)
                    } else {
                        it
                            .background(MaterialTheme.colorScheme.background)
                            .clickable(indication = null,
                                interactionSource = remember { MutableInteractionSource() }) { }
                    }
                }) {}

            MainScreens(
                modifier = Modifier.padding(paddingValues),
                bottomNavHostController = bottomNavHostController
            )
        }
    }

    override fun loadModules() {
        if (scopes.isCustomerClosed) {
            loadKoinModules(customer_module)
            scopes.isCustomerClosed = false
        }

        if (!scopes.isAuthClosed) {
            unloadKoinModules(auth_module)
            scopes.isAuthClosed = true
        }

        if (!scopes.isStoreClosed) {
            unloadKoinModules(store_module)
            scopes.isStoreClosed = true
        }
    }

    @Composable
    fun MainScreens(
        modifier: Modifier, bottomNavHostController: NavHostController
    ) {
        NavHost(
            modifier = modifier,
            navController = bottomNavHostController,
            route = Screen.Customer.Route.route,
            startDestination = Screen.Customer.Home.route
        ) {
            composable(Screen.Customer.Home.route) {
                HomeScreenRoute(navController = bottomNavHostController)
            }
            composable(Screen.Customer.Map.route) {
                MapMainScreenRoute()
            }
            composable(Screen.Customer.Cart.route) {
                CartScreenRoute(navController = bottomNavHostController)
            }
            composable(Screen.Customer.Profile.route) {
                ProfileScreenRoute(navController = bottomNavHostController)
            }

            composable(Screen.Customer.SearchLocation.route) {
                SearchLocationScreenRoute()
            }

            composable(
                Screen.Customer.QrCode.withArgsFormat(Constants.QR_CODE_ARG),
                listOf(
                    navArgument(name = Constants.QR_CODE_ARG) { type = NavType.StringType }
                )
            ) {
                QrCodeScreenRoute(navController = bottomNavHostController)
            }

            composable(
                Screen.Customer.MarkerDetails.withArgsFormat(
                    Constants.STORE_ID_ARG,
                    Constants.STORE_NAME_ARG,
                    Constants.STORE_IMAGE_ARG,
                    Constants.LATITUDE_ARG,
                    Constants.LONGITUDE_ARG
                ), arguments =
                listOf(
                    navArgument(name = Constants.STORE_ID_ARG) { type = NavType.IntType },
                    navArgument(name = Constants.STORE_NAME_ARG) { type = NavType.StringType },
                    navArgument(name = Constants.STORE_IMAGE_ARG) { type = NavType.StringType },
                    navArgument(name = Constants.LATITUDE_ARG) { type = NavType.FloatType },
                    navArgument(name = Constants.LONGITUDE_ARG) { type = NavType.FloatType }
                )
            ) {
                MarkerDetailsScreenRoute(navController = bottomNavHostController)
            }

            composable(
                Screen.Customer.Routing.withArgsFormat(
                    Constants.STORE_ID_ARG,
                    Constants.STORE_NAME_ARG,
                    Constants.LATITUDE_ARG,
                    Constants.LONGITUDE_ARG
                ), arguments = listOf(navArgument(name = Constants.STORE_ID_ARG) {
                    type = NavType.IntType
                }, navArgument(name = Constants.STORE_NAME_ARG) {
                    type = NavType.StringType
                }, navArgument(name = Constants.LATITUDE_ARG) {
                    type = NavType.FloatType
                }, navArgument(name = Constants.LONGITUDE_ARG) {
                    type = NavType.FloatType
                })
            ) {
                RoutingScreenRoute(navController = bottomNavHostController)
            }

            composable(
                Screen.Customer.OriginDest.route,
            ) {
                SearchOriginDestScreenRoute()
            }

            commonNavGraph(navController = bottomNavHostController)

            locationGraph(navController = bottomNavHostController)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager.state.observe(this) {
            if (it is SessionContract.State.Logout) {
                this.toActivity<AuthActivity>()
            }
        }
    }
}