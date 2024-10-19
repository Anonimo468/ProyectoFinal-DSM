package com.dsm.tud

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecursoAdapter
    private lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar RecyclerView y Adapter
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Inicializar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://67106a3ca85f4164ef2de5d3.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ApiService::class.java)

        // Cargar datos
        cargarDatos(1, 6)

        // Inicializar el botón de catálogo
        val catalogButton = findViewById<Button>(R.id.catalog_button)
        catalogButton.setOnClickListener {
            val intent = Intent(this, CatalogActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarDatos(1, 6)
    }

    private fun cargarDatos(page: Int, limit: Int) {
        api.obtenerProductos(page, limit).enqueue(object : Callback<List<Model>> {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                if (response.isSuccessful) {
                    val productos = response.body()
                    if (productos != null) {
                        Log.d("API", productos.toString())
                        adapter = RecursoAdapter(productos)
                        recyclerView.adapter = adapter
                        adapter.setOnItemClickListener(object : RecursoAdapter.OnItemClickListener {
                            override fun onItemClick(recurso: Model) {
                                mostrarAlertas(recurso)
                            }
                        })
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "Error al obtener Productos: $error")
                    Toast.makeText(this@MainActivity, "Error al obtener Productos: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                Log.e("API", "Error de conexión: ${t.message}")
                Toast.makeText(this@MainActivity, "Error de conexión al obtener Productos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun mostrarAlertas(recurso: Model) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_info, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.dialog_image)
        val productName = dialogView.findViewById<TextView>(R.id.product_name)
        val rangoMedicion = dialogView.findViewById<TextView>(R.id.rango_medicion)
        val precision = dialogView.findViewById<TextView>(R.id.precision)
        val interfaz = dialogView.findViewById<TextView>(R.id.interfaz)
        val uso = dialogView.findViewById<TextView>(R.id.uso)
        val beneficios = dialogView.findViewById<TextView>(R.id.beneficios)
        val voltajeOperacion = dialogView.findViewById<TextView>(R.id.voltaje_operacion)
        val dimensiones = dialogView.findViewById<TextView>(R.id.dimensiones)
        val categoria = dialogView.findViewById<TextView>(R.id.categoria)
        val precio = dialogView.findViewById<TextView>(R.id.precio)

        // Log the image URL
        Log.d("Producto", recurso.img)
        Glide.with(this).load(recurso.img).into(imageView)
        productName.text = recurso.nombre
        rangoMedicion.text = recurso.caracteristicas.rangoDeMedicion
        precision.text = recurso.caracteristicas.precision
        interfaz.text = recurso.caracteristicas.interfaz
        uso.text = recurso.uso
        beneficios.text = recurso.beneficios
        voltajeOperacion.text = recurso.especificaciones.voltajeDeOperacion
        dimensiones.text = recurso.especificaciones.dimensiones
        categoria.text = recurso.categoria
        precio.text = recurso.precio.toString()

        val builder = AlertDialog.Builder(this)
        builder.setTitle(recurso.nombre)
        builder.setView(dialogView)
        builder.setPositiveButton("Cerrar", null)
        builder.show()
    }
}
