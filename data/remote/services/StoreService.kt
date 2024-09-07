package com.hadisormeyli.marketyaab.data.remote.services

import com.hadisormeyli.marketyab.data.remote.responses.BaseResponse
import com.hadisormeyli.marketyaab.data.remote.responses.products.ProductDetailsDto
import com.hadisormeyli.marketyab.data.remote.responses.store.GeneralFeatureDto
import com.hadisormeyli.marketyab.data.remote.responses.store.StoreProductsResponse
import com.hadisormeyli.marketyab.domain.models.store.Unit
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface StoreService {

    @Multipart
    @POST("v1/get_seller_products")
    suspend fun getStoreProducts(
        @Part("group_number") page: Int
    ): StoreProductsResponse

    @Multipart
    @POST("v1/get_product_general_properties")
    suspend fun getGeneralFeatures(
        @Part("category_id") categoryId: Int
    ): List<GeneralFeatureDto>

    @Multipart
    @POST("v1/get_product_exclusive_properties")
    suspend fun getExclusiveFeatures(
        @Part("sub_category_id") subCategoryId: Int
    ): List<GeneralFeatureDto>

    @GET("v1/get_product_unit_types")
    suspend fun getUnits(): List<Unit>

    @Multipart
    @POST("/v1/add_product")
    suspend fun addProduct(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("expire_time_epoch") expiationTime: Long,
        @Part("price_per_unit") price: Int,
        @Part("unit_type_id") unit: Int,
        @Part("available_amount") stock: Int,
        @Part("category_id") categoryId: Int,
        @Part("sub_category_id") subCategoryId: Int,
        @Part("general_properties") generalProperties: RequestBody,
        @Part("exclusive_properties") exclusiveProperties: RequestBody,
        @Part picture: MultipartBody.Part?,
    ): ProductDetailsDto

    @Multipart
    @POST("/v1/edit_product")
    suspend fun editProduct(
        @Part("product_id") productId: Int,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("expire_time_epoch") expiationTime: Long,
        @Part("price_per_unit") price: Int,
        @Part("unit_type_id") unit: Int,
        @Part("available_amount") stock: Int,
        @Part("category_id") categoryId: Int,
        @Part("sub_category_id") subCategoryId: Int,
        @Part("general_properties") generalProperties: RequestBody,
        @Part("exclusive_properties") exclusiveProperties: RequestBody,
        @Part picture: MultipartBody.Part?,
    ): ProductDetailsDto

    @Multipart
    @POST("v1/remove_product")
    suspend fun deleteProduct(
        @Part("product_id") productId: Int
    ): BaseResponse
}