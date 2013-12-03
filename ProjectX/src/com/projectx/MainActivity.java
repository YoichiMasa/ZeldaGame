package com.projectx;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.RectShape;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;

public class MainActivity extends FragmentActivity implements 
OnMapClickListener, OnMyLocationButtonClickListener, ConnectionCallbacks,
OnConnectionFailedListener,
LocationListener
{
	
	private GoogleMap mMap;
	private LocationClient mLocationClient;
	private TextView mMessageView;
	
	 // These settings are the same as the settings for the map. They will in fact give you updates
    // at the maximal rates currently possible.
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//mMessageView = (TextView) findViewById(R.id.message_text);
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
	    mLocationClient.connect();
	}
	
	private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(this);
                setUpMap();
            }
        }
        }
        private void setUpMap() {
		mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		//TileProvider tileProvider = TileProviderFactory.getTileProvider(); 
		//mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider)); 
		 // Get LocationManager object from System Service LOCATION_SERVICE
	    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

	    // Create a criteria object to retrieve provider
	    Criteria criteria = new Criteria();

	    // Get the name of the best provider
	    String provider = locationManager.getBestProvider(criteria, true);

	    // Get Current Location
	    Location myLocation = locationManager.getLastKnownLocation(provider);

	    // Get latitude of the current location
	    double latitude = myLocation.getLatitude();

	    // Get longitude of the current location
	    double longitude = myLocation.getLongitude();

	    // Create a LatLng object for the current location
	    LatLng latLng = new LatLng(latitude, longitude);
	    
		CameraPosition cameraPosition = new CameraPosition.Builder()
	    .target(latLng)      // Sets the center of the map
	    .zoom(mMap.getMaxZoomLevel())                   // Sets the zoom
	    //.bearing(90)                // Sets the orientation of the camera to east
	    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
	    .build();                   // Creates a CameraPosition from the builder
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		
	}
        
        
        

		private void setUpLocationClientIfNeeded() 
		{
            if (mLocationClient == null) {
                mLocationClient = new LocationClient(
                        getApplicationContext(),
                        this,  // ConnectionCallbacks
                        this); // OnConnectionFailedListener
            }
        }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onMapClick(LatLng position) 
	{
		// TODO Auto-generated method stub
		mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
	}
	
    public void showMyLocation(View view) 
    {
        if (mLocationClient != null && mLocationClient.isConnected()) {
            String msg = "Location = " + mLocationClient.getLastLocation();
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

	@Override
	public void onLocationChanged(Location location) 
	{
		mMessageView.setText("Location = " + location);
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) 
	{
		// Do nothing
		
	}

	@Override
	public void onConnected(Bundle arg0) 
	{
		mLocationClient.requestLocationUpdates(
                REQUEST,
                this);  // LocationListener
		
	}

	@Override
	public void onDisconnected() 
	{
		// Do nothing
		
	}

	@Override
	public boolean onMyLocationButtonClick() 
	{
		Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
	}

	

	

}
