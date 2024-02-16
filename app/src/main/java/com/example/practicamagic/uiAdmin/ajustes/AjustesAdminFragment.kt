package com.example.practicamagic.uiAdmin.ajustes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.practicamagic.databinding.FragmentAjustesAdminBinding


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