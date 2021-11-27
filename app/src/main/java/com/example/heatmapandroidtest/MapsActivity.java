package com.example.heatmapandroidtest;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.heatmapandroidtest.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private HeatmapTileProvider mProvider;
    private LatLng sydney;
    private List<Marker> markerList = new ArrayList<Marker>();
    private ClusterManager<House> clusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        clusterManager = new ClusterManager<House>(this, mMap);

        //Move camera to sydney
        sydney = new LatLng(-34, 151);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        int[] colors = {
                Color.rgb(123, 255, 116),
                Color.rgb(188, 218, 35),
                Color.rgb(228, 175, 0),
                Color.rgb(252, 126, 24),
                Color.rgb(255, 71, 71)
        };

        float[] startPoints = {
                0.2f, 0.4f, 0.6f, 0.8f, 1f
        };

        ArrayList<WeightedLatLng> locations = generateLocations();
        mProvider = new HeatmapTileProvider.Builder().weightedData( locations ).build();
        mProvider.setRadius( HeatmapTileProvider.DEFAULT_RADIUS );
        mProvider.setGradient(new Gradient(colors, startPoints));
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private ArrayList<WeightedLatLng> generateLocations() {
        ArrayList<WeightedLatLng> locations = new ArrayList<WeightedLatLng>();
        double lat;
        double lng;
        double intensity;
        Random generator = new Random();
        for( int i = 0; i < 100; i++ ) {
            lat = generator.nextDouble() / 3;
            lng = generator.nextDouble() / 3;
            intensity = generator.nextDouble() / 3;
            if( generator.nextBoolean() ) {
                lat = -lat;
            }
            if( generator.nextBoolean() ) {
                lng = -lng;
            }
            locations.add(new WeightedLatLng(new LatLng(sydney.latitude + lat, sydney.longitude + lng), intensity));
            //Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(sydney.latitude + lat, sydney.longitude + lng)));
            //markerList.add(marker);
            clusterManager.addItem(new House(new LatLng(sydney.latitude + lat, sydney.longitude + lng)));
        }
        setUpClusterer();
        return locations;
    }

    private void setUpClusterer() {
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
    }

}