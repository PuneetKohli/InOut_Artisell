package com.coep.puneet.artisell_ecommerce.UI.Activity;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.coep.puneet.artisell_ecommerce.Custom.DividerItemDecoration;
import com.coep.puneet.artisell_ecommerce.Custom.SlidingTabLayout;
import com.coep.puneet.artisell_ecommerce.Global.AppConstants;
import com.coep.puneet.artisell_ecommerce.ParseObjects.Request;
import com.coep.puneet.artisell_ecommerce.R;
import com.coep.puneet.artisell_ecommerce.UI.Adapter.RequestAdapter;

import java.util.ArrayList;

public class RequestActivity extends BaseActivity
{
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private CategoryPagerAdapter mPagerAdapter;
    private Context mContext;
    private ProgressBar mProgressBar;

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_request;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setVisibility(View.INVISIBLE);
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setVisibility(View.GONE);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_loading);

        manager.loginCustomer("test@gmail.com", "password");
    }

    @Override
    protected void setupToolbar()
    {
        final Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setTitle("Requests");
        actionBar.canShowOverflowMenu();
    }

    @Override
    protected void setupLayout()
    {
        mContext = this;
        manager.delegate = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Add Request", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        // manager.getAllRequestOfCustomer();
    }

    @Override
    public void processFinish(String result, String type)
    {
        switch (type)
        {
            case AppConstants.RESULT_REQUEST:
                mProgressBar.setVisibility(View.GONE);
                mSlidingTabLayout.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.VISIBLE);
                mViewPager.setAdapter(new CategoryPagerAdapter());
                mSlidingTabLayout.setViewPager(mViewPager);
                break;
        }
    }

    class CategoryPagerAdapter extends PagerAdapter
    {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount()
        {
            return 3;
        }

        /**
         * Instantiate the {@link android.view.View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position)
        {
            // Inflate a new layout from our resources
            View view = getLayoutInflater().inflate(R.layout.request_recycler, container, false);
            container.addView(view);

            RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.requestRecycler);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(llm);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, null));

            ArrayList<Request> reqdList = new ArrayList<>();
            switch (position)
            {
                case 0:
                    reqdList = manager.pendingList;
                    break;
                case 1:
                    reqdList = manager.acceptedList;
                    break;
                case 2:
                    reqdList = manager.doneList;
                    break;
            }

            RequestAdapter mAdapter = new RequestAdapter(mContext, reqdList);
            mRecyclerView.setAdapter(mAdapter);

           /* ArrayList<Category> subcategories = manager.categoryList.get(position).getSubcategories();
            final ArrayList<Category> subsubcategories = new ArrayList<>();

            CategoryAdapter mAdapter = new CategoryAdapter(mContext, null);

            for (int i = 0; i < subcategories.size(); i++)
            {
                subsubcategories.addAll(subcategories.get(i).getSubcategories());
            }
            mAdapter.addAll(subsubcategories);

            final List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();
            int count = 0;
            for (int i = 0; i < subcategories.size(); i++)
            {
                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(count, subcategories.get(i).getCategoryName()));
                count += subcategories.get(i).getSubcategories().size();
            }
            //Sections
                *//*sections.add(new SimpleSectionedRecyclerViewAdapter.Section(14,"Section 4"));
                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(20,"Section 5"));*//*

            SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
            SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new SimpleSectionedRecyclerViewAdapter(mContext, R.layout.section, R.id.section_text, mAdapter);
            mSectionedAdapter.setSections(sections.toArray(dummy));

            //Apply this adapter to the RecyclerView
            mRecyclerView.setAdapter(mSectionedAdapter);

            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(View view, int itempos)
                        {
                            int offset = 0;
                            for (int i = 0; i < sections.size(); i++)
                            {
                                if (itempos > sections.get(i).sectionedPosition)
                                {
                                    *//*Log.d("SECTION", "First: " + sections.get(i).firstPosition);
                                    Log.d("SECTION", "Sectioned: " + sections.get(i).sectionedPosition);
                                    Log.d("SECTION", "Title: " + sections.get(i).getTitle());*//*
                                    offset += 1;
                                }
                                else
                                {
                                    break;
                                }
                            }
                            //Log.d("ONCLICK", "Offset is " + offset);
                            itempos -= offset;
                            manager.currentCategory = manager.categoryList.get(position);
                            *//*for (int i = 0; i < manager.categoryList.get(position).getSubcategories().size(); i++)
                            {
                                Category currentSubcategory = manager.categoryList.get(position).getSubcategories().get(i);
                                if (currentSubcategory.getSubcategories().contains(subsubcategories.get(itempos)))
                                {
                                    Log.d("PARSER", "Current subcat is " + currentSubcategory.getCategoryName());
                                    Log.d("PARSER", "Current subsubcat is " + subsubcategories.get(itempos).getCategoryName());
                                }
                            }*//*
                            manager.currentSubCategory = manager.categoryList.get(position);
                            manager.currentSubsubCategory = subsubcategories.get(itempos);
                            Intent productListIntent = new Intent(MainActivity.this, ProductListActivity.class);
                            startActivity(productListIntent);
                        }
                    }));*/


            return view;

        }

        /**
         * Destroy the item from the {@link android.support.v4.view.ViewPager}. In our case this is simply removing the
         * {@link android.view.View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(android.view.ViewGroup, int)} is the
         * same object as the {@link android.view.View} added to the {@link android.support.v4.view.ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o)
        {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0:
                    return "Pending";
                case 1:
                    return "Accepted";
                case 2:
                    return "Done";
                default:
                    return "Pending";
            }
        }


    }
}
