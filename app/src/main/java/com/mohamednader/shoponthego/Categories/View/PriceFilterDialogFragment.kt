package com.mohamednader.shoponthego.Categories.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mohamednader.shoponthego.databinding.SliderItemBinding

class PriceFilterDialogFragment : BottomSheetDialogFragment() {

    interface PriceFilterListener {
        fun onPriceFiltered(priceFrom: Double, priceTo: Double)
    }

    companion object {
        fun newInstance(): PriceFilterDialogFragment {
            return PriceFilterDialogFragment()
        }
    }

    private var _binding: SliderItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = SliderItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sbPriceFrom.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val priceFrom = progress.toDouble()
                binding.etPriceFrom.setText(priceFrom.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.sbPriceTo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val priceTo = progress.toDouble()
                binding.etPriceTo.setText(priceTo.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.btnApply.setOnClickListener {
            val priceFrom = binding.etPriceFrom.text.toString().toDoubleOrNull() ?: 0.0
            val priceTo = binding.etPriceTo.text.toString().toDoubleOrNull() ?: Double.MAX_VALUE

            val listener = targetFragment
            if (listener is PriceFilterListener) {
                listener.onPriceFiltered(priceFrom, priceTo)
            }

            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
