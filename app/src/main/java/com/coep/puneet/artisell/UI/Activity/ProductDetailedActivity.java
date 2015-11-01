package com.coep.puneet.artisell.UI.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coep.puneet.artisell.R;
import com.coep.puneet.artisell.UI.Fragment.AddProductWizardFragment;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import butterknife.Bind;

public class ProductDetailedActivity extends BaseActivity
{

    @Bind(R.id.tv_product_price) TextView productPrice;
    @Bind(R.id.product_image) ImageView productImage;
    @Bind(R.id.tv_product_category) TextView productCategory;
    @Bind(R.id.tv_product_quantity) TextView productQuantity;
    @Bind(R.id.tv_keypoints) TextView productTags;
    @Bind(R.id.iv_product_category) ImageView catImage;

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
