package br.com.inatel.carteirinha.ui.grade

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.inatel.carteirinha.databinding.ItemDisciplinaBinding
import java.util.Locale

class DisciplinaAdapter(
    private var disciplinas: List<Disciplina>
) : RecyclerView.Adapter<DisciplinaAdapter.DisciplinaViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DisciplinaViewHolder {

        val binding = ItemDisciplinaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return DisciplinaViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DisciplinaViewHolder,
        position: Int
    ) {
        holder.bind(disciplinas[position])
    }

    override fun getItemCount(): Int {
        return disciplinas.size
    }

    fun atualizarLista(novaLista: List<Disciplina>) {
        disciplinas = novaLista
        notifyDataSetChanged()
    }

    class DisciplinaViewHolder(
        private val binding: ItemDisciplinaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(disciplina: Disciplina) {

            binding.textCodigo.text = disciplina.codigo
            binding.textNome.text = disciplina.nome
            binding.textSemestre.text = disciplina.semestre
            binding.textSituacao.text = disciplina.situacao

            binding.textNota.text = disciplina.nota?.let {
                String.format(Locale("pt", "BR"), "%.1f", it)
            } ?: "—"
        }
    }
}