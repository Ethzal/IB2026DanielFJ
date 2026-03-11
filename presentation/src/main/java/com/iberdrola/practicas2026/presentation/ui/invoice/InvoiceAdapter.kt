package com.iberdrola.practicas2026.presentation.ui.invoice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iberdrola.practicas2026.presentation.R
import com.iberdrola.practicas2026.domain.model.InvoiceItem
import com.iberdrola.practicas2026.presentation.databinding.ItemInvoiceBinding
import java.util.Locale

class InvoiceAdapter(
    private val facturas: List<InvoiceItem>,
    private val onClick: (InvoiceItem) -> Unit
) : RecyclerView.Adapter<InvoiceAdapter.FacturaViewHolder>() {

    inner class FacturaViewHolder(private val binding: ItemInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InvoiceItem) {
            binding.tvItemDate.text = item.date
            binding.tvItemType.text = item.type
            binding.tvItemAmount.text = String.format(Locale.getDefault(), "%.2f €", item.amount)
            binding.tvItemStatus.text = item.status

            if (item.status == "Pagada") {
                binding.tvItemStatus.setBackgroundResource(R.drawable.bg_pill_pagada)
                binding.tvItemStatus.setTextColor(itemView.context.getColor(R.color.text_pagada))
            } else {
                binding.tvItemStatus.setBackgroundResource(R.drawable.bg_pill_pendient)
                binding.tvItemStatus.setTextColor(itemView.context.getColor(R.color.text_pendient))
            }

            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        val binding = ItemInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FacturaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        holder.bind(facturas[position])
    }

    override fun getItemCount() = facturas.size
}