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

        binding.textStatusScanner.text =
            "QR Code lido com sucesso!"

        binding.cardResultado.visibility =
            View.VISIBLE

        binding.textResultado.text =
            "QR Code detectado"

        binding.textDadosEstudante.text =
            conteudo
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