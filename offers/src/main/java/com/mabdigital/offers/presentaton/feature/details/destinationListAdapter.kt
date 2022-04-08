package com.mabdigital.offers.presentaton.feature.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.mabdigital.offers.R
import com.mabdigital.offers.databinding.PointListItemBinding
import com.mabdigital.offers.domain.model.map.PointDetails
import com.mabdigital.offers.domain.model.map.TerminalLocationTypeEnum
import io.reactivex.subjects.PublishSubject

class LocationListAdapter(
    private val dataList: List<PointDetails>
) : RecyclerView.Adapter<LocationViewHolder>() {

    val clickSubject = PublishSubject.create<PointDetails>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= LocationViewHolder(
        PointListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        clickSubject
    )

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) = holder.onBind(getItem(position))

    override fun getItemCount() = dataList.size
    private fun getItem(position: Int) = dataList[position]
}


class LocationViewHolder(
    private val binding: PointListItemBinding,
    private val clickSubject: PublishSubject<PointDetails>
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(point: PointDetails) {
        with(binding) {
            val icon = if (point.type == TerminalLocationTypeEnum.Source) {
                iconOfDest.destId.visibility = View.GONE
                ResourcesCompat.getDrawable(
                    itemView.resources,
                    R.drawable.ic_map_pin_source_green,
                    null
                )
            } else {
                iconOfDest.destId.visibility = View.VISIBLE
                iconOfDest.destId.text = absoluteAdapterPosition.toString()
                ResourcesCompat.getDrawable(itemView.resources, R.drawable.ic_map_pin, null)
            }
            iconOfDest.appCompatImageView.setImageDrawable(icon)
            terminalAddress.text = point.pointAddress
            root.setOnClickListener {
                clickSubject.onNext(point)
            }
        }
    }
}