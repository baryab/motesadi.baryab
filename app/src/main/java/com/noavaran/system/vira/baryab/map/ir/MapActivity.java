package com.noavaran.system.vira.baryab.map.ir;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
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
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolLongClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.activities.MapsActivity;
import com.noavaran.system.vira.baryab.activities.controllers.MapController;
import com.noavaran.system.vira.baryab.customviews.CustomAutoCompleteTextView;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.dialogs.ConfirmDialog;
import com.noavaran.system.vira.baryab.fragments.DestinationDeterminationFragment;
import com.noavaran.system.vira.baryab.fragments.OriginDeterminationFragment;
import com.noavaran.system.vira.baryab.info.LatLngInfo;
import com.noavaran.system.vira.baryab.info.PlacesInfo;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.utils.ImageUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import ir.map.sdk_map.maps.MapView;
import ir.map.sdk_map.MapirStyle;

import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ROTATION_ALIGNMENT_VIEWPORT;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String                            KEY_ORIGIN = "origin";
    private final static String                            KEY_DESTINATION = "destination";
    private              MapController                     controller;
    private              RelativeLayout                    rlContainer;
    private              CustomAutoCompleteTextView        etSearchPlaces;
    private              ImageView                         ivPlaceChooserMarker;
    private              CustomTextView                    btnMyLocation;
    private              OriginDeterminationFragment       originDeterminationFragment;
    private              DestinationDeterminationFragment  destinationDeterminationFragment;
    private              boolean                           isMyLocationEnabled = false;
    private              String                            strPlacesInfo;


    MapboxMap map;
    Style mapStyle;
    MapView mapView;
    LatLng samplePoint = new LatLng(35.732521, 51.422575);
    int sampleZoom = 14;
    String map_location_value = "";
    private String marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            map_location_value = (String) bundle.get("map_value");
        }


        Log.e("map_location_value", map_location_value);


        findViews();
        initComponents();



        clickListener();
        mapView.onCreate(savedInstanceState);


    }

    private void clickListener() {
        this.btnMyLocation.setOnClickListener(this);
       // this.ivPlaceChooserMarker.setOnClickListener(this);


    }


    private void findViews() {
        this.mapView = findViewById(R.id.map_view);
        this.rlContainer = findViewById(R.id.acMaps_rlContainer);
        this.etSearchPlaces = findViewById(R.id.acMaps_etSearchPlaces);
        this.ivPlaceChooserMarker = findViewById(R.id.acMaps_ivPlaceChooserMarker);
        this.btnMyLocation = findViewById(R.id.acMaps_btnMyLocation);

    }

    void addGeoJsonSourceURL() {
        try {

            URI            geoJsonUrl = new URI("asset://sample_data.geojson");
            GeoJsonSource geoJsonSource = new GeoJsonSource("sample_image_id", geoJsonUrl);

            mapStyle.addSource(geoJsonSource);
        } catch (URISyntaxException ignored) { }
    }


    void addSymbolLayer() {
        Bitmap        icon       = null;

        if (map_location_value.equals("origin")) {
            icon       = BitmapFactory.decodeResource(getResources(), R.drawable.ic_origin);

        } else {
            icon       = BitmapFactory.decodeResource(getResources(), R.drawable.ic_destination);

        }


        mapStyle.addImage("sample_image_id", icon);
        SymbolLayer  symbolLayer = new SymbolLayer("sample_symbol_layer_id", "sample_source_id");
        symbolLayer.setProperties(PropertyFactory.iconImage("sample_image_id"));
        mapStyle.addLayer(symbolLayer);
    }

    private void initComponents() {

        if (map_location_value.equals("origin")) {
            ivPlaceChooserMarker.setImageDrawable(getResources().getDrawable(R.drawable.ic_origin));
        } else {
            ivPlaceChooserMarker.setImageDrawable(getResources().getDrawable(R.drawable.ic_destination));
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                map = mapboxMap;
                map.setStyle(new Style.Builder().fromUri(MapirStyle.MAIN_MOBILE_VECTOR_STYLE), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        mapStyle = style;
                        zoomToSpecificLocation();


                        ivPlaceChooserMarker.setOnClickListener(new OnSingleClickListener() {
                            @Override
                            public void onSingleClick(View v) {
                                addSymbolLayer();
                               // addGeoJsonSourceURL();
                               // finish();
                            }
                        });


                    }
                });

            }
        });


    }




    private void zoomToSpecificLocation() {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(samplePoint, sampleZoom));
    }

    private void enableLocationComponent() {


        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .elevation(5)
                    .accuracyAlpha(.6f)
                    .accuracyColor(Color.RED)
                    .build();


            LocationComponent locationComponent = map.getLocationComponent();

            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, mapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build();

            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
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
                        Toast.makeText(MapActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
            });
            permissionsManager.requestLocationPermissions(this);

        }


    }

    private void addSymbolToMap() {


        if (map_location_value.equals("origin")) {
            mapStyle.addImage("sample_image_id", getResources().getDrawable(R.drawable.ic_origin));
        } else if (map_location_value.equals("destination")) {
            mapStyle.addImage("sample_image_id", getResources().getDrawable(R.drawable.ic_destination));
        } else {

            mapStyle.addImage("sample_image_id", getResources().getDrawable(R.drawable.mapbox_marker_icon_default));
        }


// create symbol manager object
        SymbolManager sampleSymbolManager = new SymbolManager(mapView, map, mapStyle);
        sampleSymbolManager.addClickListener(new OnSymbolClickListener() {
            @Override
            public void onAnnotationClick(Symbol symbol) {
                Toast.makeText(MapActivity.this, "This is CLICK_EVENT", Toast.LENGTH_SHORT).show();
            }
        });
        sampleSymbolManager.addLongClickListener(new OnSymbolLongClickListener() {
            @Override
            public void onAnnotationLongClick(Symbol symbol) {
                Toast.makeText(MapActivity.this, "This is LONG_CLICK_EVENT", Toast.LENGTH_SHORT).show();
            }
        });
// set non-data-driven properties, such as:
        sampleSymbolManager.setIconAllowOverlap(true);
        sampleSymbolManager.setIconRotationAlignment(ICON_ROTATION_ALIGNMENT_VIEWPORT);
// Add symbol at specified lat/lon
        SymbolOptions sampleSymbolOptions = new SymbolOptions();
        sampleSymbolOptions.withLatLng(samplePoint);
        sampleSymbolOptions.withIconImage("sample_image_id");
        sampleSymbolOptions.withIconSize(1.0f);
// save created Symbol Object for later access
        Symbol sampleSymbol = sampleSymbolManager.create(sampleSymbolOptions);
    }


    @Override
    public void onClick(View view) {
        if (view == btnMyLocation) {
            enableLocationComponent();

        }


        if (view == ivPlaceChooserMarker) {


        }
    }
}

