package com.mohamednader.shoponthego.Profile.View

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.mohamednader.shoponthego.Order.view.OrderActivity
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    lateinit var binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backArrowImg.setOnClickListener {
            OnBackPressed()
        }

        binding.moreText.setOnClickListener {
            val intent = Intent(requireContext() , OrderActivity::class.java)
            startActivity(intent)
        }

    }

    private fun OnBackPressed() {
        val navController = Navigation.findNavController(requireView())
        navController.popBackStack()
    }



}