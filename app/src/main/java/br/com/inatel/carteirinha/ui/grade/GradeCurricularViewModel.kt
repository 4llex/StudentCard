package br.com.inatel.carteirinha.ui.grade

import androidx.lifecycle.ViewModel
import java.util.Locale

class GradeCurricularViewModel : ViewModel() {

    val disciplinas = listOf(
        Disciplina(
            codigo = "ECO001",
            nome = "Algoritmos e Estruturas de Dados",
            semestre = "1º Semestre",
            nota = 8.5,
            situacao = "Aprovado"
        ),
        Disciplina(
            codigo = "MAT001",
            nome = "Cálculo I",
            semestre = "1º Semestre",
            nota = 7.8,
            situacao = "Aprovado"
        ),
        Disciplina(
            codigo = "FIS001",
            nome = "Física I",
            semestre = "1º Semestre",
            nota = 6.9,
            situacao = "Aprovado"
        ),
        Disciplina(
            codigo = "ECO002",
            nome = "Programação Orientada a Objetos",
            semestre = "2º Semestre",
            nota = 9.2,
            situacao = "Aprovado"
        ),
        Disciplina(
            codigo = "MAT002",
            nome = "Cálculo II",
            semestre = "2º Semestre",
            nota = 7.1,
            situacao = "Aprovado"
        ),
        Disciplina(
            codigo = "ECO003",
            nome = "Sistemas Operacionais",
            semestre = "3º Semestre",
            nota = 8.7,
            situacao = "Aprovado"
        ),
        Disciplina(
            codigo = "ECO004",
            nome = "Arquitetura de Computadores",
            semestre = "3º Semestre",
            nota = 8.9,
            situacao = "Aprovado"
        ),
        Disciplina(
            codigo = "ECO005",
            nome = "Engenharia de Software",
            semestre = "4º Semestre",
            nota = null,
            situacao = "Cursando"
        )
    )

    val totalDisciplinas: Int
        get() = disciplinas.size

    val mediaGeral: Double?
        get() {
            val notas = disciplinas.mapNotNull { it.nota }

            return if (notas.isNotEmpty()) {
                notas.average()
            } else {
                null
            }
        }

    fun mediaGeralFormatada(): String {
        return mediaGeral?.let {
            String.format(Locale("pt", "BR"), "%.1f", it)
        } ?: "—"
    }
}