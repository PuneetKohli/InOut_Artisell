package com.coep.puneet.artisell.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coep.puneet.artisell.ParseObjects.Category;
import com.coep.puneet.artisell.R;
import com.coep.puneet.artisell.UI.Activity.AddProductActivity;

import java.util.ArrayList;

/**
 * Created by Arun on 29-Oct-15.
 */
public class CategoryGridAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<Category> categoryList;
    private int height;
    private int setTickPosition = -1;

    public CategoryGridAdapter(Context mContext, ArrayList<Category> categoryList)
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
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null)
        {
            gridView = new View(mContext);
            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.category_grid_item, null);
            //imageView.setImageResource(mNavIds[position]);
            //imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height / 4));
        }
        else
        {
            gridView = (View) convertView;
        }

        TextView textView = (TextView) gridView.findViewById(R.id.tv_cat_name);

        if (setTickPosition != -1)
        {
            if (position == setTickPosition)
            {
                textView.setTextColor(mContext.getResources().getColor(R.color.app_primary));
                gridView.findViewById(R.id.selected_tick).setVisibility(View.VISIBLE);
                ((AddProductActivity) mContext).manager.currentProduct.setCategory(((AddProductActivity) mContext).manager.productCategories.get(position));
            }
            else
            {
                textView.setTextColor(mContext.getResources().getColor(android.R.color.secondary_text_light_nodisable));
                gridView.findViewById(R.id.selected_tick).setVisibility(View.INVISIBLE);
            }
        }

        // set image based on selected text
        ImageView imageView = (ImageView) gridView.findViewById(R.id.iv_cat_icon);

        String curText = categoryList.get(position).getCategory_name();
        textView.setText(categoryList.get(position).getCategory_name());

        if (categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("men"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.men_clothing));
        }
        else if (categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("wom"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.women_clothing));
        }
        else if (categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("bag"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bags));
        }
        else if (categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("jew"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.jewelry));
        }
        else if (categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("foot"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.footwear));
        }
        else if (categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("wall"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.clocks));
        }
        else if (categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("pai"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.paintings));
        }
        else if (categoryList.get(position).getCategory_name().toLowerCase().trim().startsWith("pot"))
        {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pots));
        }

        return gridView;
    }

    public void updateView(int position)
    {
        this.setTickPosition = position;
        notifyDataSetChanged();
    }
}
