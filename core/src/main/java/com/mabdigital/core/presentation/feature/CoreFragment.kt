package com.mabdigital.core.presentation.feature

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mabdigital.core.tools.extentions.navigateToOffers

class CoreFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateToOffers()
    }

}