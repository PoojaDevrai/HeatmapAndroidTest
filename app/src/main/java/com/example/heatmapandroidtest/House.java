package com.example.heatmapandroidtest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class House implements ClusterItem {

    private LatLng position;
    private String title;
    private String snippet;
    private double price;

    public House(LatLng position) {
        this.position = position;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return snippet;
    }

    public double getPrice() {
        return price;
    }
}
