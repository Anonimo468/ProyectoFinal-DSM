package com.dsm.tud

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.appcompat.widget.SearchView
import android.widget.Button

class CatalogActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CatalogAdapter
    private lateinit var api: ApiService
    private var currentPage = 1
    private val limit = 6 // Número de productos por página

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columnas
        adapter = CatalogAdapter(mutableListOf())
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://67106a3ca85f4164ef2de5d3.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ApiService::class.java)

        // Cargar la primera página
        loadPage(currentPage)
        setupPagination()

        // Configurar la barra de búsqueda
        val searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchProducts(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        // Configurar el botón de regreso al inicio
        val backToTopButton = findViewById<Button>(R.id.btn_back_to_top)
        backToTopButton.setOnClickListener {
            val intent = Intent(this@CatalogActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadPage(page: Int) {
        api.obtenerProductos(page, limit).enqueue(object : Callback<List<Model>> {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        adapter.addProducts(products)
                    }
                } else {
                    val error = response.errorBody()?.string()
                    showError("Error al obtener productos: $error")
                }
            }

            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                showError("Error de conexión: ${t.message}")
            }
        })
    }

    private fun searchProducts(query: String) {
        api.searchProducts(query).enqueue(object : Callback<List<Model>> {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        adapter.setProducts(products)
                    }
                } else {
                    val error = response.errorBody()?.string()
                    showError("Error al buscar productos: $error")
                }
            }

            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                showError("Error de conexión: ${t.message}")
            }
        })
    }

    private fun setupPagination() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                    currentPage++
                    loadPage(currentPage)
                }
            }
        })
    }

    private fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}


