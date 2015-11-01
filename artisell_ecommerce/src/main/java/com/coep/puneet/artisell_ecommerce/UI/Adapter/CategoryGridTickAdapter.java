package com.coep.puneet.artisell_ecommerce.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.coep.puneet.artisell_ecommerce.ParseObjects.Category;
import com.coep.puneet.artisell_ecommerce.R;
import com.coep.puneet.artisell_ecommerce.UI.Activity.RequestDetailed;

import java.util.ArrayList;

/**
 * Created by Arun on 29-Oct-15.
 */
public class CategoryGridTickAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<Category> categoryList;
    private int height;
    private int setTickPosition = -1;

    public CategoryGridTickAdapter(Context mContext, ArrayList<Category> categoryList)
    {
        this.mContext = mContext;
        this.categoryList = categoryList;
    }

    @Override
    public int getCount()
    {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(mContext);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.category_grid_item_tick, null);
            //imageView.setImageResource(mNavIds[position]);
            //imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height / 4));


        } else {
            gridView = (View) convertView;
        }

        if(position == setTickPosition) {
            gridView.findViewById(R.id.selected_tick).setVisibility(View.VISIBLE);
            ((RequestDetailed) mContext).manager.currentProduct.setCategory(((RequestDetailed) mContext).manager.productCategories.get(position));
        }
        else {
            gridView.findViewById(R.id.selected_tick).setVisibility(View.INVISIBLE);
        }

        // set image based on selected text
        ImageView imageView = (ImageView) gridView
                .findViewById(R.id.iv_cat_icon);

        if(categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("men"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_mens_clothing));
        }
        else if (categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("wom"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_womens_clothing));
        }
        else if (categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("bag"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_bags));
        }
        else if (categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("jew"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_jewelry));
        }
        else if (categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("foot"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_footwear));
        }
        else if (categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("wall"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_clocks));
        }
        else if (categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("pai"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_paintings));
        }
        else if (categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("pot"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.category_pots));
        }

        return gridView;
    }

    public void updateView(int position)
    {
        this.setTickPosition = position;
        notifyDataSetChanged();
    }
}
