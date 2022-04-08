package com.mabdigital.offers.presentaton.feature.details

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mabdigital.offers.R
import com.mabdigital.offers.databinding.DeatilsBottomSheetBinding
import com.mabdigital.offers.databinding.MapLoadViewBinding
import com.mabdigital.offers.domain.feature.map.MapActionEvent
import com.mabdigital.offers.presentaton.feature.activity.OfferShareViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.NumberFormat

class OfferDetailsFragment : BottomSheetDialogFragment() {

    private val mViewModel by viewModel<OfferShareViewModel>()

    private val args by navArgs<OfferDetailsFragmentArgs>()
    private val compositeDisposable = CompositeDisposable()
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
        setupView()
    }

    private fun setupView() {
        val price = String.format(getString(R.string.template_price),NumberFormat.getIntegerInstance().format(args.price.toLong()))
        binding.priceHolder.text = price
    }

    private fun setupList() {
        binding.pointList.adapter = LocationListAdapter(args.pointlist.toList()).apply {
            val disposable = clickSubject.subscribe {
                mViewModel.onEvent(MapActionEvent.OnPointClicked(it))
            }
            compositeDisposable.add(disposable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        _binding = null
    }

}