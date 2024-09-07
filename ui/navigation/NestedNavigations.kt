package com.hadisormeyli.marketyaab.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.hadisormeyli.marketyaab.constant.Constants
import com.hadisormeyli.marketyaab.ui.features.auth.location.AddLocationScreenRoute
import com.hadisormeyli.marketyaab.ui.features.auth.location.SearchLocationScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.comment.CommentScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.comment.add.AddCommentScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.comment.mycomments.MyCommentsScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.home.category.CategoryProductsScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.product.ProductScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.profile.edit.EditProfileScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.favorite.FavoriteScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.locations.MyLocationsScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.orderDetails.OrdersDetailsDetailsScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.orders.OrdersScreenRoute
import com.hadisormeyli.marketyaab.ui.features.customer.storeDetails.StoreDetailsRoute
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.locationGraph(navController: NavController) {
    navigation(
        route = Screen.Unauthenticated.LocationRoute.route,
        startDestination = Screen.Unauthenticated.Location.route
    ) {
        composable(
            route = Screen.Unauthenticated.Location.withArgsFormat(
                Constants.ADD_MODE_ARG,
                Constants.LATITUDE_ARG,
                Constants.LONGITUDE_ARG
            ),
            arguments = listOf(
                navArgument(Constants.ADD_MODE_ARG) { type = NavType.IntType },
                navArgument(Constants.LATITUDE_ARG) { type = NavType.FloatType },
                navArgument(Constants.LONGITUDE_ARG) { type = NavType.FloatType }
            )
        ) {
            AddLocationScreenRoute(navController = navController)
        }

        composable(
            route = Screen.Unauthenticated.SearchLocation.route
        ) {
            SearchLocationScreenRoute(navController = navController)
        }
    }
}

fun NavGraphBuilder.commonNavGraph(navController: NavController) {
    navigation(
        route = Screen.Authenticated.CustomerRoute.route,
        startDestination = Screen.Customer.Product.route
    ) {
        composable(
            Screen.Customer.Product.withArgsFormat(Constants.PRODUCT_ID_ARG),
            arguments = listOf(navArgument(name = Constants.PRODUCT_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            ProductScreenRoute(navController = navController)
        }

        composable(
            Screen.Customer.Store.withArgsFormat(Constants.STORE_ID_ARG),
            arguments = listOf(navArgument(name = Constants.STORE_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            StoreDetailsRoute(navController = navController)
        }

        composable(Screen.Customer.EditProfile.route) {
            EditProfileScreenRoute(navController = navController)
        }

        composable(
            Screen.Customer.ProductComments.withArgsFormat(Constants.PRODUCT_ID_ARG),
            arguments = listOf(navArgument(name = Constants.PRODUCT_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            CommentScreenRoute(navController = navController)
        }

        composable(
            Screen.Customer.AddComment.withArgsFormat(Constants.PRODUCT_ID_ARG),
            arguments = listOf(navArgument(name = Constants.PRODUCT_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            AddCommentScreenRoute(navController = navController)
        }

        composable(Screen.Customer.MyComments.route) {
            MyCommentsScreenRoute(navController = navController)
        }

        composable(Screen.Customer.Favorite.route) {
            FavoriteScreenRoute(navController = navController)
        }

        composable(Screen.Customer.Orders.route) {
            OrdersScreenRoute(navController = navController)
        }

        composable(
            Screen.Customer.OrderDetails.withArgsFormat(Constants.ORDER_ID_ARG), arguments = listOf(
                navArgument(name = Constants.ORDER_ID_ARG) {
                    type = NavType.IntType
                },
            )
        ) {
            OrdersDetailsDetailsScreenRoute(navController = navController)
        }

        composable(Screen.Customer.MyLocations.route) {
            MyLocationsScreenRoute(navController = navController)
        }

        composable(
            Screen.Customer.CategoryProducts.withArgsFormat(
                Constants.CATEGORY_ID_ARG,
                Constants.CATEGORY_NAME_ARG
            ),
            arguments = listOf(
                navArgument(name = Constants.CATEGORY_ID_ARG) {
                    type = NavType.IntType
                },
                navArgument(name = Constants.CATEGORY_NAME_ARG) {
                    type = NavType.StringType
                },
            )
        ) {
            CategoryProductsScreenRoute(navController = navController)
        }
    }
}

fun NavController.navigateSafely(route: String, builder: NavOptionsBuilder.() -> Unit = {}) {
    currentBackStackEntry?.lifecycle?.let { lifecycle ->
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            currentDestination?.route?.let {
                if (it != route) {
                    this.navigate(route, navOptions { builder() })
                }
            }
        }
    }
}


fun NavController.popBackStackSafely() {
    currentBackStackEntry?.lifecycle?.let {
        val state = it.currentState.coerceAtLeast(Lifecycle.State.STARTED)
        if (state == Lifecycle.State.STARTED || state == Lifecycle.State.RESUMED) {
            this.popBackStack()
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavController.sharedViewModel(): T {
    val navGraphRoute =
        this.currentBackStackEntry?.destination?.parent?.route ?: return koinViewModel<T>()

    val parentEntry = remember(this.currentBackStackEntry) {
        getBackStackEntry(navGraphRoute)
    }

    return koinViewModel<T>(viewModelStoreOwner = parentEntry)
}

@Composable
inline fun <reified T : ViewModel> sharedViewModel(): T {
    return koinViewModel<T>(viewModelStoreOwner = LocalContext.current as ComponentActivity)
}
