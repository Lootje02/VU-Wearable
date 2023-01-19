package nl.hva.vuwearable.ui.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import nl.hva.vuwearable.databinding.FragmentFaqBinding
import nl.hva.vuwearable.models.Faq
import nl.hva.vuwearable.ui.login.LoginViewModel

/**
 * This fragment is used to create the list of different FAQ questions
 * @author Lorenzo Bindemann
 */
class FaqFragment : Fragment() {

    private var _binding: FragmentFaqBinding? = null
    private val binding get() = _binding!!


    // get reference to the adapter class
    private lateinit var FaqList: List<Faq>
    private lateinit var rvAdapter: RvAdapter

    private val loginViewModel: LoginViewModel by activityViewModels()

    private val faqViewModel: FaqViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentFaqBinding.inflate(layoutInflater)

        // define layout manager for the Recycler view
        binding.rvList.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        loginViewModel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            faqViewModel.getFaqQuestionsAndAnswers(isLoggedIn)
            FaqList = faqViewModel.faqList
            rvAdapter = RvAdapter(FaqList)
            // attach adapter to the recycler view
            binding.rvList.adapter = rvAdapter
            rvAdapter.notifyDataSetChanged()
        }

        return binding.root

    }

    // on destroy of view make the binding reference to null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}