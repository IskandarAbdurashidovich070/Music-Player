package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentAnimationBinding

class AnimationFragment : Fragment() {
    private lateinit var binding: FragmentAnimationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnimationBinding.inflate(layoutInflater)


        Handler(Looper.myLooper()!!).postDelayed({
            findNavController().navigate(R.id.home_Fragment)
        },5000)


        return binding.root
    }

}