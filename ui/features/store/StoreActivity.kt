package com.hadisormeyli.marketyaab.ui.features.store

import android.os.Bundle
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hadisormeyli.marketyaab.constant.Constants
import com.hadisormeyli.marketyaab.di.auth_module
import com.hadisormeyli.marketyaab.di.customer_module
import com.hadisormeyli.marketyaab.di.store_module
import com.hadisormeyli.marketyaab.ui.base.BaseActivity
import com.hadisormeyli.marketyaab.ui.features.auth.AuthActivity
import com.hadisormeyli.marketyaab.ui.features.common.components.CustomSnackBar
import com.hadisormeyli.marketyaab.ui.features.common.components.StoreBottomNavigationBar
import com.hadisormeyli.marketyaab.ui.features.common.toActivity
import com.hadisormeyli.marketyaab.ui.features.store.products.StoreProductsScreenRoute
import com.hadisormeyli.marketyaab.ui.features.store.products.add.AddProductScreenRoute
import com.hadisormeyli.marketyaab.ui.features.store.products.details.StoreProductDetailsScreenRoute
import com.hadisormeyli.marketyaab.ui.features.store.profile.StoreProfileScreenRoute
import com.hadisormeyli.marketyaab.ui.features.store.profile.peronal.StorePersonalInfoScreenRoute
import com.hadisormeyli.marketyaab.ui.features.store.profile.storeInfo.StoreInfoScreenRoute
import com.hadisormeyli.marketyaab.ui.features.store.profile.storeInfo.StoreWorkTimeScreenRoute
import com.hadisormeyli.marketyaab.ui.features.store.qr_code.ScanQrCodeScreenRoute
import com.hadisormeyli.marketyaab.ui.navigation.Screen
import com.hadisormeyli.marketyaab.ui.navigation.commonNavGraph
import com.hadisormeyli.marketyaab.ui.navigation.locationGraph
import com.hadisormeyli.marketyaab.ui.session.SessionContract
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class StoreActivity : BaseActivity() {
    @Composable
    override fun CreateView() {
        val navController = rememberNavController()

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
                StoreBottomNavigationBar(navController = navController)
            }
        ) {
            NavHost(
                modifier = Modifier.padding(it),
                navController = navController,
                route = Screen.Store.Route.route,
                startDestination = Screen.Store.StoreProducts.route
            ) {
                composable(Screen.Store.ScanQrCode.route) {
                    ScanQrCodeScreenRoute(navController = navController)
                }

                composable(Screen.Store.StoreProducts.route) {
                    StoreProductsScreenRoute(
                        navController = navController,
                    )
                }

                composable(
                    route = Screen.Store.AddProduct.withArgsFormat(Constants.PRODUCT_ID_ARG),
                    arguments = listOf(navArgument(Constants.PRODUCT_ID_ARG) {
                        type = NavType.IntType
                    })
                ) {
                    AddProductScreenRoute(navController = navController)
                }

                composable(Screen.Store.Profile.route) {
                    StoreProfileScreenRoute(
                        navController = navController,
                    )
                }

                composable(
                    route = Screen.Store.PersonalInfo.route,
                ) {
                    StorePersonalInfoScreenRoute(navController = navController)
                }

                navigation(
                    route = Screen.Store.EditStoreInfo.route,
                    startDestination = Screen.Store.StoreInfo.route,
                ) {
                    composable(
                        route = Screen.Store.StoreInfo.route,
                    ) {
                        StoreInfoScreenRoute(navController = navController)
                    }

                    composable(
                        route = Screen.Store.StoreWorkTime.route,
                    ) {
                        StoreWorkTimeScreenRoute(navController = navController)
                    }
                }

                composable(
                    Screen.Store.StoreProductDetails.withArgsFormat(Constants.PRODUCT_ID_ARG),
                    exitTransition = {
                        return@composable slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                        )
                    }, popEnterTransition = {
                        return@composable slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                        )
                    },
                    arguments = listOf(navArgument(name = Constants.PRODUCT_ID_ARG) {
                        type = NavType.IntType
                    })
                ) {
                    StoreProductDetailsScreenRoute(navController = navController)
                }

                commonNavGraph(navController = navController)

                locationGraph(navController = navController)
            }
        }
    }

    override fun loadModules() {
        if (scopes.isStoreClosed) {
            loadKoinModules(store_module)
            scopes.isStoreClosed = false
        }

        if (!scopes.isAuthClosed) {
            unloadKoinModules(auth_module)
            scopes.isAuthClosed = true
        }

        if (!scopes.isCustomerClosed) {
            unloadKoinModules(customer_module)
            scopes.isCustomerClosed = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager.state.observe(this) {
            when (it) {
                SessionContract.State.Logout -> {
                    this.toActivity<AuthActivity>()
                }

                else -> {}
            }
        }
    }
}