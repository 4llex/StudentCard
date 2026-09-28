package br.com.inatel.carteirinha.ui.validacao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.inatel.carteirinha.R
import br.com.inatel.carteirinha.databinding.FragmentValidacaoQrBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory

class ValidacaoQrFragment : Fragment() {

    private var _binding: FragmentValidacaoQrBinding? = null
    private val binding get() = _binding!!

    private var qrDetectado = false

    private val barcodeCallback = object : BarcodeCallback {

        override fun barcodeResult(result: BarcodeResult?) {

            if (result == null || qrDetectado) {
                return
            }

            val conteudo = result.text ?: return

            qrDetectado = true

            requireActivity().runOnUiThread {
                processarQrCode(conteudo)
            }
        }

        override fun possibleResultPoints(
            resultPoints: MutableList<ResultPoint>?
        ) {
            // Não precisamos tratar os pontos neste momento.
        }
    }

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

        configurarBotaoLerNovamente()
        iniciarScanner()
    }

    private fun iniciarScanner() {

        qrDetectado = false

        binding.cardResultado.visibility = View.GONE

        binding.buttonLerNovamente.visibility = View.GONE

        binding.textStatusScanner.text =
            "Aponte a câmera para o QR Code..."

        binding.barcodeView.barcodeView.setDecoderFactory(
            DefaultDecoderFactory(
                listOf(BarcodeFormat.QR_CODE)
            )
        )

        binding.barcodeView.decodeContinuous(
            barcodeCallback
        )

        binding.barcodeView.resume()
    }

    private fun configurarBotaoLerNovamente() {

        binding.buttonLerNovamente.setOnClickListener {

            iniciarScanner()
        }
    }

    private fun processarQrCode(conteudo: String) {

        binding.barcodeView.pause()

        binding.cardResultado.visibility = View.VISIBLE

        val partes = conteudo.split("|")

        if (partes.size != 5) {

            mostrarQrInvalido(
                "O QR Code não possui um formato válido."
            )

            return
        }

        val status = partes[0]

        val nome = partes[1]
            .takeIf { it.startsWith("NOME=") }
            ?.removePrefix("NOME=")

        val matricula = partes[2]
            .takeIf { it.startsWith("RA=") }
            ?.removePrefix("RA=")

        val curso = partes[3]
            .takeIf { it.startsWith("CURSO=") }
            ?.removePrefix("CURSO=")

        val instituicao = partes[4]
            .takeIf { it.startsWith("INSTITUICAO=") }
            ?.removePrefix("INSTITUICAO=")

        if (
            status == "VALIDO" &&
            !nome.isNullOrBlank() &&
            !matricula.isNullOrBlank() &&
            !curso.isNullOrBlank() &&
            !instituicao.isNullOrBlank()
        ) {

            mostrarQrValido(
                nome,
                matricula,
                curso,
                instituicao
            )

        } else {

            mostrarQrInvalido(
                "Os dados da carteirinha não são válidos."
            )
        }
    }

    private fun mostrarQrValido(
        nome: String,
        matricula: String,
        curso: String,
        instituicao: String
    ) {

        binding.imageResultadoIcon.setImageResource(
            R.drawable.ic_check
        )

        binding.textResultado.text =
            "Carteirinha válida"

        binding.textResultado.setTextColor(
            requireContext().getColor(
                R.color.success_green
            )
        )

        binding.textStatusScanner.text =
            "QR Code validado com sucesso!"

        binding.textDadosEstudante.text =
            """
            Nome: $nome
            
            Matrícula: $matricula
            
            Curso: $curso
            
            Instituição: $instituicao
            """.trimIndent()

        binding.buttonLerNovamente.visibility =
            View.GONE
    }

    private fun mostrarQrInvalido(
        mensagem: String
    ) {

        binding.imageResultadoIcon.setImageResource(
            R.drawable.ic_close
        )

        binding.textResultado.text =
            "Carteirinha inválida"

        binding.textResultado.setTextColor(
            requireContext().getColor(
                R.color.error_red
            )
        )

        binding.textStatusScanner.text =
            "Não foi possível validar o QR Code."

        binding.textDadosEstudante.text =
            mensagem

        binding.buttonLerNovamente.visibility =
            View.VISIBLE
    }

    override fun onResume() {
        super.onResume()

        if (_binding != null && !qrDetectado) {
            binding.barcodeView.resume()
        }
    }

    override fun onPause() {

        if (_binding != null) {
            binding.barcodeView.pause()
        }

        super.onPause()
    }

    override fun onDestroyView() {

        if (_binding != null) {
            binding.barcodeView.pause()
        }

        super.onDestroyView()

        _binding = null
    }
}