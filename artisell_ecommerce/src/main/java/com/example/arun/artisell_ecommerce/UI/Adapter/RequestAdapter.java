package com.example.arun.artisell_ecommerce.UI.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arun.artisell_ecommerce.ParseObjects.Request;
import com.example.arun.artisell_ecommerce.R;
import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arun on 01-Nov-15.
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.VHCategory>
{
    private final Context mContext;
    private List<Request> mData;

    public RequestAdapter(Context context, ArrayList<Request> data)
    {
        mContext = context;
        if (data != null)
        {
            mData = new ArrayList<Request>(data);
        }
        else
        {
            mData = new ArrayList<Request>();
        }
    }

    public void add(Request s, int position)
    {
        position = position == -1 ? getItemCount() : position;
        mData.add(position, s);
        notifyItemInserted(position);
    }

    public void addAll(ArrayList<Request> s)
    {
        mData.addAll(s);
        notifyDataSetChanged();
    }

    public void remove(int position)
    {
        if (position < getItemCount())
        {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public VHCategory onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.request_item, parent, false);
        return new VHCategory(view);
    }

    @Override
    public void onBindViewHolder(VHCategory holder, final int position)
    {
        holder.description.setText(mData.get(position).getRequestDescription());
        holder.budget.setText("Rs " + mData.get(position).getRequestBudget());
        if(mData.get(position).getRequestPhoto() == null) {

            if(mData.get(position).getRequestCategory().getCategory_name().toLowerCase().trim().startsWith("men"))
            {
                holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_mens_clothing));
            }
            else if (mData.get(position).getRequestCategory().getCategory_name().toLowerCase().trim().startsWith("wom"))
            {
                holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_womens_clothing));
            }
            else if (mData.get(position).getRequestCategory().getCategory_name().toLowerCase().trim().startsWith("bag"))
            {
                holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_bags));
            }
            else if (mData.get(position).getRequestCategory().getCategory_name().toLowerCase().trim().startsWith("jew"))
            {
                holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_womens_clothing));
            }
            else if (mData.get(position).getRequestCategory().getCategory_name().toLowerCase().trim().startsWith("foot"))
            {
                holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_footwear));
            }
            else if (mData.get(position).getRequestCategory().getCategory_name().toLowerCase().trim().startsWith("clo"))
            {
                holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_clocks));
            }
        }
        else {
            ParseFile pf = mData.get(position).getRequestPhoto();
            Glide.with(mContext).load(pf.getUrl()).asBitmap().centerCrop().placeholder(R.drawable.background_material).into(holder.image);
        }

        /*holder.title.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(mContext, "Position =" + position, Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }


    public static class VHCategory extends RecyclerView.ViewHolder
    {
        public final TextView description;
        public final TextView budget;
        public final ImageView image;

        public VHCategory(View view)
        {
            super(view);
            description = (TextView) view.findViewById(R.id.req_description);
            budget = (TextView) view.findViewById(R.id.req_budget);
            image = (ImageView) view.findViewById(R.id.req_image);
        }
    }
}
