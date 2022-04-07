package com.mabdigital.core.presentation.feature

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mabdigital.core.tools.extentions.navigateToOffers

class CoreFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToOffers()
    }
}