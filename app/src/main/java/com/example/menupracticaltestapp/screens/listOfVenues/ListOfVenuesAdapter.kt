package com.example.menupracticaltestapp.screens.listOfVenues

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.menupracticaltestapp.R
import com.example.menupracticaltestapp.data.OrderTypesEnum
import com.example.menupracticaltestapp.data.Venue
import com.example.menupracticaltestapp.data.VenuesListResponse
import com.example.menupracticaltestapp.databinding.ListItemVenueClosedBinding
import com.example.menupracticaltestapp.databinding.ListItemVenueOpenBinding
import java.util.Calendar
import kotlin.math.roundToInt

class ListOfVenuesAdapter(private val onClick: VenueClickedListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    lateinit var bindingClosed: ListItemVenueClosedBinding
    lateinit var bindingOpen: ListItemVenueOpenBinding

    var venuesList: MutableList<VenuesListResponse> = mutableListOf()

    enum class ViewTypes(val value: Int){
        ITEM_OPEN(1),
        ITEM_CLOSED(2)
    }

    fun updateList(list: List<VenuesListResponse>) {
        venuesList.clear()
        venuesList.addAll(list)
        notifyItemRangeChanged(0, list.size)
    }

    private fun getItem(index: Int): VenuesListResponse {
        return venuesList[index]
    }

    override fun getItemViewType(position: Int): Int {
        return when (!getItem(position).venue!!.willOpen || (!getItem(position).venue!!.isOpen && !canOrderInAdvance(getItem(position).venue!!))) {
            true -> ViewTypes.ITEM_CLOSED.value
            false -> ViewTypes.ITEM_OPEN.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ViewTypes.ITEM_OPEN.value -> {
                bindingOpen = ListItemVenueOpenBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return VenueOpenViewHolder(bindingOpen.root)
            }
            ViewTypes.ITEM_CLOSED.value -> {
                bindingClosed = ListItemVenueClosedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return VenueClosedViewHolder(bindingClosed.root)
            }
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = venuesList[position]
        when (holder) {
            is VenueOpenViewHolder -> holder.bind(item, onClick)
            is VenueClosedViewHolder -> holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return venuesList.size
    }

    inner class VenueClosedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: VenuesListResponse) {
            bindingClosed.venueName.text = item.venue?.name
            bindingClosed.venueAddress.text = "${item.venue!!.address}, ${item.venue!!.city}"
            bindingClosed.venueDistance.text = setUpDistance(item)

            if (!item.venue!!.willOpen) {
                bindingClosed.venueWorkingHours.text = itemView.context.resources.getText(R.string.temporarily_unavailable)
            }
            else if (!item.venue!!.isOpen) {
                bindingClosed.venueWorkingHours.text = itemView.context.resources.getText(R.string.closed)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpDistance(item: VenuesListResponse): String {
        return if (item.distance!! > 1000) {
            val distanceKilometers = (item.distance!!/1000 * 10.0).roundToInt() / 10.0
            "$distanceKilometers km"
        } else {
            "${item.distance!!.roundToInt()} m"
        }
    }

    inner class VenueOpenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(
            item: VenuesListResponse,
            onClick: VenueClickedListener
        ) {
            bindingOpen.venueName.text = item.venue?.name
            bindingOpen.venueAddress.text = "${item.venue!!.address}, ${item.venue!!.city}"
            bindingOpen.venueDistance.text = setUpDistance(item)

            if (!item.venue!!.isOpen && canOrderInAdvance(item.venue!!)) {
                bindingOpen.venueWorkingHours.text = "${whenIsItOpen(item.venue!!, itemView.context)} ${itemView.context.resources.getString(R.string.order_in_advance)}"
            }
            else {
                bindingOpen.venueWorkingHours.text = whenIsItOpen(item.venue!!, itemView.context)
            }

            for (orderType in item.venue!!.orderTypes) {
                if (orderType.referenceType == OrderTypesEnum.PREORDER.type) {
                    bindingOpen.venueAvailabilityPreorder.visibility = View.VISIBLE
                }
                if (orderType.referenceType == OrderTypesEnum.TAKEOUT.type) {
                    bindingOpen.venueAvailabilityCurbed.visibility = View.VISIBLE
                }
                if (orderType.referenceType == OrderTypesEnum.DELIVERY.type) {
                    bindingOpen.venueAvailabilityDelivery.visibility = View.VISIBLE
                }
            }

            itemView.setOnClickListener {
                onClick.venueClicked(item)
            }
        }
    }

    private fun canOrderInAdvance(venue: Venue): Boolean {
        return if (venue.daysInAdvance == 0) {
            false
        } else {
            val daysOpened = mutableListOf<Int>()
            for (servingTime in venue.servingTimes) {
                daysOpened.addAll(servingTime.days)
            }
            val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1 + venue.daysInAdvance!!
            daysOpened.contains(day)
        }
    }

    //Since there is no business logic explaining how to show the different scenarios that show when the venue is opened this is just a suggestion
    private fun whenIsItOpen(venue: Venue, context: Context): String {
        if (venue.servingTimes.isEmpty()) return ""

        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1

        var isOpenedToday = false
        var timeOpened = ""
        var timeClosed = ""

        var nextDay = -1
        var nextDayOpens = ""

        for (servingTime in venue.servingTimes) {
            if (servingTime.days.contains(today)) {
                isOpenedToday = true
                timeOpened = servingTime.timeFrom!!
                timeClosed = servingTime.timeTo!!
            }
            else {
                for(day in servingTime.days) {
                    if (day - today > 0) {
                        if (nextDay < 0) {
                            nextDay = day
                            nextDayOpens = servingTime.timeFrom!!
                        }
                        else if(nextDay > day){
                            nextDay = day
                            nextDayOpens = servingTime.timeFrom!!
                        }
                    }
                }
            }
        }
        return if (isOpenedToday) {
            "${context.resources.getString(R.string.today)} $timeOpened - $timeClosed"
        } else {
            "${context.resources.getString(R.string.opens)} ${
                when(nextDay) {
                    0 -> context.resources.getString(R.string.sunday)
                    1 -> context.resources.getString(R.string.monday)
                    2 -> context.resources.getString(R.string.tuesday)
                    3 -> context.resources.getString(R.string.wednesday)
                    4 -> context.resources.getString(R.string.thursday)
                    5 -> context.resources.getString(R.string.friday)
                    else -> context.resources.getString(R.string.saturday)                
                }
            } ${context.resources.getString(R.string.at)} $nextDayOpens"
        }
    }
}

