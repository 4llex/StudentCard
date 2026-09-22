package br.com.inatel.carteirinha.ui.validacao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.inatel.carteirinha.databinding.FragmentValidacaoQrBinding

class ValidacaoQrFragment : Fragment() {

    private var _binding: FragmentValidacaoQrBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentValidacaoQrBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLerQrCode.setOnClickListener {
            // Scanner será implementado na próxima etapa.
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}