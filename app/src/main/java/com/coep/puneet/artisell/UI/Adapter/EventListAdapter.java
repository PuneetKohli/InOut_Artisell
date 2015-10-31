package com.coep.puneet.artisell.UI.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coep.puneet.artisell.ParseObjects.Events;
import com.coep.puneet.artisell.R;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.VHArtisan>
{
    private Context mContext;
    private ArrayList<Events> artisanList;

    public EventListAdapter(Context mContext, ArrayList<Events> artisanList)
    {
        this.mContext = mContext;
        this.artisanList = artisanList;

    }

    @Override
    public EventListAdapter.VHArtisan onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.event_list_item, parent, false);
        return new VHArtisan(view);
    }

    @Override
    public void onBindViewHolder(EventListAdapter.VHArtisan holder, int position)
    {
        Events events = artisanList.get(position);
        holder.name.setText(events.getEventName());
        holder.location.setText(events.getEventLocation());
        holder.startDate.setText(events.getStartDate().toLocaleString() + "-" + events.getEndDate().toLocaleString());
    }

    @Override
    public int getItemCount()
    {
        return artisanList.size();
    }

    public static class VHArtisan extends RecyclerView.ViewHolder
    {
        public final TextView name;
        public final TextView location;
        public final TextView startDate;

        public VHArtisan(View view)
        {
            super(view);
            name = (TextView) view.findViewById(R.id.eventName);
            location = (TextView) view.findViewById(R.id.eventLocation);
            startDate = (TextView) view.findViewById(R.id.startDate);
        }
    }
}
