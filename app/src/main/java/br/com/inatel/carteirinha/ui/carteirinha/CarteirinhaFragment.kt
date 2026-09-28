package br.com.inatel.carteirinha.ui.carteirinha

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.inatel.carteirinha.databinding.FragmentCarteirinhaBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File

class CarteirinhaFragment : Fragment() {

    private var _binding: FragmentCarteirinhaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CarteirinhaViewModel by viewModels()

    private val selecionarFoto =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let { salvarFoto(it) }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCarteirinhaBinding.inflate(
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

        configurarFoto()
        carregarFoto()
        preencherDados()
    }

    override fun onResume() {
        super.onResume()

        if (_binding != null) {
            preencherDados()
        }
    }

    private fun configurarFoto() {

        binding.imageFoto.setOnClickListener {
            selecionarFoto.launch("image/*")
        }
    }

    private fun salvarFoto(uri: Uri) {

        try {

            val nomeArquivo = "foto_estudante.jpg"

            requireContext()
                .contentResolver
                .openInputStream(uri)
                ?.use { input ->

                    requireContext()
                        .openFileOutput(
                            nomeArquivo,
                            android.content.Context.MODE_PRIVATE
                        )
                        .use { output ->

                            input.copyTo(output)
                        }
                }

            salvarReferenciaFoto(nomeArquivo)

            val arquivo = File(
                requireContext().filesDir,
                nomeArquivo
            )

            val bitmapOriginal =
                BitmapFactory.decodeFile(
                    arquivo.absolutePath
                )

            if (bitmapOriginal != null) {

                val bitmapCorrigido =
                    corrigirOrientacao(
                        bitmapOriginal,
                        arquivo.absolutePath
                    )

                binding.imageFoto.setImageBitmap(
                    bitmapCorrigido
                )
            }

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    private fun salvarReferenciaFoto(nomeArquivo: String) {

        requireContext()
            .getSharedPreferences(
                "carteirinha_preferences",
                android.content.Context.MODE_PRIVATE
            )
            .edit()
            .putString(
                "foto_estudante",
                nomeArquivo
            )
            .apply()
    }

    private fun carregarFoto() {

        val nomeArquivo =
            requireContext()
                .getSharedPreferences(
                    "carteirinha_preferences",
                    android.content.Context.MODE_PRIVATE
                )
                .getString(
                    "foto_estudante",
                    null
                )

        if (nomeArquivo != null) {

            val arquivo = File(
                requireContext().filesDir,
                nomeArquivo
            )

            if (arquivo.exists()) {

                val bitmapOriginal =
                    BitmapFactory.decodeFile(
                        arquivo.absolutePath
                    )

                if (bitmapOriginal != null) {

                    val bitmapCorrigido =
                        corrigirOrientacao(
                            bitmapOriginal,
                            arquivo.absolutePath
                        )

                    binding.imageFoto.setImageBitmap(
                        bitmapCorrigido
                    )
                }
            }
        }
    }

    private fun corrigirOrientacao(
        bitmap: Bitmap,
        caminhoArquivo: String
    ): Bitmap {

        val exif = ExifInterface(caminhoArquivo)

        val orientacao =
            exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

        val matrix = Matrix()

        when (orientacao) {

            ExifInterface.ORIENTATION_ROTATE_90 ->
                matrix.postRotate(90f)

            ExifInterface.ORIENTATION_ROTATE_180 ->
                matrix.postRotate(180f)

            ExifInterface.ORIENTATION_ROTATE_270 ->
                matrix.postRotate(270f)

            ExifInterface.ORIENTATION_FLIP_HORIZONTAL ->
                matrix.preScale(-1f, 1f)

            ExifInterface.ORIENTATION_FLIP_VERTICAL ->
                matrix.preScale(1f, -1f)
        }

        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }

    private fun preencherDados() {

        val estudante = viewModel.obterEstudante()

        binding.textNome.text =
            estudante.nome

        binding.textMatricula.text =
            "Matrícula: ${estudante.matricula}"

        binding.textCurso.text =
            estudante.curso

        binding.textNivel.text =
            estudante.nivelEnsino

        binding.textDataNascimento.text =
            "Data de nascimento: ${estudante.dataNascimento}"

        binding.textInstituicao.text =
            "Instituição: ${estudante.instituicao}"

        binding.textValidade.text =
            "Validade: ${estudante.validade}"

        val qrCode =
            gerarQrCode(
                viewModel.obterQrCodeConteudo()
            )

        binding.imageQrCode.setImageBitmap(qrCode)
    }

    private fun gerarQrCode(
        conteudo: String
    ): Bitmap {

        val barcodeEncoder =
            BarcodeEncoder()

        return barcodeEncoder.encodeBitmap(
            conteudo,
            BarcodeFormat.QR_CODE,
            600,
            600
        )
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}