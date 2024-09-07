package com.hadisormeyli.marketyaab.ui.navigation

import com.hadisormeyli.marketyaab.constant.Constants.ADD_COMMENT_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.ADD_PRODUCT_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.AUTHENTICATED_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.CART_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.CATEGORY_PRODUCTS_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.CODE_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.CUSTOMER_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.EDIT_PROFILE_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.EDIT_STORE_INFO_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.FAVORITE_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.HOME_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.LOCATION_NAV_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.LOCATION_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.LOGIN_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.MAIN_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.MARKER_DETAILS_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.MY_COMMENTS_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.MY_LOCATIONS_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.ORDERS_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.ORDER_DETAILS_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.ORIGIN_DEST_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.PERSONAL_INFO_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.PHONE_NUMBER_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.PRODUCT_COMMENTS_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.PRODUCT_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.PROFILE_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.QR_CODE_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.REGISTER_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.ROUTING_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.SCAN_QR_CODE_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.SEARCH_LOCATION_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.SPLASH_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.STORE_INFO_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.STORE_PRODUCTS_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.STORE_PRODUCT_DETAILS_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.STORE_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.STORE_WORK_TIME_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.UNAUTHENTICATED_ROUTE
import com.hadisormeyli.marketyaab.constant.Constants.USER_ROLE_ROUTE

sealed class Screen(val route: String) {

    data object Splash : Screen(SPLASH_ROUTE)

    sealed class Unauthenticated(route: String) : Screen(route) {
        data object Route : Unauthenticated(UNAUTHENTICATED_ROUTE)

        data object Login : Unauthenticated(LOGIN_ROUTE)

        data object RegisterRoute : Unauthenticated(REGISTER_ROUTE)
        data object UserRole : Unauthenticated(USER_ROLE_ROUTE)
        data object PhoneNumber : Unauthenticated(PHONE_NUMBER_ROUTE)
        data object Code : Unauthenticated(CODE_ROUTE)
        data object PersonalInfo : Unauthenticated(PERSONAL_INFO_ROUTE)
        data object StoreInfo : Unauthenticated(STORE_INFO_ROUTE)
        data object StoreWorkTime : Unauthenticated(STORE_WORK_TIME_ROUTE)

        data object LocationRoute : Unauthenticated(LOCATION_NAV_ROUTE)
        data object Location : Unauthenticated(LOCATION_ROUTE)
        data object SearchLocation : Unauthenticated(SEARCH_LOCATION_ROUTE)
    }

    sealed class Authenticated(route: String) : Screen(route) {
        data object Route : Authenticated(AUTHENTICATED_ROUTE)
        data object CustomerRoute : Authenticated(CUSTOMER_ROUTE)
        data object StoreRoute : Authenticated(STORE_ROUTE)
    }

    sealed class Customer(route: String) : Authenticated("${CUSTOMER_ROUTE}_$route") {
        data object Route : Customer(CUSTOMER_ROUTE)
        data object Main : Customer(MAIN_ROUTE)
        data object Product : Customer(PRODUCT_ROUTE)
        data object OrderDetails : Customer(ORDER_DETAILS_ROUTE)
        data object CategoryProducts : Customer(CATEGORY_PRODUCTS_ROUTE)
        data object Store : Customer(STORE_ROUTE)
        data object EditProfile : Customer(EDIT_PROFILE_ROUTE)
        data object ProductComments : Customer(PRODUCT_COMMENTS_ROUTE)
        data object AddComment : Customer(ADD_COMMENT_ROUTE)
        data object MyComments : Customer(MY_COMMENTS_ROUTE)
        data object Favorite : Customer(FAVORITE_ROUTE)
        data object Orders : Customer(ORDERS_ROUTE)
        data object MyLocations : Customer(MY_LOCATIONS_ROUTE)
        data object Home : Customer(HOME_ROUTE)
        data object MarkerDetails : Customer(MARKER_DETAILS_ROUTE)
        data object Routing : Customer(ROUTING_ROUTE)
        data object OriginDest : Customer(ORIGIN_DEST_ROUTE)
        data object Map : Customer(LOCATION_ROUTE)
        data object SearchLocation : Customer(SEARCH_LOCATION_ROUTE)
        data object Cart : Customer(CART_ROUTE)
        data object Profile : Customer(PROFILE_ROUTE)
        data object QrCode : Customer(QR_CODE_ROUTE)
    }

    sealed class Store(route: String) : Authenticated("${STORE_ROUTE}_$route") {
        data object Route : Store(STORE_ROUTE)
        data object Main : Store(MAIN_ROUTE)
        data object StoreProductDetails : Store(STORE_PRODUCT_DETAILS_ROUTE)
        data object AddProduct : Store(ADD_PRODUCT_ROUTE)
        data object PersonalInfo : Store(PERSONAL_INFO_ROUTE)
        data object EditStoreInfo : Store(EDIT_STORE_INFO_ROUTE)
        data object StoreInfo : Store(STORE_INFO_ROUTE)
        data object StoreWorkTime : Store(STORE_WORK_TIME_ROUTE)
        data object Home : Store(HOME_ROUTE)
        data object ScanQrCode : Store(SCAN_QR_CODE_ROUTE)
        data object Profile : Store(PROFILE_ROUTE)
        data object StoreProducts : Store(STORE_PRODUCTS_ROUTE)

    }

    fun withArgs(vararg args: Any): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    fun withArgsFormat(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/{$arg}")
            }
        }
    }
}