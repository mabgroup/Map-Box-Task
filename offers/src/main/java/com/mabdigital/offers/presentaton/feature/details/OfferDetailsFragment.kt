package com.mabdigital.offers.presentaton.feature.details

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mabdigital.offers.databinding.DeatilsBottomSheetBinding
import com.mabdigital.offers.databinding.MapLoadViewBinding
import com.mabdigital.offers.presentaton.feature.activity.OfferShareViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OfferDetailsFragment : BottomSheetDialogFragment() {

    private val mViewModel by viewModel<OfferShareViewModel>()

    private var _binding: DeatilsBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        if(_binding==null)
            _binding = DeatilsBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
    }

    private fun setupList() {

    }

}