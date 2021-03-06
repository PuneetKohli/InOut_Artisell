package com.coep.puneet.artisell.UI.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coep.puneet.artisell.Global.Navigator;
import com.coep.puneet.artisell.R;
import com.coep.puneet.artisell.UI.Activity.AddProductActivity;
import com.coep.puneet.artisell.UI.Activity.ArtisanProfileActivity;
import com.coep.puneet.artisell.UI.Activity.ArtisanSearch;
import com.parse.ParseUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtisanListAdapter extends RecyclerView.Adapter<ArtisanListAdapter.VHArtisan>
{
    private Context mContext;
    private ArrayList<ParseUser> artisanList;

    public ArtisanListAdapter(Context mContext, ArrayList<ParseUser> artisanList)
    {
        this.mContext = mContext;
        this.artisanList = artisanList;
    }

    @Override
    public ArtisanListAdapter.VHArtisan onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.artisan_list_item, parent, false);
        return new VHArtisan(view);
    }

    @Override
    public void onBindViewHolder(ArtisanListAdapter.VHArtisan holder, final int position)
    {
        final ParseUser user = artisanList.get(position);

        holder.name.setText(user.getString("name"));
        holder.category.setText(user.getString("primary_category"));
        holder.location.setText(user.getString("location"));
        Glide.with(mContext).load(user.getParseFile("profile_image").getUrl()).asBitmap().centerCrop().placeholder(R.mipmap.ic_launcher).into(holder.image);

        holder.buttonCall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Navigator navigator = new Navigator();
                navigator.callIntent(mContext, user.getUsername());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((ArtisanSearch)mContext).manager.currentArtisanSelected = ((ArtisanSearch)mContext).manager.artisanList.get(position);
                Navigator n = new Navigator();
                n.openNewActivity(mContext, new ArtisanProfileActivity());
            }
        });

        if(user.getString("primary_category").toLowerCase().trim().startsWith("men"))
        {
            holder.categoryImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_mens_clothing));
        }
        else if (user.getString("primary_category").toLowerCase().trim().startsWith("wom"))
        {
            holder.categoryImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_womens_clothing));
        }
        else if (user.getString("primary_category").toLowerCase().trim().startsWith("bag"))
        {
            holder.categoryImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_bags));
        }
        else if (user.getString("primary_category").toLowerCase().trim().startsWith("jew"))
        {
            holder.categoryImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_jewelry));
        }
        else if (user.getString("primary_category").toLowerCase().trim().startsWith("foot"))
        {
            holder.categoryImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_footwear));
        }
        else if (user.getString("primary_category").toLowerCase().trim().startsWith("wall"))
        {
            holder.categoryImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_clocks));
        }
        else if (user.getString("primary_category").toLowerCase().trim().startsWith("pai"))
        {
            holder.categoryImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_paintings));
        }
        else if (user.getString("primary_category").toLowerCase().trim().startsWith("pot"))
        {
            holder.categoryImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_pots));
        }
    }

    @Override
    public int getItemCount()
    {
        return artisanList.size();
    }

    public static class VHArtisan extends RecyclerView.ViewHolder
    {
        public final TextView name;
        public final TextView category;
        public final TextView location;
        public final CircleImageView image;
        public final View buttonCall;
        public final ImageView categoryImage;

        public VHArtisan(View view)
        {
            super(view);
            name = (TextView) view.findViewById(R.id.artisanName);
            category = (TextView) view.findViewById(R.id.artisanCategory);
            location = (TextView) view.findViewById(R.id.artisanLocation);
            image = (CircleImageView) view.findViewById(R.id.artisanProfilePic);
            buttonCall = view.findViewById(R.id.button_call_other_artisan);
            categoryImage = (ImageView) view.findViewById(R.id.category_image);
        }
    }
}
