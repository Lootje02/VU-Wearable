package nl.hva.vuwearable.ui.faq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.hva.vuwearable.databinding.FaqItemBinding
import nl.hva.vuwearable.models.Faq

class RvAdapter(
    private var languageList: List<Faq>
) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    // create an inner class with name ViewHolder
    // It takes a view argument, in which pass the generated class of faq_item.xml
    // ie SingleItemBinding and in the RecyclerView.ViewHolder(binding.root) pass it like this
    inner class ViewHolder(val binding: FaqItemBinding) : RecyclerView.ViewHolder(binding.root)

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FaqItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // bind the items with each item of the list languageList which than will be
    // shown in recycler view
    // to keep it simple we are not setting any image data to view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(languageList[position]){
                // set name of the language from the list
                binding.tvLangName.text = this.question
                // set description to the text
                // since this is inside "expandedView" its visibility will be gone initially
                // after click on the item we will make the visibility of the "expandedView" visible
                // which will also make the visibility of desc also visible
                binding.tvDescription.text = this.answer
                // check if boolean property "extend" is true or false
                // if it is true make the "extendedView" Visible
                binding.expandedView.visibility = if (this.expand) View.VISIBLE else View.GONE
                // on Click of the item take parent card view in our case
                // revert the boolean "expand"
                binding.cardLayout.setOnClickListener {
                    this.expand = !this.expand
                    notifyDataSetChanged()
                }
            }
        }
    }
    // return the size of languageList
    override fun getItemCount(): Int {
        return languageList.size
    }
}