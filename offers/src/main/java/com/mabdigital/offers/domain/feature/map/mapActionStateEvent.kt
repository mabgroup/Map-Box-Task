package com.mabdigital.offers.domain.feature.map

import com.mabdigital.core.base.viewmodel.BaseState
import com.mabdigital.core.base.viewmodel.BaseViewModelEvent
import com.mabdigital.offers.domain.model.map.PointDetails

sealed class MapActionState() : BaseState {
    data class MoveToPoint(val point:PointDetails) : MapActionState()
}

sealed class MapActionEvent() : BaseViewModelEvent {
    data class OnPointClicked(val point: PointDetails) : MapActionEvent()
}