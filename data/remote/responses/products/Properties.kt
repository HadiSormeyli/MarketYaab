package com.hadisormeyli.marketyaab.data.remote.responses.products

import com.hadisormeyli.marketyab.domain.models.product.ProductFeature

data class Properties(
    val general: List<ProductFeature>,
    val exclusive: List<ProductFeature>
)