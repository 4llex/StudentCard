package br.com.inatel.carteirinha.ui.validacao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.inatel.carteirinha.databinding.FragmentValidacaoQrBinding
import com.google.zxing.integration.android.IntentIntegrator

class ValidacaoQrFragment : Fragment() {

    private var _binding: FragmentValidacaoQrBinding? = null
    private val binding get() = _binding!!

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

        binding.buttonLerQrCode.setOnClickListener {
            iniciarScanner()
        }
    }

    private fun iniciarScanner() {

        IntentIntegrator.forSupportFragment(this)
            .setDesiredBarcodeFormats(
                IntentIntegrator.QR_CODE
            )
            .setPrompt(
                "Aponte a câmera para o QR Code da carteirinha"
            )
            .setBeepEnabled(true)
            .setOrientationLocked(false)
            .initiateScan()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: android.content.Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        val resultado =
            IntentIntegrator.parseActivityResult(
                requestCode,
                resultCode,
                data
            )

        if (resultado != null) {

            val conteudo = resultado.contents

            if (conteudo != null) {
                binding.buttonLerQrCode.text =
                    "QR Code lido"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}