package com.coep.puneet.artisell.UI.Activity;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.coep.puneet.artisell.R;
import com.coep.puneet.artisell.UI.Adapter.JobListAdapter;

import butterknife.Bind;

public class JobActivity extends BaseActivity
{
    @Bind(R.id.jobRecycler) RecyclerView mRecyclerView;

    JobListAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_job;
    }

    @Override
    protected void setupToolbar()
    {
        getSupportActionBar().setTitle(getString(R.string.title_activity_jobs_activity));

    }

    @Override
    protected void setupLayout()
    {

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mAdapter = new JobListAdapter(this, manager.jobsList);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }
}
