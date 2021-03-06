package com.coep.puneet.artisell.UI.Activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.coep.puneet.artisell.Custom.ExpandableHeightGridView;
import com.coep.puneet.artisell.R;
import com.coep.puneet.artisell.UI.Adapter.NavGridAdapter;

import butterknife.Bind;

public class HomeActivity extends BaseActivity
{
    @Bind(R.id.category_grid_view) ExpandableHeightGridView navGrid;
    private Boolean exit = false;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        manager.delegate = this;
        //manager.loginArtisan("7507118432");
    }

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_home;
    }

    @Override
    protected void setupToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void setupLayout()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height1 = metrics.heightPixels;
        String[] mNavLabels = {getString(R.string.home_button_1), getString(R.string.home_button_2), getString(R.string.home_button_3), getString(R.string.home_button_4), getString(R.string.home_button_5), getString(R.string.home_button_6), getString(R.string.home_button_7), getString(R.string.home_button_8)};
        Integer[] mNavIds = {R.drawable.home_view_product, R.drawable.home_add_product, R.drawable.home_convert_language, R.drawable.home_my_profile, R.drawable.home_other_artisans, R.drawable.home_event, R.drawable.home_job_search, R.drawable.bs_ic_more_light};

        navGrid.setAdapter(new NavGridAdapter(this, mNavIds, mNavLabels, height1));
        navGrid.setExpanded(true);
        navGrid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        navigator.openNewActivity(HomeActivity.this, new ViewProductActivity());
                        break;
                    case 1:
                        navigator.openNewActivity(HomeActivity.this, new AddProductActivity());
                        break;
                    case 2:
                        setLanguage();
                        break;
                    case 3:
                        navigator.openNewActivity(HomeActivity.this, new ProfileActivity());
                        break;
                    case 4:
                        navigator.openNewActivity(HomeActivity.this, new ArtisanSearch());
                        break;
                    case 5:
                        navigator.openNewActivity(HomeActivity.this, new EventSearch());
                        break;
                    case 6:
                        navigator.openNewActivity(HomeActivity.this, new JobActivity());
                        break;
                }
            }
        });
    }

    private void setLanguage()
    {
        new BottomSheet.Builder(this).title(getString(R.string.change_language)).sheet(R.menu.menu_change_language).listener(new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case R.id.english:
                        manager.setLocale("en");
                        restartActivity();
                        break;
                    case R.id.hindi:
                        manager.setLocale("hi");
                        restartActivity();
                        break;
                }
            }
        }).show();
    }

    private void restartActivity() {
        finish();
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed()
    {

        if (exit)
        {
            this.finishAffinity();
        }
        else
        {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }
};

