package com.hadisormeyli.marketyaab.data.remote.services

import com.hadisormeyli.marketyaab.data.remote.responses.products.CategoryDto
import com.hadisormeyli.marketyaab.data.remote.responses.products.CommentResponse
import com.hadisormeyli.marketyaab.data.remote.responses.products.ProductDetailsDto
import com.hadisormeyli.marketyab.domain.models.store.SubCategory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CommonService {

    @GET("v1/get_product_categories")
    suspend fun getCategories(): List<CategoryDto>

    @Multipart
    @POST("v1/get_product_sub_categories")
    suspend fun getSubCategories(
        @Part("category_id") categoryId: Int
    ): List<SubCategory>

    @Multipart
    @POST("v1/product_details")
    suspend fun getProductDetails(
        @Part("product_id") productId: Int, @Part("favorite_location_id") favoriteLocationId: Int
    ): ProductDetailsDto

    @Multipart
    @POST("v1/get_product_comments")
    suspend fun getProductComments(
        @Part("product_id") productId: Int, @Part("group_number") page: Int
    ): CommentResponse
}