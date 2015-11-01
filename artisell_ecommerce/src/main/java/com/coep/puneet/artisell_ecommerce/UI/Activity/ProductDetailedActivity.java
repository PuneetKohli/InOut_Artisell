package com.coep.puneet.artisell_ecommerce.UI.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.coep.puneet.artisell_ecommerce.Global.SendUnicodeSms;
import com.coep.puneet.artisell_ecommerce.Global.Utils;
import com.coep.puneet.artisell_ecommerce.R;
import com.github.sendgrid.SendGrid;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;

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
        sendMessage("arunswaminathan94@gmail.com", 0);
        sendMessage("punkohl@gmail.com", 1);
        SendUnicodeSms.sendSms("9819954448", "You have received a new order. Please check the app");
        SendUnicodeSms.sendSms("7507118432", "Congratulation on purchasing the product. Your order is being processed currently");
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

    private class SendEmailWithSendGrid extends AsyncTask<Hashtable<String, String>, Void, String>
    {

        @Override
        protected String doInBackground(Hashtable<String, String>... params)
        {
            Hashtable<String, String> h = params[0];
            Utils creds = new Utils();
            SendGrid sendgrid = new SendGrid(creds.getUsername(), creds.getPassword());
            sendgrid.addTo(h.get("to"));
            sendgrid.setFrom(h.get("from"));
            sendgrid.setSubject(h.get("subject"));
            sendgrid.setText(h.get("text"));
            String response = sendgrid.send();
            return response;
        }
    }

    // Called when the user clicks the Send button
    @SuppressWarnings("unchecked")
    public void sendMessage(String source, int flag)
    {
        Hashtable<String, String> params = new Hashtable<String, String>();
        String result = null;

        params.put("to", source);
        params.put("from", "noreply@artisell.com");
        params.put("subject", "Artisell Transaction Status");
        if (flag == 0)
        {
            params.put("text", "Congratulation on purchasing this product. It shall delivered to you soon");
        }
        else
        {
            params.put("text", "You have a received a new order, Please visit the app to check its status");
        }
        // Send the Email
        SendEmailWithSendGrid email = new SendEmailWithSendGrid();
        try
        {
            result = email.execute(params).get();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
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
        if (manager.currentProduct.getCategory() == null)
        {
            manager.currentProduct.fetchInBackground(new GetCallback<ParseObject>()
            {
                @Override
                public void done(ParseObject object, ParseException e)
                {
                    productCategory.setText(manager.currentProduct.getCategory().getCategory_name());
                    if (manager.currentProduct.getCategory().getCategory_name().toLowerCase().trim().startsWith("men"))
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
            });
        }
        else
        {
            productCategory.setText(manager.currentProduct.getCategory().getCategory_name());
            if (manager.currentProduct.getCategory().getCategory_name().toLowerCase().trim().startsWith("men"))
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



    }

}
