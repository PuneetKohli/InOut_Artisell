package com.coep.puneet.artisell_ecommerce.UI.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.clarifai.api.exception.ClarifaiException;
import com.coep.puneet.artisell_ecommerce.Custom.ExpandableHeightGridView;
import com.coep.puneet.artisell_ecommerce.Custom.RecyclerItemClickListener;
import com.coep.puneet.artisell_ecommerce.Global.AppConstants;
import com.coep.puneet.artisell_ecommerce.R;
import com.coep.puneet.artisell_ecommerce.UI.Adapter.CategoryGridAdapter;
import com.coep.puneet.artisell_ecommerce.UI.Adapter.ProductListAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements TextView.OnEditorActionListener
{
    @Bind(R.id.newProductsRecycler) RecyclerView mRecyclerView;
    @Bind(R.id.category_grid_view) ExpandableHeightGridView navGrid;
    @Bind(R.id.et_search) EditText searchText;

    Bitmap bm;
    private final ClarifaiClient client = new ClarifaiClient(APP_ID, APP_SECRET);
    private static final String APP_ID = "fT_vdVp2I1INwwolmoF5G_cG2gbnRONmS4QnZpgH";
    private static final String APP_SECRET = "qFNVrIR411_-zR6ssALK_5UuB9QwcHx6RprHBGbb";
    ProductListAdapter mAdapter;
    LinearLayoutManager mLayoutManager;


    @OnClick(R.id.button_image_search)
    void imageSearch()
    {
        Log.d("Test", "Test");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, AppConstants.REQUEST_CAMERA);
    }

    @OnClick(R.id.button_text_search)
    void textSearch()
    {
        Log.d("Test", "Test clicked search button");
        //Do image search!
        String tags = searchText.getText().toString();
        String[] temp = tags.split(" ");
        ArrayList<String> temp1 = new ArrayList<>();
        Collections.addAll(temp1, temp);

        manager.tagSearch(temp1);
        navigator.openNewActivity(MainActivity.this, new ProductListActivity());

    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        manager.getAllProducts();
        manager.getAllCategory();
    }

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_main;
    }

    @Override
    protected void setupToolbar()
    {

    }

    @Override
    protected void setupLayout()
    {
        manager.delegate = this;
        searchText.clearFocus();
        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                Log.d("Test", "Focus changed");
                //Open Search Activity
            }
        });

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mAdapter = new ProductListAdapter(this, null);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                manager.currentProduct = manager.allProducts.get(position);
                navigator.openNewActivity(MainActivity.this, new ProductDetailedActivity());
            }
        }));

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height1 = metrics.heightPixels;

        navGrid.setAdapter(new CategoryGridAdapter(this, manager.productCategories));
        navGrid.setExpanded(true);
        navGrid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                manager.searchProducts.clear();
                manager.getAllProductsOfCategory(manager.productCategories.get(position));
                navigator.openNewActivity(MainActivity.this, new ProductListActivity());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_request) {
            navigator.openNewActivity(MainActivity.this, new RequestActivity());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(String result, String type)
    {
        switch (type)
        {
            case AppConstants.RESULT_PRODUCT_LIST:
                if (manager.allProducts.size() != 0)
                {
                    mAdapter = new ProductListAdapter(MainActivity.this, manager.allProducts);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            case AppConstants.RESULT_CATEGORY_LIST:
                if (manager.productCategories.size() != 0)
                {
                    navGrid.setAdapter(new CategoryGridAdapter(this, manager.productCategories));
                    navGrid.setExpanded(true);
                }
                break;
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if (event != null)
        {
            // if shift key is down, then we want to insert the '\n' char in the TextView;
            // otherwise, the default action is to send the message.
            if (!event.isShiftPressed())
            {
                Log.d("Yolo", "Yolo");
            }
            return true;
        }
        return false;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            // The user picked an image. Send it to Clarifai for recognition.
            //Log.d("", "User picked image: " + intent.getData());
            if (requestCode == AppConstants.REQUEST_CAMERA) {
                bm = (Bitmap) intent.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                assert bm != null;
                bm.compress(Bitmap.CompressFormat.JPEG, 70, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try
                {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            if (bm != null) {

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] byteArray = stream.toByteArray();

                // Run recognition on a background thread since it makes a network call.
                new AsyncTask<Bitmap, Void, RecognitionResult>() {
                    @Override protected RecognitionResult doInBackground(Bitmap... bitmaps) {
                        return recognizeBitmap(bitmaps[0]);
                    }
                    @Override protected void onPostExecute(RecognitionResult result) {
                        updateUIForResult(result);
                    }
                }.execute(bm);
            } else {
                Toast.makeText(this, "Unable to load Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private RecognitionResult recognizeBitmap(Bitmap bitmap) {
        try {
            // Scale down the image. This step is optional. However, sending large images over the
            // network is slow and  does not significantly improve recognition performance.
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 320,
                    320 * bitmap.getHeight() / bitmap.getWidth(), true);

            // Compress the image as a JPEG.
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            scaled.compress(Bitmap.CompressFormat.JPEG, 90, out);
            byte[] jpeg = out.toByteArray();

            // Send the JPEG to Clarifai and return the result.
            return client.recognize(new RecognitionRequest(jpeg)).get(0);
        } catch (ClarifaiException e) {
            Log.e("lol", "Clarifai error", e);
            return null;
        }
    }

    private void updateUIForResult(RecognitionResult result) {
        if (result != null) {
            ArrayList<String> tags = new ArrayList<>();
            if (result.getStatusCode() == RecognitionResult.StatusCode.OK) {
                for (Tag tag : result.getTags()) {
                    tags.add(tag.getName());
                }
                manager.tagSearch(tags);
                navigator.openNewActivity(MainActivity.this, new ProductListActivity());

            } else {
                Log.e("lol", "Clarifai: " + result.getStatusMessage());
            }
        } else {
            //Log.e("lol", "Sorry, there was an error recognizing your image.: " + result.getStatusMessage());
        }
    }

}
