package br.com.inatel.carteirinha.ui.carteirinha

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EstudantePreferences(context: Context) {

    private val preferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    fun salvarEstudante(estudante: Estudante) {
        preferences.edit()
            .putString(KEY_NOME, estudante.nome)
            .putString(KEY_MATRICULA, estudante.matricula)
            .putString(KEY_DATA_NASCIMENTO, estudante.dataNascimento)
            .putString(KEY_CURSO, estudante.curso)
            .putString(KEY_INSTITUICAO, INSTITUICAO_INATEL)
            .putString(KEY_NIVEL_ENSINO, estudante.nivelEnsino)
            .putString(KEY_VALIDADE, estudante.validade)
            .apply()
    }

    fun carregarEstudante(): Estudante {
        return Estudante(
            nome = preferences.getString(
                KEY_NOME,
                "Lucas Silva Santos"
            ) ?: "Lucas Silva Santos",

            matricula = preferences.getString(
                KEY_MATRICULA,
                "202698765"
            ) ?: "202698765",

            dataNascimento = preferences.getString(
                KEY_DATA_NASCIMENTO,
                "15/08/2000"
            ) ?: "15/08/2000",

            curso = preferences.getString(
                KEY_CURSO,
                "Engenharia da Computação"
            ) ?: "Engenharia da Computação",

            instituicao = INSTITUICAO_INATEL,

            nivelEnsino = preferences.getString(
                KEY_NIVEL_ENSINO,
                "Graduação"
            ) ?: "Graduação",

            validade = preferences.getString(
                KEY_VALIDADE,
                calcularValidadeInicial()
            ) ?: calcularValidadeInicial()
        )
    }

    private fun calcularValidadeInicial(): String {
        val calendario = Calendar.getInstance()
        calendario.add(Calendar.YEAR, 1)

        val formato = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale("pt", "BR")
        )

        return formato.format(calendario.time)
    }

    companion object {
        private const val PREFS_NAME = "carteirinha_preferences"

        private const val KEY_NOME = "estudante_nome"
        private const val KEY_MATRICULA = "estudante_matricula"
        private const val KEY_DATA_NASCIMENTO = "estudante_data_nascimento"
        private const val KEY_CURSO = "estudante_curso"
        private const val KEY_INSTITUICAO = "estudante_instituicao"
        private const val KEY_NIVEL_ENSINO = "estudante_nivel_ensino"
        private const val KEY_VALIDADE = "estudante_validade"

        private const val INSTITUICAO_INATEL = "INATEL"
    }
}