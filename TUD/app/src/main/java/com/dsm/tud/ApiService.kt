package com.dsm.tud

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("Productos")
    fun obtenerProductos(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<List<Model>>


    @GET("Categorias")
    fun obtenerCategorias(): Call<List<String>> // Ajusta según el tipo de respuesta de tu API


    @GET("Productos")
    fun searchProducts(
        @Query("search") query: String
    ): Call<List<Model>>
}
