package com.dsm.tud


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecursoAdapter(private val Productos: List<Model>) : RecyclerView.Adapter<RecursoAdapter.ViewHolder>() {
    private var onItemClick: OnItemClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imagenView: ImageView = view.findViewById(R.id.ivImagen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recurso_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recurso = Productos[position]

        Glide.with(holder.itemView.context)
            .load(recurso.img)
            .into(holder.imagenView)
        holder.itemView.setOnClickListener {
            onItemClick?.onItemClick(recurso)
        }
    }

    override fun getItemCount(): Int = Productos.size

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClick = listener
    }

    interface OnItemClickListener {
        fun onItemClick(recurso: Model)
    }
}
