package com.dsm.tud

data class Model(
    val nombre: String,
    val caracteristicas: Caracteristicas,
    val uso: String,
    val beneficios: String,
    val especificaciones: Especificaciones,
    val categoria: String,
    val img: String,
    val precio: Int,
    val id: String
)

data class Caracteristicas(
    val rangoDeMedicion: String,
    val precision: String,
    val interfaz: String
)

data class Especificaciones(
    val voltajeDeOperacion: String,
    val dimensiones: String
)
