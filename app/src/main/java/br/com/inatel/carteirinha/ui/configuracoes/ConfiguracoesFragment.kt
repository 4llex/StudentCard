package br.com.inatel.carteirinha.ui.configuracoes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.inatel.carteirinha.databinding.FragmentConfiguracoesBinding
import br.com.inatel.carteirinha.ui.carteirinha.Estudante
import br.com.inatel.carteirinha.ui.carteirinha.EstudantePreferences
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.view.inputmethod.EditorInfo
import android.content.Context
import android.widget.Filter
import android.widget.Filterable

class ConfiguracoesFragment : Fragment() {

    private var _binding: FragmentConfiguracoesBinding? = null
    private val binding get() = _binding!!

    private lateinit var estudantePreferences: EstudantePreferences

    private val cursos = listOf(
        "Engenharia da Computação",
        "Engenharia Elétrica",
        "Engenharia de Software",
        "Engenharia Biomédica",
        "Engenharia de Controle e Automação"
    )

    private val niveisEnsino = listOf(
        "Graduação",
        "Pós-graduação"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfiguracoesBinding.inflate(
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

        configurarPicklistCursos()
        configurarPicklistNivelEnsino()
        carregarDados()
        configurarBotaoSalvar()
        configurarLimpezaDeErros()
        configurarDataNascimento()
    }

    private fun configurarPicklistCursos() {
        val adapter = ListaCompletaAdapter(
            requireContext(),
            cursos
        )

        binding.editCurso.setAdapter(adapter)
        binding.editCurso.threshold = 0

        binding.editCurso.setOnClickListener {
            binding.editCurso.error = null
            binding.editCurso.showDropDown()
        }
    }

    private fun configurarPicklistNivelEnsino() {
        val adapter = ListaCompletaAdapter(
            requireContext(),
            niveisEnsino
        )

        binding.editNivelEnsino.setAdapter(adapter)
        binding.editNivelEnsino.threshold = 0

        binding.editNivelEnsino.setOnClickListener {
            binding.editNivelEnsino.error = null
            binding.editNivelEnsino.showDropDown()
        }
    }

    private fun carregarDados() {

        val estudante = estudantePreferences.carregarEstudante()

        binding.editNome.setText(estudante.nome)
        binding.editMatricula.setText(estudante.matricula)
        binding.editDataNascimento.setText(estudante.dataNascimento)
        binding.editCurso.setText(estudante.curso, false)
        binding.editInstituicao.setText(estudante.instituicao)
        binding.editNivelEnsino.setText(estudante.nivelEnsino, false)
    }

    private fun configurarBotaoSalvar() {

        binding.buttonSalvar.setOnClickListener {
            salvarDados()
        }
    }

    private fun salvarDados() {

        limparErros()

        val nome = binding.editNome.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val matricula = binding.editMatricula.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val dataNascimento = binding.editDataNascimento.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val curso = binding.editCurso.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val nivelEnsino = binding.editNivelEnsino.text
            ?.toString()
            ?.trim()
            .orEmpty()

        if (nome.isBlank()) {
            mostrarErroNome("Informe o nome.")
            return
        }

        if (matricula.isBlank()) {
            mostrarErroMatricula("Informe a matrícula.")
            return
        }

        if (dataNascimento.isBlank()) {
            mostrarErroDataNascimento(
                "Informe a data de nascimento."
            )
            return
        }

        if (!validarDataNascimento(dataNascimento)) {
            mostrarErroDataNascimento(
                "Informe uma data válida no formato dd/MM/yyyy."
            )
            return
        }

        if (curso !in cursos) {
            mostrarErroCurso("Selecione um curso da lista.")
            return
        }

        if (nivelEnsino !in niveisEnsino) {
            mostrarErroNivelEnsino(
                "Selecione um nível de ensino da lista."
            )
            return
        }

        val validade = calcularValidade()

        val estudante = Estudante(
            nome = nome,
            matricula = matricula,
            dataNascimento = dataNascimento,
            curso = curso,
            instituicao = "INATEL",
            nivelEnsino = nivelEnsino,
            validade = validade
        )

        estudantePreferences.salvarEstudante(estudante)

        Toast.makeText(
            requireContext(),
            "Dados salvos com sucesso!",
            Toast.LENGTH_SHORT
        ).show()

        findNavController().navigateUp()
    }

    private fun validarDataNascimento(
        data: String
    ): Boolean {

        val formato = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale("pt", "BR")
        )

        formato.isLenient = false

        return try {

            val dataConvertida = formato.parse(data)
                ?: return false

            val hoje = Calendar.getInstance()

            val nascimento = Calendar.getInstance()
            nascimento.time = dataConvertida

            nascimento.after(hoje).not()

        } catch (e: ParseException) {
            false
        }
    }

    private fun calcularValidade(): String {

        val calendario = Calendar.getInstance()

        calendario.add(
            Calendar.YEAR,
            1
        )

        val formato = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale("pt", "BR")
        )

        return formato.format(calendario.time)
    }

    private fun configurarLimpezaDeErros() {

        binding.editNome.setOnFocusChangeListener { _, ganhouFoco ->
            if (ganhouFoco) {
                binding.editNome.error = null
            }
        }

        binding.editMatricula.setOnFocusChangeListener { _, ganhouFoco ->
            if (ganhouFoco) {
                binding.editMatricula.error = null
            }
        }

        binding.editDataNascimento.setOnFocusChangeListener { _, ganhouFoco ->
            if (ganhouFoco) {
                binding.editDataNascimento.error = null
            }
        }
    }

    private fun limparErros() {

        binding.editNome.error = null
        binding.editMatricula.error = null
        binding.editDataNascimento.error = null
        binding.editCurso.error = null
        binding.editNivelEnsino.error = null
    }

    private fun mostrarErroNome(mensagem: String) {
        binding.editNome.error = mensagem
        binding.editNome.requestFocus()
    }

    private fun mostrarErroMatricula(mensagem: String) {
        binding.editMatricula.error = mensagem
        binding.editMatricula.requestFocus()
    }

    private fun mostrarErroDataNascimento(mensagem: String) {
        binding.editDataNascimento.error = mensagem
        binding.editDataNascimento.requestFocus()
    }

    private fun mostrarErroCurso(mensagem: String) {
        binding.editCurso.error = mensagem
        binding.editCurso.requestFocus()
    }

    private fun mostrarErroNivelEnsino(mensagem: String) {
        binding.editNivelEnsino.error = mensagem
        binding.editNivelEnsino.requestFocus()
    }

    private class ListaCompletaAdapter(
        context: Context,
        private val itens: List<String>
    ) : ArrayAdapter<String>(
        context,
        android.R.layout.simple_dropdown_item_1line,
        itens
    ), Filterable {

        private val filtro = object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return FilterResults().apply {
                    values = itens
                    count = itens.size
                }
            }

            override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults?
            ) {
                notifyDataSetChanged()
            }
        }

        override fun getFilter(): Filter {
            return filtro
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun formatarDataNascimento() {

        val textoAtual = binding.editDataNascimento.text
            ?.toString()
            ?.replace("/", "")
            ?.trim()
            .orEmpty()

        if (textoAtual.length != 8) {
            return
        }

        val dataFormatada =
            "${textoAtual.substring(0, 2)}/" +
                    "${textoAtual.substring(2, 4)}/" +
                    textoAtual.substring(4, 8)

        binding.editDataNascimento.setText(dataFormatada)
        binding.editDataNascimento.setSelection(
            dataFormatada.length
        )
    }

    private fun configurarDataNascimento() {
        binding.editDataNascimento.setOnFocusChangeListener { _, ganhouFoco ->
            if (!ganhouFoco) {
                formatarDataNascimento()
            } else {
                binding.editDataNascimento.error = null
            }
        }

        binding.editDataNascimento.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                formatarDataNascimento()

                // Remove o foco do campo
                binding.editDataNascimento.clearFocus()

                // Fecha o teclado virtual
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)
                            as InputMethodManager

                inputMethodManager.hideSoftInputFromWindow(
                    binding.editDataNascimento.windowToken,
                    0
                )

                true
            } else {
                false
            }
        }
    }
}