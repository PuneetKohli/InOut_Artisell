package com.coep.puneet.artisell_ecommerce.UI.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.coep.puneet.artisell_ecommerce.R;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class ProductDetailedActivity extends BaseActivity
{

    @Bind(R.id.tv_product_price) TextView productPrice;
    @Bind(R.id.product_image) ImageView productImage;
    @Bind(R.id.tv_product_category) TextView productCategory;
    @Bind(R.id.tv_product_quantity) TextView productQuantity;
    @Bind(R.id.tv_keypoints) TextView productTags;
    @Bind(R.id.cat_img) ImageView catImage;

    @OnClick(R.id.loginButtonFrame)
    void click()
    {
        //Buy item
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_product_detailed;
    }

    @Override
    protected void setupToolbar()
    {
        getSupportActionBar().setTitle(manager.currentProduct.getProduct_name());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void setupLayout()
    {
        productPrice.setText("Rs. " + manager.currentProduct.getProductPrice() + "");
        productQuantity.setText("" + manager.currentProduct.getProductQuantity());
        ParseFile image = manager.currentProduct.getProductImage();
        String url = image.getUrl();
        Picasso.with(ProductDetailedActivity.this).load(url).placeholder(R.drawable.ab_background).into(productImage);
        Log.d("ProductDetail", "Image is : " + productImage);
        productCategory.setText(manager.currentProduct.getCategory().getCategory_name());
        ArrayList<String> tags = (manager.currentProduct.getProductTags());
        String keypoints = "";
        if (tags.size() > 0)
        {

            for (int i = 0; i < tags.size() - 1; i++)
            {
                keypoints += tags.get(i) + ", \t";
            }

            keypoints += tags.get(tags.size() - 1);
        }
        
        productTags.setText(keypoints);
        if(manager.currentProduct.getCategory().getCategory_name().toLowerCase().trim().startsWith("men"))
        {
            catImage.setImageDrawable(getResources().getDrawable(R.drawable.category_mens_clothing));
        }
        else if (manager.currentProduct.getCategory().getCategory_name().toLowerCase().trim().startsWith("wom"))
        {
            catImage.setImageDrawable(getResources().getDrawable(R.drawable.category_womens_clothing));
        }
        else if (manager.currentProduct.getCategory().getCategory_name().toLowerCase().trim().startsWith("bag"))
        {
            catImage.setImageDrawable(getResources().getDrawable(R.drawable.category_bags));
        }
        else if (manager.currentProduct.getCategory().getCategory_name().toLowerCase().trim().startsWith("jew"))
        {
            catImage.setImageDrawable(getResources().getDrawable(R.drawable.category_jewelry));
        }
        else if (manager.currentProduct.getCategory().getCategory_name().toLowerCase().trim().startsWith("foot"))
        {
            catImage.setImageDrawable(getResources().getDrawable(R.drawable.category_footwear));
        }
        else if (manager.currentProduct.getCategory().getCategory_name().toLowerCase().trim().startsWith("wall"))
        {
            catImage.setImageDrawable(getResources().getDrawable(R.drawable.category_clocks));
        }
        else if (manager.currentProduct.getCategory().getCategory_name().toLowerCase().trim().startsWith("pai"))
        {
            catImage.setImageDrawable(getResources().getDrawable(R.drawable.category_paintings));
        }
        else if (manager.currentProduct.getCategory().getCategory_name().toLowerCase().trim().startsWith("pot"))
        {
            catImage.setImageDrawable(getResources().getDrawable(R.drawable.category_pots));
        }


    }

}
