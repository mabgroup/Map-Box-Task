package com.mabdigital.core.tools.extentions

import androidx.fragment.app.Fragment
import com.mabdigital.core.presentation.feature.CoreActivity

fun Fragment.navigateToOffers() {
    (requireActivity() as CoreActivity).navigateToOffers()
}