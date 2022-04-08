package com.mabdigital.offers.presentaton.feature.details

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mabdigital.offers.databinding.DeatilsBottomSheetBinding
import com.mabdigital.offers.domain.feature.map.MapActionEvent
import com.mabdigital.offers.presentaton.feature.activity.OfferShareViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.NumberFormat

class OfferDetailsFragment : BottomSheetDialogFragment() {

    private val mViewModel by sharedViewModel<OfferShareViewModel>()

    private val args by navArgs<OfferDetailsFragmentArgs>()
    private val compositeDisposable = CompositeDisposable()
    private var _binding: DeatilsBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, 0);
        this.isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            dialog.setCanceledOnTouchOutside(false)
            dialog.behavior.isFitToContents = true
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            dialog.behavior.isDraggable = true
            dialog.behavior.isHideable = false

            val bottomSheet = dialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)

        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        if (_binding == null)
            _binding = DeatilsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupView()
        configView()
    }



    private fun configView() {
        view?.post {
            val dialogWindow: Window? = dialog!!.window
            dialogWindow?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            )
            view?.invalidate()
        }
    }

    private fun setupView() {
        val price =
            String.format("%s ريال", NumberFormat.getIntegerInstance().format(args.price.toLong()))
        binding.priceHolder.text = price
        binding.userLocation.setOnClickListener {
            mViewModel.onEvent(MapActionEvent.OnUserLocationClick)
        }
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

    override fun onStart() {
        super.onStart()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }
}