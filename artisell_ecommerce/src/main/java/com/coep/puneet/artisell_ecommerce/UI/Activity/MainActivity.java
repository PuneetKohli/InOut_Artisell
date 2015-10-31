package com.coep.puneet.artisell_ecommerce.UI.Activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.coep.puneet.artisell_ecommerce.R;
import com.coep.puneet.artisell_ecommerce.UI.Adapter.ProductListAdapter;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity
{
    @Bind(R.id.newProductsRecycler) RecyclerView mRecyclerView;

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

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mAdapter = new ProductListAdapter(this, null);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
