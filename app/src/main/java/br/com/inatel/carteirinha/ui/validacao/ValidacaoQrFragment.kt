package br.com.inatel.carteirinha.ui.validacao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.inatel.carteirinha.databinding.FragmentValidacaoQrBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult

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

        iniciarScanner()
    }

    private fun iniciarScanner() {

        qrDetectado = false

        binding.textStatusScanner.text =
            "Aponte a câmera para o QR Code..."

        binding.barcodeView.barcodeView.setDecoderFactory(
            com.journeyapps.barcodescanner.DefaultDecoderFactory(
                listOf(BarcodeFormat.QR_CODE)
            )
        )

        binding.barcodeView.decodeContinuous(
            barcodeCallback
        )

        binding.barcodeView.resume()
    }

    private fun processarQrCode(conteudo: String) {

        binding.barcodeView.pause()

        binding.cardResultado.visibility = View.VISIBLE

        val partes = conteudo.split("|")

        if (partes.size != 5) {
            mostrarQrInvalido()
            return
        }

        val status = partes[0]

        val nome = partes[1]
            .removePrefix("NOME=")

        val matricula = partes[2]
            .removePrefix("RA=")

        val curso = partes[3]
            .removePrefix("CURSO=")

        val instituicao = partes[4]
            .removePrefix("INSTITUICAO=")

        if (
            status == "VALIDO" &&
            nome.isNotBlank() &&
            matricula.isNotBlank() &&
            curso.isNotBlank() &&
            instituicao.isNotBlank()
        ) {

            mostrarQrValido(
                nome,
                matricula,
                curso,
                instituicao
            )

        } else {

            mostrarQrInvalido()
        }
    }

    private fun mostrarQrValido(
        nome: String,
        matricula: String,
        curso: String,
        instituicao: String
    ) {

        binding.textStatusScanner.text =
            "QR Code validado com sucesso!"

        binding.textResultado.text =
            "Carteirinha válida"

        binding.textDadosEstudante.text =
            """
        Nome: $nome
        
        Matrícula: $matricula
        
        Curso: $curso
        
        Instituição: $instituicao
        """.trimIndent()
    }

    private fun mostrarQrInvalido() {

        binding.textStatusScanner.text =
            "Não foi possível validar o QR Code."

        binding.textResultado.text =
            "Carteirinha inválida"

        binding.textDadosEstudante.text =
            "O QR Code não possui um formato válido."
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
        binding.barcodeView.pause()

        super.onDestroyView()
        _binding = null
    }
}