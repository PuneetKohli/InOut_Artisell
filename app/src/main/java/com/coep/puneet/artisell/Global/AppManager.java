package com.coep.puneet.artisell.Global;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.coep.puneet.artisell.ParseObjects.Category;
import com.coep.puneet.artisell.ParseObjects.Events;
import com.coep.puneet.artisell.ParseObjects.Product;
import com.coep.puneet.artisell.ParseObjects.Request;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppManager extends Application
{

    private String LOG_TAG = AppManager.class.getCanonicalName();

    public ArrayList<Category> productCategories = new ArrayList<>();
    public ArrayList<Product> currentArtisanProducts = new ArrayList<>();
    public ArrayList<ParseUser> artisanList = new ArrayList<>();
    public ArrayList<Request> jobsList = new ArrayList<>();
    public Product currentProduct;
    public AsyncResponse delegate = null;

    public Locale myLocale;
    static boolean localeChanged;

    ConnectivityManager cm;
    NetworkInfo ni;
    public Bitmap currentBm;
    public ParseUser currentArtisanSelected;
    public ArrayList<Events> eventList = new ArrayList<>();


    @Override
    public void onCreate()
    {
        super.onCreate();
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        ni = cm.getActiveNetworkInfo();
        parseInit();
        currentProduct = new Product();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = settings.getString("language", "en");
        setLocale(lang);
    }

    // * manually changing locale/
    public void setLocale(String lang)
    {
        if (!"".equals(lang) && !getBaseContext().getResources().getConfiguration().locale.getLanguage().equals(lang))
        {
            Log.d("Manager", "Setting locale to " + lang);
            myLocale = new Locale(lang);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("language", lang);
            onConfigurationChanged(conf);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        Log.d("Manager", "Configuration changed");
        if (myLocale != null)
        {
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    private void parseInit()
    {
        ParseObject.registerSubclass(Request.class);
        ParseObject.registerSubclass(Events.class);
        ParseObject.registerSubclass(Category.class);
        ParseObject.registerSubclass(Product.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "A2M7yTjp5iULp8xiFXymM29yX2U9bHEKhJdcsJEN", "L7WlExlZM9DoWNQkCCxY8uf7ummyn6cy1yrhnU7U");
    }

    public void getAllCategory()
    {

        if ((ni != null) && (ni.isConnected()))
        {
            ParseQuery<Category> query = Category.getQuery();
            query.orderByAscending("category_name");
            query.findInBackground(new FindCallback<Category>()
            {
                @Override
                public void done(final List<Category> list, ParseException e)
                {
                    if (e == null)
                    {
                        ParseObject.unpinAllInBackground("category_list", list, new DeleteCallback()
                        {
                            public void done(ParseException e)
                            {
                                if (e != null)
                                {
                                    Log.d(LOG_TAG, e.getMessage());
                                    return;
                                }

                                // Add the latest results for this query to the cache.
                                ParseObject.pinAllInBackground("category_list", list);
                            }
                        });
                        productCategories.clear();
                        for (int i = 0; i < list.size(); i++)
                        {
                            productCategories.add(list.get(i));
                        }
                        Log.d("Manager", "Got all category list");
                        delegate.processFinish(LOG_TAG, AppConstants.RESULT_CATEGORY_LIST);

                    }
                    else
                    {
                        Log.d(LOG_TAG, e.getMessage());
                    }
                }
            });
        }
        else
        {
            getAllCategoryLocal();
        }
    }

    public void getAllCategoryLocal()
    {
        ParseQuery<Category> query = Category.getQuery();
        query.orderByAscending("category_name");
        query.fromPin("category_list");
        query.findInBackground(new FindCallback<Category>()
        {
            @Override
            public void done(List<Category> list, ParseException e)
            {
                if (e == null)
                {
                    productCategories.clear();
                    for (int i = 0; i < list.size(); i++)
                    {
                        productCategories.add(list.get(i));
                    }
                    delegate.processFinish(LOG_TAG, AppConstants.RESULT_CATEGORY_LIST);
                    getAllCategory();
                }
                else
                {
                    getAllCategory();
                    delegate.processFinish(LOG_TAG, AppConstants.RESULT_CATEGORY_LIST_ERROR);
                }
            }
        });
    }

    public void loginArtisan(String mobile)
    {

        if ((ni != null) && (ni.isConnected()))
        {
            ParseUser.logInInBackground(mobile, "password", new LogInCallback()
            {
                @Override
                public void done(ParseUser parseUser, ParseException e)
                {
                    if (e == null)
                    {
                        SharedPreferences.Editor editor = getSharedPreferences("Parse", MODE_PRIVATE).edit();
                        editor.putString("objectId", parseUser.getObjectId());
                        editor.apply();
                        Log.d("Manager", "Login complete");
                        delegate.processFinish(LOG_TAG, AppConstants.RESULT_LOGIN_SUCCESS);
                        //getAllProductsFromCurrentArtisan();
                    }
                    else
                    {
                        Log.d(LOG_TAG, e.getMessage());
                        delegate.processFinish(LOG_TAG, AppConstants.RESULT_LOGIN_FAIL);
                        //getAllProductsFromCurrentArtisanOffline();
                    }
                }
            });
        }
        else
        {
            //getAllProductsFromCurrentArtisanOffline();
        }
    }

    public void getAllProductsFromCurrentArtisan()
    {
        if ((ni != null) && (ni.isConnected()))
        {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.include("user_products");
            query.include("user_products.product_category");

            query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>()
            {
                @Override
                public void done(final ParseUser artisan, ParseException e)
                {
                    if (e == null)
                    {
                        @SuppressWarnings("unchecked") final ArrayList<Product> currentProducts = (ArrayList<Product>) artisan.get("user_products");
                        ParseObject.unpinAllInBackground("user_product_list", currentProducts, new DeleteCallback()
                        {
                            public void done(ParseException e)
                            {
                                if (e != null)
                                {
                                    Log.d(LOG_TAG, e.getMessage());
                                    return;
                                }

                                // Add the latest results for this query to the cache.
                                if (currentProducts != null)
                                {
                                    ParseObject.pinAllInBackground("user_product_list", currentProducts);
                                }
                            }
                        });
                        if (currentProducts != null)
                        {
                            currentArtisanProducts.clear();
                            currentArtisanProducts.addAll(currentProducts);
                            Log.d("Manager", "Product list fetched with size " + currentArtisanProducts.size());
                        }
                        else
                        {
                            Log.d("Manager", "Product List empty from Parse");
                        }
                        delegate.processFinish("manager", AppConstants.RESULT_PRODUCT_LIST);
                    }
                    else
                    {
                        //getAllProductsFromCurrentArtisanOffline();
                    }
                }
            });
        }
        else
        {
            //getAllProductsFromCurrentArtisanOffline();
        }
    }

    public void getAllArtisans()
    {
        if ((ni != null) && (ni.isConnected()))
        {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.include("user_products");
            query.include("user_products.product_category");
            query.whereEqualTo("is_artisan", true);

            query.findInBackground(new FindCallback<ParseUser>()
            {
                @Override
                public void done(final List<ParseUser> artisan, ParseException e)
                {
                    if (e == null)
                    {
                        artisanList.clear();
                        for (int i = 0; i < artisan.size(); i++)
                        {
                            ParseObject.pinAllInBackground("artisan_list", artisan);
                            if (!artisan.get(i).getUsername().equals(ParseUser.getCurrentUser().getUsername()))
                                artisanList.add(artisan.get(i));
                        }
                        delegate.processFinish("manager", AppConstants.RESULT_ARTISAN);
                    }
                    else
                    {
                        //getAllProductsFromCurrentArtisanOffline();
                    }
                }
            });
        }
        else
        {
            //getAllProductsFromCurrentArtisanOffline();
        }
    }

    public void getAllArtisansLocal()
    {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.include("user_products");
        query.include("user_products.product_category");
        query.whereEqualTo("is_artisan", true);
        query.fromLocalDatastore();

        query.findInBackground(new FindCallback<ParseUser>()
        {
            @Override
            public void done(final List<ParseUser> artisan, ParseException e)
            {
                if (e == null)
                {
                    artisanList.clear();

                    for (int i = 0; i < artisan.size(); i++)
                    {
                        if (!artisan.get(i).getUsername().equals(ParseUser.getCurrentUser().getUsername()))
                            artisanList.add(artisan.get(i));

                    }
                    delegate.processFinish("manager", AppConstants.RESULT_ARTISAN);
                    getAllArtisans();
                }
                else
                {
                    getAllArtisans();
                    //getAllProductsFromCurrentArtisanOffline();
                }
            }
        });
    }

    public void getAllProductsFromCurrentArtisanOffline()
    {
        SharedPreferences prefs = getSharedPreferences("Parse", MODE_PRIVATE);
        String restoredText = prefs.getString("objectId", null);
        String objectId = null;
        if (restoredText != null)
        {
            objectId = prefs.getString("objectId", "");//"No name defined" is the default value.
        }

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.include("user_products");
        query.include("user_products.product_category");
        query.fromLocalDatastore();
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>()
        {
            @Override
            public void done(final ParseUser artisan, ParseException e)
            {
                if (e == null)
                {
                    @SuppressWarnings("unchecked") final ArrayList<Product> currentProducts = (ArrayList<Product>) artisan.get("user_products");
                    currentArtisanProducts.clear();
                    currentArtisanProducts.addAll(currentProducts);
                    delegate.processFinish("manager", AppConstants.RESULT_PRODUCT_LIST);
                    getAllProductsFromCurrentArtisan();
                }
                else
                {
                    delegate.processFinish(LOG_TAG, AppConstants.RESULT_PRODUCT_LIST_ERROR);
                    getAllProductsFromCurrentArtisan();

                }
            }
        });
    }

    public void getAllEvents()
    {
        if ((ni != null) && (ni.isConnected()))
        {
            ParseQuery<Events> query = Events.getQuery();

            query.findInBackground(new FindCallback<Events>()
            {
                @Override
                public void done(final List<Events> events, ParseException e)
                {
                    if (e == null)
                    {
                        ParseObject.pinAllInBackground("event_list", events);

                        eventList.clear();
                        for (int i = 0; i < events.size(); i++)
                        {

                            eventList.add(events.get(i));
                        }
                        delegate.processFinish("manager", AppConstants.RESULT_ARTISAN);
                    }
                    else
                    {
                        //getAllProductsFromCurrentArtisanOffline();
                    }
                }
            });
        }
        else
        {
            //getAllProductsFromCurrentArtisanOffline();
        }
    }

    public void getAllEventsLocal()
    {
        ParseQuery<Events> query = Events.getQuery();
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<Events>()
        {
            @Override
            public void done(final List<Events> events, ParseException e)
            {
                if (e == null)
                {
                    eventList.clear();
                    for (int i = 0; i < events.size(); i++)
                    {

                        eventList.add(events.get(i));
                    }
                    delegate.processFinish("manager", AppConstants.RESULT_ARTISAN);
                    getAllEvents();
                }
                else
                {
                    //getAllProductsFromCurrentArtisanOffline();
                    getAllEvents();

                }
            }
        });
    }

    public void getAllRequests()
    {
        ParseQuery<Request> query = Request.getQuery();
        query.include("req_category");
        query.whereEqualTo("status", 0);
        query.findInBackground(new FindCallback<Request>()
        {
            @Override
            public void done(List<Request> objects, ParseException e)
            {
                if (e == null)
                {
                    ParseObject.pinAllInBackground("request_list", objects);

                    jobsList.clear();
                    jobsList.addAll(objects);
                }
            }
        });
    }

    public void getAllRequestsLocal()
    {
        ParseQuery<Request> query = Request.getQuery();
        query.fromLocalDatastore();
        query.include("req_category");
        query.whereEqualTo("status", 0);
        query.findInBackground(new FindCallback<Request>()
        {
            @Override
            public void done(List<Request> objects, ParseException e)
            {
                if (e == null)
                {
                    jobsList.clear();
                    jobsList.addAll(objects);
                    getAllRequests();
                }
            }
        });
    }
}
