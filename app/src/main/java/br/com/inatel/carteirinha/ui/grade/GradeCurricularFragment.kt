package br.com.inatel.carteirinha.ui.grade

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.inatel.carteirinha.databinding.FragmentGradeCurricularBinding

class GradeCurricularFragment : Fragment() {

    private var _binding: FragmentGradeCurricularBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GradeCurricularViewModel by viewModels()

    private lateinit var adapter: DisciplinaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGradeCurricularBinding.inflate(
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

        configurarRecyclerView()
        carregarDisciplinas()
        configurarResumo()
    }

    private fun configurarResumo() {

        binding.textTotalDisciplinas.text =
            viewModel.totalDisciplinas.toString()

        binding.textMediaGeral.text =
            viewModel.mediaGeralFormatada()
    }

    private fun configurarRecyclerView() {

        adapter = DisciplinaAdapter(emptyList())

        binding.recyclerDisciplinas.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@GradeCurricularFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun carregarDisciplinas() {

        adapter.atualizarLista(viewModel.disciplinas)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}