package com.pemmob.pawangdigimon.data.model

import com.google.gson.annotations.SerializedName

data class DigimonListResponse(
    @SerializedName("content") val content: List<DigimonSummary> = emptyList(),
    @SerializedName("pageable") val pageable: Pageable? = null
)

data class DigimonSummary(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("href") val href: String? = null,
    @SerializedName("image") val image: String? = null
)

data class Pageable(
    @SerializedName("currentPage") val currentPage: Int = 0,
    @SerializedName("elementsOnPage") val elementsOnPage: Int = 0,
    @SerializedName("totalElements") val totalElements: Int = 0,
    @SerializedName("totalPages") val totalPages: Int = 0
)

data class DigimonDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("xAntibody") val xAntibody: Boolean = false,
    @SerializedName("images") val images: List<DigimonImage> = emptyList(),
    @SerializedName("levels") val levels: List<DigimonLevel> = emptyList(),
    @SerializedName("types") val types: List<DigimonType> = emptyList(),
    @SerializedName("attributes") val attributes: List<DigimonAttribute> = emptyList(),
    @SerializedName("fields") val fields: List<DigimonField> = emptyList(),
    @SerializedName("releaseDate") val releaseDate: String? = null,
    @SerializedName("descriptions") val descriptions: List<DigimonDescription> = emptyList()
)

data class DigimonImage(
    @SerializedName("href") val href: String,
    @SerializedName("transparent") val transparent: Boolean = false
)

data class DigimonLevel(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("level") val level: String
)

data class DigimonType(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("type") val type: String
)

data class DigimonAttribute(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("attribute") val attribute: String
)

data class DigimonField(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("field") val field: String,
    @SerializedName("image") val image: String? = null
)

data class DigimonDescription(
    @SerializedName("origin") val origin: String? = null,
    @SerializedName("language") val language: String? = null,
    @SerializedName("description") val description: String
)
