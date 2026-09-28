package br.com.inatel.carteirinha.ui.carteirinha

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class CarteirinhaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val estudantePreferences =
        EstudantePreferences(application)

    fun obterEstudante(): Estudante {
        return estudantePreferences.carregarEstudante()
    }

    fun obterQrCodeConteudo(): String {

        val estudante = obterEstudante()

        return "VALIDO" +
                "|NOME=${estudante.nome}" +
                "|RA=${estudante.matricula}" +
                "|CURSO=${estudante.curso}" +
                "|INSTITUICAO=${estudante.instituicao}"
    }
}