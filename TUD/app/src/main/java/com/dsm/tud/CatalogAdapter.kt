package com.dsm.tud

// CatalogAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CatalogAdapter(private val products: MutableList<Model>) : RecyclerView.Adapter<CatalogAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.catalog_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun addProducts(newProducts: List<Model>) {
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    fun clearProducts() {
        products.clear()
        notifyDataSetChanged()
    }

    // In CatalogAdapter class
    fun setProducts(products: List<Model>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }


    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.catalog_image)
        private val nameView: TextView = itemView.findViewById(R.id.catalog_name)
        private val priceView: TextView = itemView.findViewById(R.id.catalog_price)
        private val categoryView: TextView = itemView.findViewById(R.id.catalog_category)

        fun bind(product: Model) {
            nameView.text = product.nombre
            priceView.text = "${product.precio}"
            categoryView.text = product.categoria
            Glide.with(itemView.context).load(product.img).into(imageView)
        }
    }
}
