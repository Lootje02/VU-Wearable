package nl.hva.vuwearable.ui.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import nl.hva.vuwearable.databinding.FragmentFaqBinding
import nl.hva.vuwearable.models.Faq

class FaqFragment : Fragment() {

    private var _binding: FragmentFaqBinding? = null
    private val binding get() = _binding!!


    // get reference to the adapter class
    private var FaqList = ArrayList<Faq>()
    private lateinit var rvAdapter: RvAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentFaqBinding.inflate(layoutInflater)

        // define layout manager for the Recycler view
        binding.rvList.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        // attach adapter to the recycler view
        rvAdapter = RvAdapter(FaqList)
        binding.rvList.adapter = rvAdapter

        // create new objects
        // add some row data
        val Faq1 = Faq(
            "Java",
            "Java is an Object Oriented Programming Faq." +
                    " Java is used in all kind of applications like Mobile Applications (Android is Java based), " +
                    "desktop applications, web applications, client server applications, enterprise applications and many more. ",
            false
        )
        val Faq2 = Faq(
            "Kotlin",
            "Kotlin is a statically typed, general-purpose programming Faq" +
                    " developed by JetBrains, that has built world-class IDEs like IntelliJ IDEA, PhpStorm, Appcode, etc.",
            false
        )
        val Faq3 = Faq(
            "Python",
            "Python is a high-level, general-purpose and a very popular programming Faq." +
                    " Python programming Faq (latest Python 3) is being used in web development, Machine Learning applications, " +
                    "along with all cutting edge technology in Software Industry.",
            false
        )
        val Faq4 = Faq(
            "CPP",
            "C++ is a general purpose programming Faq and widely used now a days for " +
                    "competitive programming. It has imperative, object-oriented and generic programming features. ",
            false
        )

        // add items to list
        FaqList.add(Faq1)
        FaqList.add(Faq2)
        FaqList.add(Faq3)
        FaqList.add(Faq4)

        rvAdapter.notifyDataSetChanged()

        return binding.root

    }

    // on destroy of view make the binding reference to null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}