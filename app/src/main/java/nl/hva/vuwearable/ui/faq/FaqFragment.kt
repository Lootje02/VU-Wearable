package nl.hva.vuwearable.ui.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import nl.hva.vuwearable.MainActivity
import nl.hva.vuwearable.databinding.FragmentFaqBinding
import nl.hva.vuwearable.models.Faq

class FaqFragment : Fragment() {

    private var _binding: FragmentFaqBinding? = null
    private val binding get() = _binding!!


    // get reference to the adapter class
    private lateinit var FaqList : List<Faq>
    private lateinit var rvAdapter: RvAdapter

    private val faqViewModel : FaqViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentFaqBinding.inflate(layoutInflater)

        // define layout manager for the Recycler view
        binding.rvList.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        // attach adapter to the recycler view
        val userIsLoggedIn = (activity as MainActivity).loginViewModel.checkIfUserIsLoggedIn()
        faqViewModel.getFaqQuestionsAndAnswers(userIsLoggedIn)
        FaqList = faqViewModel.faqList
        rvAdapter = RvAdapter(FaqList)
        binding.rvList.adapter = rvAdapter

        rvAdapter.notifyDataSetChanged()


        return binding.root

    }

    // on destroy of view make the binding reference to null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}