package com.hadisormeyli.marketyaab.constant

import android.Manifest

object Constants {
    //routes
    const val SPLASH_ROUTE = "splash"

    const val UNAUTHENTICATED_ROUTE = "unauthenticated"
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"
    const val USER_ROLE_ROUTE = "user_role"
    const val PHONE_NUMBER_ROUTE = "phone_number"
    const val CODE_ROUTE = "code"
    const val PERSONAL_INFO_ROUTE = "personal_info"
    const val STORE_INFO_ROUTE = "store_info"
    const val EDIT_STORE_INFO_ROUTE = "edit_store_info_route"
    const val STORE_WORK_TIME_ROUTE = "store_work_time"

    const val LOCATION_NAV_ROUTE = "location_route"
    const val LOCATION_ROUTE = "location"
    const val SEARCH_LOCATION_ROUTE = "search_location"

    const val AUTHENTICATED_ROUTE = "authenticated"
    const val CUSTOMER_ROUTE = "customer"
    const val STORE_ROUTE = "store"
    const val STORE_PRODUCTS_ROUTE = "store_products"
    const val EDIT_PROFILE_ROUTE = "edit_profile"
    const val PRODUCT_COMMENTS_ROUTE = "product_comments"
    const val ADD_COMMENT_ROUTE = "add_comment"
    const val MY_COMMENTS_ROUTE = "my_comments"
    const val FAVORITE_ROUTE = "favorite"
    const val ORDERS_ROUTE = "orders"
    const val MY_LOCATIONS_ROUTE = "my_locations"
    const val MAIN_ROUTE = "main"
    const val STORE_PRODUCT_DETAILS_ROUTE = "store_product_details"
    const val ADD_PRODUCT_ROUTE = "add_product"
    const val HOME_ROUTE = "home"
    const val QR_CODE_ROUTE = "qr_code"
    const val SCAN_QR_CODE_ROUTE = "scan_qr_code"
    const val MARKER_DETAILS_ROUTE = "marker_details"
    const val ROUTING_ROUTE = "routing"
    const val ORIGIN_DEST_ROUTE = "origin_dest"
    const val PRODUCT_ROUTE = "product"
    const val ORDER_DETAILS_ROUTE = "order_details"
    const val CATEGORY_PRODUCTS_ROUTE = "category_products"
    const val CART_ROUTE = "cart"
    const val PROFILE_ROUTE = "profile"

    //args
    const val PRODUCT_ID_ARG = "product_id"
    const val CATEGORY_ID_ARG = "category_id"
    const val ORDER_ID_ARG = "order_id"
    const val CATEGORY_NAME_ARG = "category_name"
    const val STORE_ID_ARG = "store_id"
    const val STORE_NAME_ARG = "store_name"
    const val STORE_IMAGE_ARG = "store_image"
    const val STORE_PROFILE_PART = "store_profile_picture"
    const val STORE_PRODUCT_PART = "picture"
    const val LATITUDE_ARG = "latitude"
    const val ADD_MODE_ARG = "add_mode"
    const val LONGITUDE_ARG = "longitude"
    const val QR_CODE_ARG = "qr_code"
    const val MARKER_ID = "marker_id"
    const val MARKER_IMAGE = "marker_image"

    //other
    const val AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY"
    const val REFRESH_AUTH_TOKEN_KEY = "REFRESH_AUTH_TOKEN_KEY"

    //    const val SERVER_URL: String = BuildConfig.BASE_URL
    const val SERVER_URL = "https://floppy-hotels-behave.loca.lt/"
    const val NESHAN_URL: String = "https://api.neshan.org/"

    const val PUBLIC_OKHTTP_CLIENT = "PUBLIC_OKHTTP_CLIENT"
    const val REFRESH_OKHTTP_CLIENT = "REFRESH_OKHTTP_CLIENT"
    const val AUTHENTICATED_OKHTTP_CLIENT = "AUTHENTICATED_OKHTTP_CLIENT"

    const val PUBLIC_RETROFIT = "PUBLIC_RETROFIT"
    const val NESHAN_RETROFIT = "NESHAN_RETROFIT"
    const val REFRESH_RETROFIT = "REFRESH_RETROFIT"
    const val AUTHENTICATED_RETROFIT = "AUTHENTICATED_RETROFIT"

    const val DATABASE_NAME = BuildConfig.APPLICATION_ID
    const val APP_DATASTORE = "app"

    const val UPDATE_INTERVAL_IN_MILLISECONDS = 1000L
    const val MOVE_CAMERA_DELAY = 0.6f
    const val MAP_DEFAULT_ZOOM = 14f
    const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION

    const val SEARCH_INTERVAL_DELAY = 500L
    const val BUTTON_CLICK_DELAY = 300L
    const val SHIMMER_ANIMATION_DURATION = 2500
    const val CENTER_LOCATION_LAT = 35.70204
    const val CENTER_LOCATION_LONG = 51.335352
    const val MIN_CREDIT = 10000
    const val MIN_PRICE = 1000

    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 45L
    const val WRITE_TIMEOUT = 240L
    const val SNACK_BAR_DURATION = 2000L
    const val EXIT_INTERVAL = 2000L

    const val IMAGE_URL = SERVER_URL + "v1/get_picture?picture_id="

    const val PAGE_SIZE = 20
    const val STORE_PAGE_SIZE = 10
    const val COMMENT_PAGE_SIZE = 10
    const val MAX_CART_SET_SIZE = 1
    const val RELOAD_CART_TASK_ID = -1
    const val CONFIRM_CART_TASK_ID = -2
}