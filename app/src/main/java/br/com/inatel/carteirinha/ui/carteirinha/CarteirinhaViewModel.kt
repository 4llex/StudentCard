package br.com.inatel.carteirinha.ui.carteirinha

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CarteirinhaViewModel : ViewModel() {

    val estudante: Estudante = Estudante(
        nome = "Alex Rafael Silva Rosa",
        matricula = "202698765",
        dataNascimento = "18/10/1985",
        curso = "Engenharia de Computação",
        instituicao = "INATEL",
        nivelEnsino = "Graduação",
        validade = calcularValidade()
    )

    val qrCodeConteudo: String
        get() =
            "VALIDO" +
                    "|NOME=${estudante.nome}" +
                    "|RA=${estudante.matricula}" +
                    "|CURSO=${estudante.curso}" +
                    "|INSTITUICAO=${estudante.instituicao}"

    private fun calcularValidade(): String {
        val calendario = Calendar.getInstance()

        calendario.add(Calendar.YEAR, 1)

        calendario.set(Calendar.MONTH, Calendar.MARCH)
        calendario.set(Calendar.DAY_OF_MONTH, 31)

        val formato = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale("pt", "BR")
        )

        return formato.format(calendario.time)
    }
}