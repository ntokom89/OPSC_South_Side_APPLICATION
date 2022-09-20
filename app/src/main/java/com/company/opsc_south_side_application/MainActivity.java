package com.company.opsc_south_side_application;

import static android.content.ContentValues.TAG;

import static com.company.opsc_south_side_application.BuildConfig.GOOGLE_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.company.opsc_south_side_application.directionsModel.Legs;
import com.company.opsc_south_side_application.directionsModel.Root;
import com.company.opsc_south_side_application.directionsModel.Routes;
import com.company.opsc_south_side_application.directionsModel.step.Steps;
import com.company.opsc_south_side_application.placesModel.Features;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mMapView;
    public static GoogleMap mGoogleMap;
    LocationRequest locationRequest;
    public static Context context;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private final int requestCode = 2;
    private final String[] reqPermissions = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION };
   // private List<Routes> routes;
    public static LatLng origin;
    public static LatLng dest;
    private List<Routes> routes;
    FloatingActionButton button;
    Boolean choosenMode = false;
    String modeName;
    private Map<Marker, Map<String, Object>> markers = new HashMap<>();
    private Map<String, Object> dataModel = new HashMap<>();
    public static NavigationFragment dialogFragment;

    AutocompleteSupportFragment autocompleteFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        dialogFragment = new NavigationFragment();
        Places.initialize(getApplicationContext(), GOOGLE_KEY);
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);
        button = findViewById(R.id.floatingActionButton);
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autoCompleteDestination);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {

                dest = place.getLatLng();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL urlConnection;
                String url;
                try {
                    url = getDirectionsUrl(origin, dest, null);
                    urlConnection = new URL(url);
                    new fetchDirectionsData().execute(urlConnection);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // method to get the location

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(mapViewBundle);//

        mMapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        // Add a marker in Sydney and move the camera
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this, reqPermissions, requestCode);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        //googleMap.getUiSettings().isMyLocationButtonEnabled();
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        getLastLocation();
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Map dataModel = (Map)markers.get(marker);
                dest = new LatLng((double)dataModel.get("latitude"),(double)dataModel.get("longitude"));
                //dialogFragment=new NavigationFragment();
                dialogFragment.show(getSupportFragmentManager(),"My  Fragment");
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new NavigationFragment()).setReorderingAllowed(true).commit();

                return false;
            }
        });
    }

    private List<LatLng> decode(String points) {

        int len = points.length();

        final List<LatLng> path = new ArrayList<>(len / 2);
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = points.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = points.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }

        return path;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Location permission was granted. This would have been triggered in response to failing to start the
            // LocationDisplay, so try starting this again.
        } else {
            // If permission was denied, show toast to inform user what was chosen. If LocationDisplay is started again,
            // request permission UX will be shown again, option should be shown to allow never showing the UX again.
            // Alternative would be to disable functionality so request is not shown again.
            Toast.makeText(this, "R.string.location_permission_denied", Toast.LENGTH_SHORT).show();

            // Update UI to reflect that the location display did not actually start
        }
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();
                            Log.d("LocationM","lat : " + lat + " lon : " + lon);
                            origin = new LatLng(lat,lon);
                            getPlacesUrl();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        com.google.android.gms.location.LocationRequest mLocationRequest = new com.google.android.gms.location.LocationRequest();
        mLocationRequest.setPriority(LocationRequest.QUALITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            double lat = mLastLocation.getLatitude();
            double lon = mLastLocation.getLongitude();
            Log.d("LocationM","lat : " + lat + " lon : " + lon);
            origin = new LatLng(lat,lon);
            getPlacesUrl();
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static String getDirectionsUrl(LatLng origin, LatLng dest, String mode) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        if(mode == null){
            mode = "mode=driving";
        }
        //String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyBui6YhBrtJz5AyFLCOPTfgZlnV4NPpFRM";

        Log.d(TAG, url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection =
                (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("//A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
    public void impelemntFetchDirection(URL url){
        new fetchDirectionsData().execute(url);
    }

    public  class fetchDirectionsData extends AsyncTask<URL,Void,String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String data = null;
            try{
                data = getResponseFromHttpUrl(url);

            }catch(Exception e){
                e.printStackTrace();
            }
            return data;
        }
        @Override
        protected void onPostExecute(String data) {
            if (data != null) {
                //TextView.setText(data);
                Log.d("LocationM","data : " + data );
                consumeGson(data);

                ParserTask parserTask = new ParserTask();


                parserTask.execute();


            }
            super.onPostExecute(data);
        }
    }

    private String downloadPlacesUrl(String category){
        String url;
        switch (category){
            case "Restaurant":
                url = "https://api.geoapify.com/v2/places?categories=catering.restaurant&filter=circle:" + origin.longitude + "," + origin.latitude +",5000&bias=proximity:" + origin.longitude + "," + origin.latitude +"&limit=30&&apiKey=9c7ca58b70be4b988353ab35df122e0b";
                break;
            case "Gas Station":
                url = "https://api.geoapify.com/v2/places?categories=service.vehicle.fuel&filter=circle:" + origin.longitude + "," + origin.latitude +",5000&bias=proximity:" + origin.longitude + "," + origin.latitude +"&limit=30&&apiKey=9c7ca58b70be4b988353ab35df122e0b";
                break;
            case "Museum":
                url = "https://api.geoapify.com/v2/places?categories=entertainment.museum&filter=circle:" + origin.longitude + "," + origin.latitude +",5000&bias=proximity:" + origin.longitude + "," + origin.latitude +"&limit=30&&apiKey=9c7ca58b70be4b988353ab35df122e0b";
                break;
            case "Park":
                url = "https://api.geoapify.com/v2/places?categories=leisure.park&filter=circle:" + origin.longitude + "," + origin.latitude +",5000&bias=proximity:" + origin.longitude + "," + origin.latitude +"&limit=30&&apiKey=9c7ca58b70be4b988353ab35df122e0b";
                break;
            case "Supermarket":
                url = "https://api.geoapify.com/v2/places?categories=commercial.supermarket&filter=circle:" + origin.longitude + "," + origin.latitude +",5000&bias=proximity:" + origin.longitude + "," + origin.latitude +"&limit=30&&apiKey=9c7ca58b70be4b988353ab35df122e0b";
                break;
            default :
                url = "https://api.geoapify.com/v2/places?categories=commercial.supermarket&filter=circle:" + origin.longitude + "," + origin.latitude +",5000&bias=proximity:" + origin.longitude + "," + origin.latitude +"&limit=30&&apiKey=9c7ca58b70be4b988353ab35df122e0b";
                break;
        }

        return url;
    }

    private  void getPlacesUrl(){
        Gson gson = new Gson();
        URL url1;


        try {
            String url = "";
            url = downloadPlacesUrl("Gas Station");
            //url = "https://api.geoapify.com/v2/places?categories=commercial.supermarket&filter=circle:" + origin.longitude + "," + origin.latitude +",5000&bias=proximity:28.1289133,-25.9949025&limit=30&&apiKey=9c7ca58b70be4b988353ab35df122e0b";
            url1 = new URL(url);
            new fetchPlacesData().execute(url1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class fetchPlacesData extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String data = null;
            try{
                data = getResponseFromHttpUrl(url);

            }catch(Exception e){
                e.printStackTrace();
            }
            return data;
        }
        @Override
        protected void onPostExecute(String propertiesData) {
            if (propertiesData != null) {
                //TextView.setText(data);
                consumeGsonPlaces(propertiesData);
            }
            super.onPostExecute(propertiesData);
        }
    }

    private void consumeGsonPlaces(String propertiesData) {
        Gson gson = new Gson();
        Marker marker;
        com.company.opsc_south_side_application.placesModel.Root placesData = gson.fromJson(propertiesData, com.company.opsc_south_side_application.placesModel.Root.class);

        for(Features feature : placesData.getFeatures()){
            double latitude = feature.getProperties().getLat();
            double longitude = feature.getProperties().getLon();

            String mode = "Gas Station";
            switch(mode){
                case "Gas Station":
                    marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("Gas Station : " + feature.getProperties().getName())
                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.baseline_local_gas_station_black_24dp))

                    );
                    dataModel.put("title", feature.getProperties().getName());
                    dataModel.put("latitude", latitude);
                    dataModel.put("longitude", longitude);
                    markers.put(marker, dataModel);
                    break;
                case "Restaurant":
                    marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("Restaurant : " + feature.getProperties().getName())
                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.supermarket_icon_32px))
                    );
                    dataModel.put("title", feature.getProperties().getName());
                    dataModel.put("latitude", latitude);
                    dataModel.put("longitude", longitude);
                    markers.put(marker, dataModel);
                    break;
                case "Museum":
                    marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("Museum : " + feature.getProperties().getName())
                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.baseline_museum_black_24dp))
                    );
                    dataModel.put("title", feature.getProperties().getName());
                    dataModel.put("latitude", latitude);
                    dataModel.put("longitude", longitude);
                    markers.put(marker, dataModel);
                    break;
                case "Park":
                    marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("Park : " + feature.getProperties().getName())
                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.baseline_park_black_24dp))
                    );
                    dataModel.put("title", feature.getProperties().getName());
                    dataModel.put("latitude", latitude);
                    dataModel.put("longitude", longitude);
                    markers.put(marker, dataModel);
                    break;
                case "Supermarket":
                    marker =mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("SuperMarket : " + feature.getProperties().getName())
                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.supermarket_icon_32px))
                    );
                    dataModel.put("title", feature.getProperties().getName());
                    dataModel.put("latitude", latitude);
                    dataModel.put("longitude", longitude);
                    markers.put(marker, dataModel);
                    break;
                default:
                    marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("SuperMarket : " + feature.getProperties().getName())
                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.supermarket_icon_32px))
                    );
                    dataModel.put("title", feature.getProperties().getName());
                    dataModel.put("latitude", latitude);
                    dataModel.put("longitude", longitude);
                    markers.put(marker, dataModel);
                    break;

            }
        }
    }


    protected void consumeGson(String directionsJSON) {
        if (directionsJSON != null) {

            Gson gson = new Gson();
            Root weatherData = gson.fromJson(directionsJSON, Root.class);
            routes = weatherData.getRoutes();
        }else{

        }
    }

    private class ParserTask extends AsyncTask<Void, Integer,List<Routes>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<Routes> doInBackground(Void... voids) {

            return routes;
        }

        @Override
        protected void onPostExecute(List<Routes> routes) {
            clearUI();
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            points = new ArrayList();
            lineOptions = new PolylineOptions();

            Routes route = routes.get(0);
            Legs leg = route.getLegs().get(0);

            Log.d("LocationM","data end: " + leg.getEnd_location().getLat() + "Long : " + leg.getEnd_location().getLng() );
            dialogFragment.setUpFragmentUi(leg.getDistance().getText(),leg.getDuration().getText());
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(leg.getEnd_location().getLat(), leg.getEnd_location().getLng()))
                    .title("End Location"));

            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(leg.getStart_location().getLat(), leg.getStart_location().getLng()))
                    .title("Start Location")
            );

            List<LatLng> stepList = new ArrayList<>();

            PolylineOptions options = new PolylineOptions()
                    .width(25)
                    .color(Color.BLUE)
                    .geodesic(true)
                    .clickable(true)
                    .visible(true);

            List<PatternItem> pattern;

            pattern = Arrays.asList(
                    new Dash(30));


            options.pattern(pattern);

            for (Steps steps : leg.getSteps()) {
                Log.d("LocationM","data end: " + leg.getEnd_location().getLat() + "Long : " + leg.getEnd_location().getLng() );

                List<LatLng> decodedLatLng = decode(steps.getPolyline().getPoints());
                for (LatLng latLng : decodedLatLng) {
                    stepList.add(new LatLng(latLng.latitude, latLng.longitude));
                }
            }

            options.addAll(stepList);


            /*
            for (Routes route: routes) {

                for (Legs leg : route.getLegs()) {

                    double lat = leg.getEnd_location().getLat();
                    double lng = leg.getEnd_location().getLng();

                    double lat2 = leg.getStart_location().getLat();
                    double lng2 = leg.getEnd_location().getLng();
                    LatLng position = new LatLng(lat, lng);
                    LatLng position2 = new LatLng(lat2, lng2);
                    points.add(position);
                    points.add(position2);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.BLUE);
                lineOptions.geodesic(true);

            }

             */

// Drawing polyline in the Google Map for the i-th route
            Polyline polyline = mGoogleMap.addPolyline(options);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    private void clearUI() {

        mGoogleMap.clear();


    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}