import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tugas13.Film
import com.example.tugas13.databinding.FilmLayoutBinding

typealias OnClickMember = (Film) -> Unit

class FilmAdapter(private val onClickMember: OnClickMember) :
    ListAdapter<Film, FilmAdapter.ItemMemberViewHolder>(FilmDiffCallback()) {

    // Define the long click listener
    private var onItemClick: ((Film) -> Unit)? = null

    // Function to set the long click listener
    fun setOnItemClickListener(listener: (Film) -> Unit) {
        onItemClick = listener
    }

    inner class ItemMemberViewHolder(private val binding: FilmLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Film) {
            with(binding) {
                txtJudul.text = data.title
                txtRating.text = data.rating
                txtTag.text = data.tag

                Glide.with(itemView.context)
                    .load(data.url)
                    .into(imgCover)

                itemView.setOnClickListener {
                    onItemClick?.invoke(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMemberViewHolder {
        val binding = FilmLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemMemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class FilmDiffCallback : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem == newItem
        }
    }
}