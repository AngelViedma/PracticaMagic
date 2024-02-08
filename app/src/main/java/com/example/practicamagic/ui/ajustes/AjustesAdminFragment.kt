package com.example.practicamagic.ui.ajustes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.practicamagic.R
import com.example.practicamagic.databinding.FragmentAjustesAdminBinding
import com.example.practicamagic.databinding.FragmentDashboardBinding
import com.example.practicamagic.ui.dashboard.DashboardViewModel


class AjustesAdminFragment : Fragment() {

    private var _binding: FragmentAjustesAdminBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = AjustesAdminFragment()
    }

    private lateinit var viewModel: AjustesAdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val ajustesAdminViewModel =
            ViewModelProvider(this).get(AjustesAdminViewModel::class.java)

        _binding = FragmentAjustesAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAdmin
        ajustesAdminViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}