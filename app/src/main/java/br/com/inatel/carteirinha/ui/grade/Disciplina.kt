package br.com.inatel.carteirinha.ui.grade

data class Disciplina(
    val codigo: String,
    val nome: String,
    val semestre: String,
    val nota: Double?,
    val situacao: String
)