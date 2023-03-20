package com.example.kotlin.view.weatherlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlin.databinding.FragmentThreadsBinding
import kotlinx.android.synthetic.main.fragment_threads.*
import java.lang.Thread.sleep


class ThreadsFragment : Fragment() {

    private var _binding: FragmentThreadsBinding? = null //убрали утечку памяти
    private val binding: FragmentThreadsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThreadsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            serviceButton.setOnClickListener {
                Thread{
                    val time = threadsEditText.text.toString().toLong()
                    sleep(time*1000L)
                    threadsTextView.text = "Плотно поработали ${time} секунд"
                } .start()


            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ThreadsFragment()
    }


}