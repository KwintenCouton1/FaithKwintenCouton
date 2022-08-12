package post


import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.android.faith.R

import com.example.android.faith.TextItemViewHolder
import com.example.android.faith.database.Post



class PostAdapter : RecyclerView.Adapter<TextItemViewHolder>(){
    var data =  listOf<Post>()
    set(value){
        field = value
        notifyDataSetChanged()
    }


    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]

        holder.textView.text = item.text
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(R.layout.text_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }
}


///**
// * [RecyclerView.Adapter] that can display a [PlaceholderItem].
// * TODO: Replace the implementation with code for your data type.
// */
//class PostAdapter(
//    private val values: List<PlaceholderItem>
//) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//        return ViewHolder(
//            FragmentPostBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = values[position]
//        holder.idView.text = item.id
//        holder.contentView.text = item.content
//    }
//
//    override fun getItemCount(): Int = values.size
//
//    inner class ViewHolder(binding: FragmentPostBinding) : RecyclerView.ViewHolder(binding.root) {
//        val idView: TextView = binding.itemNumber
//        val contentView: TextView = binding.content
//
//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
//    }
//
//}