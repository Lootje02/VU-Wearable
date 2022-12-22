package nl.hva.vuwearable.ui.faq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.hva.vuwearable.databinding.FaqItemBinding
import nl.hva.vuwearable.models.Faq

/**
 * This adapter is created to show a Faq xml item for every item in the list
 * The expandedView is used to show the answer when you click on the item
 * @author Lorenzo Bindemann
 */
class RvAdapter(
    private var questionList: List<Faq>
) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    /** create an inner class with name ViewHolder
     * It takes a view argument, in which pass the generated class of faq_item.xml
     * ie SingleItemBinding and in the RecyclerView.ViewHolder(binding.root) pass it like this */
    inner class ViewHolder(val binding: FaqItemBinding) : RecyclerView.ViewHolder(binding.root)

    /** inside the onCreateViewHolder inflate the view of SingleItemBinding
     * and return new ViewHolder object containing this layout */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FaqItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    /** bind the items with each item of the list questionList which than will be
     * shown in recycler view
     * to keep it simple we are not setting any image data to view */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(questionList[position]){
                binding.tvLangName.text = this.question
                binding.tvDescription.text = this.answer
                // check if boolean property "extend" is true or false
                // if it is true make the "extendedView" Visible
                binding.expandedView.visibility = if (this.expand) {
                    binding.expandedView.animate()
                        .translationY(1.0f)
                        .alpha(1.0f).duration = 300
                    View.VISIBLE
                } else {
                    binding.expandedView.animate()
                        .translationY(0.0f)
                        .alpha(0.0f).duration = 300
                    View.GONE
                }
                // on Click of the item take parent card view in our case
                // revert the boolean "expand"
                binding.cardLayout.setOnClickListener {
                    this.expand = !this.expand
                    notifyDataSetChanged()
                }
            }
        }
    }
    // return the size of faqList
    override fun getItemCount(): Int {
        return questionList.size
    }
}