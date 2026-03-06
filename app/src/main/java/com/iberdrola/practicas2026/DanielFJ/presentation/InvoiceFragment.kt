package com.iberdrola.practicas2026.DanielFJ.presentation

import androidx.fragment.app.Fragment
import com.iberdrola.practicas2026.DanielFJ.R
import dagger.hilt.android.AndroidEntryPoint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.iberdrola.practicas2026.DanielFJ.data.InvoiceDetail
import com.iberdrola.practicas2026.DanielFJ.databinding.FragmentInvoiceBinding
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.getValue

@AndroidEntryPoint
class InvoiceFragment : Fragment(R.layout.fragment_invoice) {

    private val viewModel: InvoiceViewModel by viewModels()

    private var _binding: FragmentInvoiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvInvoices.layoutManager = LinearLayoutManager(requireContext())

        // Decidir si usar mocks locales
        viewModel.usarMocksLocales = true
        viewModel.fetchFacturas()

        // Observar Estados usando Coroutines
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is InvoiceViewModel.UiState.Loading -> {
                        binding.viewFlipper.displayedChild = 0
                        binding.shimmerViewContainer.startShimmer()
                    }
                    is InvoiceViewModel.UiState.Success -> {
                        binding.shimmerViewContainer.stopShimmer()
                        binding.viewFlipper.displayedChild = 1

                        llenarUltimaFactura(state.data.lastInvoice)

                        binding.rvInvoices.adapter = InvoiceAdapter(state.data.history) { _ ->
                            mostrarDialogoNoDisponible()
                        }
                    }
                    is InvoiceViewModel.UiState.Error -> {
                        binding.shimmerViewContainer.stopShimmer()
                        Toast.makeText(requireContext(), state.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun llenarUltimaFactura(factura: InvoiceDetail) {
        binding.tvLastInvoiceType.text = factura.type
        val formattedAmount = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(factura.amount)
        binding.tvLastInvoiceAmount.text = formattedAmount
        binding.tvLastInvoiceDate.text = String.format(
            Locale.getDefault(),
            "%s - %s",
            factura.startDate,
            factura.endDate
        )
        binding.tvLastInvoiceStatus.text = factura.status

        if (factura.status == "Pagada") {
            binding.tvLastInvoiceStatus.setBackgroundResource(R.drawable.bg_pill_pagada)
            binding.tvLastInvoiceStatus.setTextColor(requireContext().getColor(R.color.text_pagada))
        } else {
            binding.tvLastInvoiceStatus.setBackgroundResource(R.drawable.bg_pill_pendient)
            binding.tvLastInvoiceStatus.setTextColor(requireContext().getColor(R.color.text_pendient))
        }
    }

    private fun mostrarDialogoNoDisponible() {
        AlertDialog.Builder(requireContext())
            .setTitle("Información")
            .setMessage("Esta factura aún no está disponible.")
            .setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}