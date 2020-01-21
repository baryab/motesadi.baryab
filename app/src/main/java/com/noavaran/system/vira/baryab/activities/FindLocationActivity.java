package com.noavaran.system.vira.baryab.activities;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnLocationClickListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.activities.map.ir.GPSTraker;

import java.util.List;

import ir.map.sdk_map.MapirStyle;
import ir.map.sdk_map.maps.MapView;

public class FindLocationActivity extends AppCompatActivity {

    MapboxMap map;
    Style mapStyle;
    MapView mapView;


    LocationEngine locationEngine;
    LocationLayerPlugin locationLayerPlugin;
    PermissionsManager permissionsManager;
    Location originLayout;

    GPSTraker gpsTraker;
    private double longtitude;
    private double latitude;

    private LatLng myPoint = new LatLng(latitude, longtitude);
    int zoom = 25;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location);

        gpsTraker = new GPSTraker(this);
        if (gpsTraker.canGetLocation) {
            latitude = gpsTraker.latitude;
            longtitude = gpsTraker.longtitude;
        }

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                map = mapboxMap;
                map.setStyle(new Style.Builder().fromUri(MapirStyle.MAIN_MOBILE_VECTOR_STYLE), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapStyle = style;

                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longtitude))
                                .title("موقعیت من"));

                        zoomToSpecificLocation();
                        enableLocationComponent();
                    }
                });
            }
        });


    }

    private void zoomToSpecificLocation() {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(myPoint, zoom));

    }

    private void enableLocationComponent() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Create and customize the LocationComponent's options
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .elevation(5)
                    .accuracyAlpha(.6f)
                    .accuracyColor(Color.RED)
                    .build();
            // Get an instance of the component
            LocationComponent locationComponent = map.getLocationComponent();
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, mapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build();
            // Activate with options
            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
            // Add the location icon click listener
            locationComponent.addOnLocationClickListener(new OnLocationClickListener() {
                @Override
                public void onLocationComponentClick() {
                }
            });
        } else {
            PermissionsManager permissionsManager = new PermissionsManager(new PermissionsListener() {
                @Override
                public void onExplanationNeeded(List<String> permissionsToExplain) {
                }
                @Override
                public void onPermissionResult(boolean granted) {
                    if (granted)
                        enableLocationComponent();
                    else
                        Toast.makeText(FindLocationActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
            });
            permissionsManager.requestLocationPermissions(this);
        }
    }


}
