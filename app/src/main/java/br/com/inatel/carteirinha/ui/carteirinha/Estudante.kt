package br.com.inatel.carteirinha.ui.carteirinha

data class Estudante(
    val nome: String,
    val matricula: String,
    val dataNascimento: String,
    val curso: String,
    val instituicao: String,
    val nivelEnsino: String,
    val validade: String
)