package com.example.kotlin.lesson6

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.kotlin.databinding.FragmentThreadsBinding
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
        val myThreads = MyThreads()
        myThreads.start()
        with(binding) {
            val time = threadsEditText.text.toString().toLong()
            var counter = 0
            serviceButton.setOnClickListener {
                Thread {
                    sleep(time * 1000L)
                    requireActivity().runOnUiThread {
                        threadsTextView.text = "$time секунд"
                        createTextView("${Thread.currentThread().name} ${++counter}")
                    }
                }.start()
            }
            serviceButton2.setOnClickListener {
                myThreads.mHandler?.post {

                    sleep(time * 1000L)
                    Handler(Looper.getMainLooper()).post {
                        threadsTextView2.text = "${time} секунд"  // то же самое
                        createTextView("${Thread.currentThread().name} ${++counter}")
                    }
                }
            }
        }
    }

    private fun createTextView(name: String) {
        binding.mainContainer.addView(TextView(requireContext()).apply {
            text = Thread.currentThread().name
            textSize = 14f
        })
    }

    class MyThreads : Thread() {
        var mHandler: Handler? = null
        override fun run() {
            Looper.prepare() //подготовка
            mHandler = Handler(Looper.myLooper()!!)
            Looper.loop() //зациклил

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ThreadsFragment()
    }

}
