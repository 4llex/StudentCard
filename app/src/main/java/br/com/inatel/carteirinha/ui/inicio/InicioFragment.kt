package br.com.inatel.carteirinha.ui.inicio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.inatel.carteirinha.R
import br.com.inatel.carteirinha.databinding.FragmentInicioBinding
import br.com.inatel.carteirinha.ui.carteirinha.EstudantePreferences

class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    private lateinit var estudantePreferences: EstudantePreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(
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

        estudantePreferences = EstudantePreferences(requireContext())

        carregarDados()
        configurarNavegacao()
    }

    override fun onResume() {
        super.onResume()

        if (_binding != null) {
            carregarDados()
        }
    }

    private fun carregarDados() {
        val estudante = estudantePreferences.carregarEstudante()

        binding.textSaudacao.text = "Olá, ${obterPrimeiroNome(estudante.nome)}!"
        binding.textCurso.text = estudante.curso

        binding.textStatusCarteirinha.text = "Carteirinha válida"
        binding.textValidadeCarteirinha.text =
            "Validade: ${estudante.validade}"

        binding.textMatricula.text =
            "Matrícula: ${estudante.matricula}"

        binding.textNivelEnsino.text =
            "Nível: ${estudante.nivelEnsino}"
    }

    private fun obterPrimeiroNome(nomeCompleto: String): String {
        return nomeCompleto
            .trim()
            .split("\\s+".toRegex())
            .firstOrNull()
            .orEmpty()
    }

    private fun configurarNavegacao() {
        binding.cardCarteirinha.setOnClickListener {
            navegarPara(R.id.navigation_carteirinha)
        }

        binding.buttonVerCarteirinha.setOnClickListener {
            navegarPara(R.id.navigation_carteirinha)
        }

        binding.cardGrade.setOnClickListener {
            navegarPara(R.id.navigation_grade)
        }

        binding.cardValidarQr.setOnClickListener {
            navegarPara(R.id.navigation_validacao_qr)
        }
    }

    private fun navegarPara(destinationId: Int) {
        findNavController().navigate(destinationId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}