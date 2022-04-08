package com.mabdigital.offers.presentaton.feature.activity

import com.mabdigital.core.base.viewmodel.BaseViewModel
import com.mabdigital.offers.domain.feature.map.MapActionEvent
import com.mabdigital.offers.domain.feature.map.MapActionState

class OfferShareViewModel : BaseViewModel<MapActionState,MapActionEvent>() {

    override fun onEvent(uiEvent: MapActionEvent) {
        when(uiEvent) {
            is MapActionEvent.OnPointClicked -> state.postValue(MapActionState.MoveToPoint(uiEvent.point))
        }
    }
}