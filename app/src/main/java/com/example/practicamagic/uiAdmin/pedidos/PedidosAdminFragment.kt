package com.example.practicamagic.uiAdmin.pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.practicamagic.databinding.FragmentPedidosAdminBinding
import com.example.practicamagic.uiAdmin.ventas.VentasAdminFragment

class PedidosAdminFragment : Fragment() {

    lateinit var binding: FragmentPedidosAdminBinding
    lateinit var viewModel: PedidosAdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPedidosAdminBinding.inflate(inflater, container, false)
        viewModel= ViewModelProvider(this)[PedidosAdminViewModel::class.java]

        initObservers()

        return binding.root
    }

    private fun initObservers() {
        viewModel.text.observe(viewLifecycleOwner) {
        }
    }
    companion object {
        fun newInstance() = VentasAdminFragment()
    }
}