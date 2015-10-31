package com.coep.puneet.artisell.UI.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.coep.puneet.artisell.R;
import com.coep.puneet.artisell.UI.Adapter.ArtisanListAdapter;

import butterknife.Bind;

public class ArtisanSearch extends BaseActivity
{
    @Bind(R.id.artisanSearchRecycler) RecyclerView mRecyclerView;

    ArtisanListAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_artisan_search;
    }

    @Override
    protected void setupToolbar()
    {
        getSupportActionBar().setTitle(R.string.title_activity_artisan_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void setupLayout()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                navigator.openNewActivity(ArtisanSearch.this, new MapsActivityArtisans());
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mAdapter = new ArtisanListAdapter(this, manager.artisanList);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


/*        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
            }
        }));*/

    }

}
