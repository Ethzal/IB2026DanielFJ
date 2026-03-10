package com.iberdrola.practicas2026.presentation.ui.invoice

import androidx.fragment.app.Fragment
import com.iberdrola.practicas2026.presentation.R
import dagger.hilt.android.AndroidEntryPoint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iberdrola.practicas2026.domain.model.InvoiceDetail
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.getValue
import com.google.android.material.snackbar.Snackbar
import com.iberdrola.practicas2026.presentation.databinding.FragmentInvoiceBinding

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

        binding.switchMode.isChecked = viewModel.usarMocksLocales
        binding.switchMode.text = if (binding.switchMode.isChecked) "Local" else "Remoto"
        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->
            binding.switchMode.text = if (isChecked) "Local" else "Remoto"
            viewModel.toggleMode(isChecked)
        }

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

        // Switch
        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->
            // 1. Cambiamos colores del switch según el modo
            val color = if (isChecked) R.color.white else R.color.text_pagada

            binding.switchMode.thumbTintList = requireContext().getColorStateList(color)
            binding.switchMode.trackTintList = requireContext().getColorStateList(if (isChecked) R.color.gray else R.color.bg_pagada)

            // 2. Feedback
            binding.switchMode.text = if (isChecked) "Local" else "Remoto"
            showModeChangedSnackbar(isChecked)

            // 3. Acción
            viewModel.toggleMode(isChecked)
        }

        // Back button
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
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
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Información")
            .setMessage("Esta factura aún no está disponible.")
            .setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showModeChangedSnackbar(isLocal: Boolean) {
        val message = if (isLocal) "Modo Local: Cargando mocks..." else "Modo Remoto: Conectando a API..."

        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(requireContext().getColor(R.color.brand_green))
            .setTextColor(requireContext().getColor(R.color.white))
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}