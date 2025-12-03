package ru.netology.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.myapp.FeedFragment.Companion.textArgs
import ru.netology.myapp.databinding.FragmentNewPostBinding
import ru.netology.myapp.util.AndroidUtils
import kotlin.getValue

class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        arguments?.textArgs?.let { text ->
            binding.edit.setText(text)
        }


        binding.save.setOnClickListener {
            viewModel.save(binding.edit.text.toString())
            AndroidUtils.hideKeyboard(requireView())

        }
        viewModel.postCreated.observe(viewLifecycleOwner){
            findNavController().navigateUp()
            viewModel.load()
        }

        return binding.root
    }
}