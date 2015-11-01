package com.coep.puneet.artisell_ecommerce.UI.Activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.coep.puneet.artisell_ecommerce.Custom.ExpandableHeightGridView;
import com.coep.puneet.artisell_ecommerce.Custom.RecyclerItemClickListener;
import com.coep.puneet.artisell_ecommerce.Global.AppConstants;
import com.coep.puneet.artisell_ecommerce.R;
import com.coep.puneet.artisell_ecommerce.UI.Adapter.CategoryGridAdapter;
import com.coep.puneet.artisell_ecommerce.UI.Adapter.ProductListAdapter;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity
{
    @Bind(R.id.newProductsRecycler) RecyclerView mRecyclerView;
    @Bind(R.id.category_grid_view) ExpandableHeightGridView navGrid;

    ProductListAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    @OnClick(R.id.button_image_search)
    void imageSearch()
    {
        Log.d("Test", "Test");
        //Do image search!
    }

    @Bind(R.id.et_search) EditText editTextSearch;

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
        editTextSearch.clearFocus();
        editTextSearch.setOnFocusChangeListener(new View.OnFocusChangeListener()
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
                /*switch (position)
                {
                    case 0:
                        navigator.openNewActivity(MainActivity.this, new ViewProductActivity());
                        break;
                    case 1:
                        navigator.openNewActivity(MainActivity.this, new AddProductActivity());
                        break;
                    case 2:
                        setLanguage();
                        break;
                    case 3:
                        navigator.openNewActivity(MainActivity.this, new ProfileActivity());
                        break;
                    case 4:
                        navigator.openNewActivity(MainActivity.this, new ArtisanSearch());
                        break;
                    case 5:
                        navigator.openNewActivity(MainActivity.this, new EventSearch());
                        break;
                }*/
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
}
