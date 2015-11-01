package com.coep.puneet.artisell_ecommerce.ParseObjects;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Category")
public class Category extends ParseObject
{

    public String getCategory_name()
    {
        try
        {
            fetchIfNeeded();
            return getString("category_name");
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void setCategory_name(String category_name)
    {
        put("category_name", category_name);
    }

    public static ParseQuery<Category> getQuery()
    {
        return ParseQuery.getQuery(Category.class);
    }

    public static boolean isSelected;

}
