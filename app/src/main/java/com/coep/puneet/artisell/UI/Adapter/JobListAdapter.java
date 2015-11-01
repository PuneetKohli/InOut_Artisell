package com.coep.puneet.artisell.UI.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coep.puneet.artisell.ParseObjects.Request;
import com.coep.puneet.artisell.R;
import com.coep.puneet.artisell.UI.Activity.JobActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.VHArtisan>
{
    private Context mContext;
    private ArrayList<Request> jobsList;

    public JobListAdapter(Context mContext, ArrayList<Request> artisanList)
    {
        this.mContext = mContext;
        this.jobsList = artisanList;

    }

    @Override
    public JobListAdapter.VHArtisan onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.request_list_item, parent, false);
        return new VHArtisan(view);
    }

    @Override
    public void onBindViewHolder(JobListAdapter.VHArtisan holder, final int position)
    {
        Request request = jobsList.get(position);
        holder.description.setText(request.getRequestDescription());

        String s = "" + DateUtils.getRelativeTimeSpanString(request.getRequestDeliverBy().getTime(), System.currentTimeMillis(), 0);
        s = s.replace("minute", "min");


        holder.deliveredBy.setText(s + "");
        holder.budget.setText("Rs. " + request.getRequestBudget());

        if (request.getRequestPhoto() != null)
            Glide.with(mContext).load(request.getRequestPhoto().getUrl()).asBitmap().centerCrop().into(holder.imageView);
        else
        {
            if(request.getRequestCategory().getCategory_name().toLowerCase().trim().startsWith("men"))
            {
                holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_mens_clothing));
            }
            else if (request.getRequestCategory().getCategory_name().toLowerCase().trim().startsWith("wom"))
            {
                holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_womens_clothing));
            }
            else if (request.getRequestCategory().getCategory_name().toLowerCase().trim().startsWith("bag"))
            {
                holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_bags));
            }
            else if (request.getRequestCategory().getCategory_name().toLowerCase().trim().startsWith("jew"))
            {
                holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_jewelry));
            }
            else if (request.getRequestCategory().getCategory_name().toLowerCase().trim().startsWith("foot"))
            {
                holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_footwear));
            }
            else if (request.getRequestCategory().getCategory_name().toLowerCase().trim().startsWith("wall"))
            {
                holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_clocks));
            }
            else if (request.getRequestCategory().getCategory_name().toLowerCase().trim().startsWith("pai"))
            {
                holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_paintings));
            }
            else if (request.getRequestCategory().getCategory_name().toLowerCase().trim().startsWith("pot"))
            {
                holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_pots));
            }
        }

        holder.rejectView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                jobsList.remove(position);
                notifyItemRemoved(position);
            }
        });
        holder.acceptView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*jobsList.remove(position);
                notifyItemRemoved(position);*/
                //SendUnicodeSms.sendSms("7507118432", "Your request has been accepted. Please check the app for the status");
                ((JobActivity) mContext).manager.jobsList.get(position).setRequestStatus(1);
                ((JobActivity) mContext).manager.jobsList.get(position).saveEventually();
                jobsList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(mContext, "You have succesfully accepted the job", Toast.LENGTH_SHORT).show();
            }
        });
    }

    

    @Override
    public int getItemCount()
    {
        return jobsList.size();
    }

    public static class VHArtisan extends RecyclerView.ViewHolder
    {
        public final CircleImageView imageView;
        public final TextView description;
        public final TextView budget;
        public final TextView deliveredBy;
        public final View rejectView;
        public final View acceptView;

        public VHArtisan(View view)
        {
            super(view);
            description = (TextView) view.findViewById(R.id.eventName);
            budget = (TextView) view.findViewById(R.id.eventLocation);
            deliveredBy = (TextView) view.findViewById(R.id.startDate);
            imageView = (CircleImageView) view.findViewById(R.id.request_image);
            rejectView = view.findViewById(R.id.button_reject_request);
            acceptView = view.findViewById(R.id.button_accept_request);
        }
    }
}

