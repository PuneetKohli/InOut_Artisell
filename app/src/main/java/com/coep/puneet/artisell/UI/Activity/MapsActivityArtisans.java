package com.coep.puneet.artisell.UI.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.coep.puneet.artisell.Global.AppManager;
import com.coep.puneet.artisell.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivityArtisans extends FragmentActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    private AppManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        manager = (AppManager) getApplication();
        setContentView(R.layout.activity_maps_activity_artisans);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        ArrayList<LatLng> mCoords = new ArrayList<>();

        for(int i = 0 ; i < manager.artisanList.size(); i++) {
            mCoords.add(new LatLng(manager.artisanList.get(i).getParseGeoPoint("coordinates").getLatitude(), manager.artisanList.get(i).getParseGeoPoint("coordinates").getLongitude()));
            mMap.addMarker(new MarkerOptions().position(mCoords.get(i)).title(manager.artisanList.get(i).getString("name")).snippet(manager.artisanList.get(i).getUsername() + "\n" + manager.artisanList.get(i).getString("primary_category")));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mCoords.get(0)));

        zoomToCoverAllMarkers(mCoords, mMap);
    }

    private static void zoomToCoverAllMarkers(ArrayList<LatLng> list, GoogleMap googleMap)
    {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).latitude != 0.0 && list.get(i).longitude != 0.0)
            {
                builder.include(list.get(i));
            }
        }
        LatLngBounds bounds = builder.build();
        int padding = 10;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.moveCamera(cu);
        googleMap.animateCamera(cu);
    }
}
