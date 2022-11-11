package com.company.opsc_south_side_application;

import static android.content.ContentValues.TAG;

import static com.company.opsc_south_side_application.BuildConfig.GOOGLE_KEY;
import static com.company.opsc_south_side_application.navigationFragments.NavigationFragment.isFavouritePlace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;

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
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.company.opsc_south_side_application.directionsModel.Legs;
import com.company.opsc_south_side_application.directionsModel.Root;
import com.company.opsc_south_side_application.directionsModel.Routes;
import com.company.opsc_south_side_application.directionsModel.step.Steps;
import com.company.opsc_south_side_application.models.PlacesModel;
import com.company.opsc_south_side_application.models.User;
import com.company.opsc_south_side_application.navigationFragments.NavigationFragment;
import com.company.opsc_south_side_application.navigationFragments.WhereNavigationFragment;
import com.company.opsc_south_side_application.placesModel.Features;
import com.company.opsc_south_side_application.profileFragments.profileFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
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
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mMapView;
    public static GoogleMap mGoogleMap;
    public static Context context;
    public static String fragmentType;
    public static FloatingActionButton profileButton;
    FusedLocationProviderClient fusedLocationProviderClient;
    public static PlacesModel place;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private final int requestCode = 2;
    private final String[] reqPermissions = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION };
   // private List<Routes> routes;
    public static LatLng origin;
    public static LatLng dest;
    public static String metric;
    User user;
    public static String landmarkPreference;
    private List<Routes> routes;
    String firebaseUser;
    FirebaseAuth firebaseAuth;
    public static DatabaseReference databaseReference;
    public static Button buttonWhere;
    public static  Hashtable<Marker, PlacesModel> markers = new Hashtable<>();
    public static Hashtable<Marker,String> listener = new Hashtable<Marker,String>();
    public static ArrayList<PlacesModel> placesModelsList = new ArrayList<>();
    public static ArrayList<PlacesModel> favouritePlacesModelsList = new ArrayList<>();
    public static NavigationFragment dialogFragment = new NavigationFragment();
    public WhereNavigationFragment whereNavFragment;
    public static FragmentContainerView containerView;
    public static String title;
    Integer points;

    AutocompleteSupportFragment autocompleteFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        //dialogFragment = new NavigationFragment();
        whereNavFragment = new WhereNavigationFragment();

        Places.initialize(getApplicationContext(), GOOGLE_KEY);
        //Firebase implementation later
        firebaseAuth = FirebaseAuth.getInstance();
        //signInWithAccountTest("ntokozomweli001@gmail.com","ntokozo@1");
        //signInWithAccountTest("ntokozomweli001@gmail.com","ntokozo@1");

        firebaseUser = firebaseAuth.getUid();
         //firebaseUser = "Vcz171LR1EfrkfpNBkxz6wzp6fF3";
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser);





        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);
        //button = findViewById(R.id.floatingActionButtonProfile);
        buttonWhere = findViewById(R.id.buttonWhereNavigation);
        containerView = findViewById(R.id.fragmentContainerViewNav);
        profileButton = findViewById(R.id.floatingActionButtonProfile);



        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileFragment fragment = new profileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewWhere, fragment).setReorderingAllowed(true).commit();
                buttonWhere.setVisibility(View.INVISIBLE);
                profileButton.setVisibility(View.INVISIBLE);
            }
        });
        buttonWhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //whereNavFragment.show(getSupportFragmentManager(), "My  Fragment");
                buttonWhere.setVisibility(View.INVISIBLE);
                profileButton.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewWhere, whereNavFragment).setReorderingAllowed(true).commit();
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



    //Method to load firebase data from the database with user and their settings and favourite places
    //https://www.geeksforgeeks.org/how-to-populate-recyclerview-with-firebase-data-using-firebaseui-in-android-studio/ GeeksforGeeks
    public void loadFirebaseData(){
        DatabaseReference placesListFirebase = databaseReference.child("FavouritePlaces");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                metric = user.getDistanceUnit();
                landmarkPreference = user.getLandmarkPreference();
                points = user.getPoints();
                getPlacesUrl();
                getPointsURL();
                Log.d("Firebase data details", "metric : " + metric +", landmarkpreference : " + landmarkPreference);
                placesListFirebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        favouritePlacesModelsList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            PlacesModel placesModel = dataSnapshot.getValue(PlacesModel.class);
                            favouritePlacesModelsList.add(placesModel);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this,"Database reading failed for favourite places",Toast.LENGTH_LONG).show();
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Database reading failed for user settings",Toast.LENGTH_LONG).show();
            }
        });

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
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        //mGoogleMap.getUiSettings().setCompassEnabled(true);
        getLastLocation();
        //Method to set click listener for marker
        //https://www.digitalocean.com/community/tutorials/android-passing-data-between-fragments
        //https://stackoverflow.com/questions/72900044/helpattempt-to-invoke-virtual-method-void-android-widget-textview-settextjav itay bielski
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                PlacesModel placeMarker = markers.get(marker);
                String markerType = listener.get(marker);
                if(markerType.equals("PlaceMarkerType")) {
                    isFavouritePlace = false;
                    place = new PlacesModel();
                    place.setLatitude(marker.getPosition().latitude);
                    place.setLongitude(marker.getPosition().longitude);
                    place.setPlaceType(placeMarker.getPlaceType());
                    place.setName(placeMarker.getName());
                    place.setAddress(placeMarker.getAddress());
                    dest = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                    dialogFragment=new NavigationFragment();

                    //Log.d("placeTitle",title);

                    //containerView.setVisibility(View.VISIBLE);
                    buttonWhere.setVisibility(View.INVISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewNav, dialogFragment).setReorderingAllowed(true).commit();
                    for(PlacesModel place1 : favouritePlacesModelsList){
                        if(place1.getLatitude().equals(marker.getPosition().latitude) && place1.getLongitude().equals(marker.getPosition().longitude)){
                            isFavouritePlace = true;
                            place.setPlaceID(place1.getPlaceID());
                        }
                    }
                    //dialogFragment.show(getSupportFragmentManager(), "My  Fragment");
                    //dialogFragment.destLocation.setText(title);
                    //dialogFragment.setUpFragmentUiAddress(titleList.get(marker));

                    // getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new NavigationFragment()).setReorderingAllowed(true).commit();
                }else if (markerType.equals("directionMarker")){

                }
                return false;
            }
        });
    }

    //Method to decode the polylines
    //https://abhiandroid.com/programming/googlemaps
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

    //https://www.geeksforgeeks.org/how-to-get-user-location-in-android/
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


    //https://www.geeksforgeeks.org/how-to-get-user-location-in-android/
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
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                            loadFirebaseData();
                            //loadFirebaseData();
                            //getPlacesUrl();
                            //signInWithAccountTest("ntokozomweli001@gmail.com","ntokozo@1");

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

    //https://www.geeksforgeeks.org/how-to-get-user-location-in-android/
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        com.google.android.gms.location.LocationRequest mLocationRequest = new com.google.android.gms.location.LocationRequest();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mLocationRequest.setPriority(LocationRequest.QUALITY_HIGH_ACCURACY);
        }
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    //Location callback
    //https://www.geeksforgeeks.org/how-to-get-user-location-in-android/
    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            double lat = mLastLocation.getLatitude();
            double lon = mLastLocation.getLongitude();
            Log.d("LocationM","lat : " + lat + " lon : " + lon);
            origin = new LatLng(lat,lon);
            loadFirebaseData();
            //loadFirebaseData();
            //getPlacesUrl();
            //signInWithAccountTest("ntokozomweli001@gmail.com","ntokozo@1");
        }
    };

    // method to check for permissions
    //https://www.geeksforgeeks.org/how-to-get-user-location-in-android/
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    //https://www.geeksforgeeks.org/how-to-get-user-location-in-android/
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
    }

    // method to check
    // if location is enabled
    //https://www.geeksforgeeks.org/how-to-get-user-location-in-android/
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

    //Method to construct the directions url for the Google directions API
    //https://abhiandroid.com/programming/googlemaps
    public static String getDirectionsUrl(LatLng origin, LatLng dest, String mode, String distanceUnit) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String unitType = "units=" + distanceUnit;
        if(mode == null){
            mode = "mode=driving";
        }
        //String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode +"&" +unitType ;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyBui6YhBrtJz5AyFLCOPTfgZlnV4NPpFRM";

        Log.d(TAG, url);
        return url;
    }

    //A method to send a request and retrieve the response from the URL sent.
    //
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

    public void impelementFetchDirection(URL url){
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

    //Method to get list of places from Geoapify service place API
    //https://www.geoapify.com/places-api
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
                url = "https://api.geoapify.com/v2/places?categories=commercial.supermarket,entertainment.museum,leisure.park,service.vehicle.fuel,catering.restaurant&filter=circle:" + origin.longitude + "," + origin.latitude +",5000&bias=proximity:" + origin.longitude + "," + origin.latitude +"&limit=50&&apiKey=9c7ca58b70be4b988353ab35df122e0b";
                break;
        }

        return url;
    }

    //Method to get list of places from Geoapify service place API
    //https://www.geoapify.com/places-api
    private String downloadPlacesUrlPoints(){
        String url;
        url = "https://api.geoapify.com/v2/places?categories=commercial.supermarket,entertainment.museum,leisure.park,service.vehicle.fuel,catering.restaurant&filter=circle:" + origin.longitude + "," + origin.latitude +",50&bias=proximity:" + origin.longitude + "," + origin.latitude +"&limit=50&&apiKey=9c7ca58b70be4b988353ab35df122e0b";


        return url;
    }
    //method to get the places URL
    private  void getPlacesUrl(){
        Gson gson = new Gson();
        URL url1;


        try {
            String url = "";
            url = downloadPlacesUrl(landmarkPreference);
            //url = "https://api.geoapify.com/v2/places?categories=commercial.supermarket&filter=circle:" + origin.longitude + "," + origin.latitude +",5000&bias=proximity:28.1289133,-25.9949025&limit=30&&apiKey=9c7ca58b70be4b988353ab35df122e0b";
            url1 = new URL(url);
            new fetchPlacesData().execute(url1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getPointsURL(){
        try {
            String url = "";
            url = downloadPlacesUrlPoints();
            //url = "https://api.geoapify.com/v2/places?categories=commercial.supermarket&filter=circle:" + origin.longitude + "," + origin.latitude +",5000&bias=proximity:28.1289133,-25.9949025&limit=30&&apiKey=9c7ca58b70be4b988353ab35df122e0b";
            URL url1 = new URL(url);
            new fetchPlacesPointsData().execute(url1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //A async Task method to fetch places data and consume it via GSON
    //https://www.digitalocean.com/community/tutorials/android-google-map-drawing-route-two-points
    public class fetchPlacesData extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String data = null;
            try {
                data = getResponseFromHttpUrl(url);

            } catch (Exception e) {
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


        public class fetchPlacesPointsData extends AsyncTask<URL, Void, String> {

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
                    consumeGsonPlacesPoints(propertiesData);
                }else {
                    //Toast.makeText(getApplicationContext(), "",)
                }
                super.onPostExecute(propertiesData);
            }
        }
    private void consumeGsonPlacesPoints(String propertiesData) {
        Gson gson = new Gson();
        Marker marker;
        com.company.opsc_south_side_application.placesModel.Root placesData = gson.fromJson(propertiesData, com.company.opsc_south_side_application.placesModel.Root.class);
        placesModelsList.clear();
        for (Features feature : placesData.getFeatures()) {
            double latitude = feature.getProperties().getLat();
            double longitude = feature.getProperties().getLon();

            String mode = " ";
            List<String> modes = feature.getProperties().getCategories();

            if (modes.contains("service.vehicle.fuel")) {
                mode = "Gas Station";
                Toast.makeText(getApplicationContext(),"5 points rewarded", Toast.LENGTH_SHORT).show();
                points = points +5;
            } else if (modes.contains("commercial.supermarket")) {
                mode = "Supermarket";
                Toast.makeText(getApplicationContext(),"10 points rewarded", Toast.LENGTH_SHORT).show();
                points = points +10;
            } else if (modes.contains("entertainment.museum")) {
                mode = "Museum";
                Toast.makeText(getApplicationContext(),"25 points rewarded", Toast.LENGTH_SHORT).show();
                points = points +25;
            } else if (modes.contains("leisure.park")) {
                mode = "Park";
                Toast.makeText(getApplicationContext(),"20 points rewarded", Toast.LENGTH_SHORT).show();
                points = points +20;
            } else if (modes.contains("catering.restaurant")) {
                mode = "Restaurant";
                Toast.makeText(getApplicationContext(),"15 points rewarded", Toast.LENGTH_SHORT).show();
                points = points +15;
            }
        }
    }

    //Method to consume GSON places and place markers with them.
    private void consumeGsonPlaces(String propertiesData) {
        Gson gson = new Gson();
        Marker marker;
        com.company.opsc_south_side_application.placesModel.Root placesData = gson.fromJson(propertiesData, com.company.opsc_south_side_application.placesModel.Root.class);
        placesModelsList.clear();
        for(Features feature : placesData.getFeatures()){
            double latitude = feature.getProperties().getLat();
            double longitude = feature.getProperties().getLon();

            String mode = " ";
            List<String> modes = feature.getProperties().getCategories();

            if(modes.contains("service.vehicle.fuel")){
                mode = "Gas Station";
            }else if(modes.contains("commercial.supermarket")){
                mode = "Supermarket";
            }else if(modes.contains("entertainment.museum")){
                mode = "Museum";
            }else if(modes.contains("leisure.park")){
                mode = "Park";
            }else if(modes.contains("catering.restaurant")){
                mode = "Restaurant";
            }
            PlacesModel place = new PlacesModel();
            switch(mode){
                case "Gas Station":
                    Log.d("Place Name","Gas Station : " + feature.getProperties().getName());
                    marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("Gas Station : " + feature.getProperties().getName())
                            .icon(bitmapDescriptorFromVector(context, R.drawable.baseline_local_gas_station_black_24dp))

                    );
                    listener.put(marker,"PlaceMarkerType");
                    place.setName("Gas Station : " + feature.getProperties().getName());
                    place.setAddress(feature.getProperties().getAddress_line2());
                    place.setPlaceType(mode);
                    place.setLatitude(latitude);
                    place.setLongitude(longitude);
                    markers.put(marker, place);
                    placesModelsList.add(place);
                    break;
                case "Restaurant":
                    marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("Restaurant : " + feature.getProperties().getName())
                            .icon(bitmapDescriptorFromVector(context, R.drawable.ic_baseline_restaurant))
                    );
                    place.setName("Restaurant : " + feature.getProperties().getName());
                    place.setAddress(feature.getProperties().getAddress_line2());
                    place.setPlaceType(mode);
                    place.setLatitude(latitude);
                    place.setLongitude(longitude);
                    markers.put(marker, place);
                    placesModelsList.add(place);
                    listener.put(marker,"PlaceMarkerType");
                    break;
                case "Museum":
                    marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("Museum : " + feature.getProperties().getName())
                            .icon(bitmapDescriptorFromVector(context, R.drawable.baseline_museum_black_24dp))

                    );
                    place.setName("Museum : " + feature.getProperties().getName());
                    place.setAddress(feature.getProperties().getAddress_line2());
                    place.setPlaceType(mode);
                    place.setLatitude(latitude);
                    place.setLongitude(longitude);
                    markers.put(marker, place);
                    placesModelsList.add(place);
                    listener.put(marker,"PlaceMarkerType");
                    break;
                case "Park":
                    marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("Park : " + feature.getProperties().getName())
                            .icon(bitmapDescriptorFromVector(context, R.drawable.baseline_park_black_24dp))
                    );
                    place.setName("Park : " + feature.getProperties().getName());
                    place.setAddress(feature.getProperties().getAddress_line2());
                    place.setPlaceType(mode);
                    place.setLatitude(latitude);
                    place.setLongitude(longitude);
                    markers.put(marker, place);
                    placesModelsList.add(place);
                    listener.put(marker,"PlaceMarkerType");
                    break;
                case "Supermarket":
                    marker =mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("SuperMarket : " + feature.getProperties().getName())
                            .icon(bitmapDescriptorFromVector(context, R.drawable.supermarket_icon_32px))
                    );
                    place.setName("SuperMarket : " + feature.getProperties().getName());
                    place.setAddress(feature.getProperties().getAddress_line2());
                    place.setPlaceType(mode);
                    place.setLatitude(latitude);
                    place.setLongitude(longitude);
                    markers.put(marker, place);
                    placesModelsList.add(place);
                    listener.put(marker,"PlaceMarkerType");
                    break;

            }
        }
    }


    //Consume GSON of the routes for the directions data
    protected void consumeGson(String directionsJSON) {
        if (directionsJSON != null) {

            Gson gson = new Gson();
            Root routesData = gson.fromJson(directionsJSON, Root.class);
            routes = routesData.getRoutes();
        }else{

        }
    }

    //A class that parses the routes data to draw routes between two points.
    //https://www.digitalocean.com/community/tutorials/android-google-map-drawing-route-two-points
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
            if(fragmentType.equals("Main Nav")){
                dialogFragment.setUpFragmentUi(leg.getDistance().getText(),leg.getDuration().getText());
            }else if(fragmentType.equals("Where Nav")){
                Toast.makeText(context,"Duration : " + leg.getDuration().getText() +" Distance : " + leg.getDistance().getText(),Toast.LENGTH_LONG).show();
            }
            //dialogFragment.setUpFragmentUi(leg.getDistance().getText(),leg.getDuration().getText());
            Marker endMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(leg.getEnd_location().getLat(), leg.getEnd_location().getLng()))
                    .title("End Location"));

            listener.put(endMarker,"directionMarker");
            Marker startMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(leg.getStart_location().getLat(), leg.getStart_location().getLng()))
                    .title("Start Location ")
            );
            listener.put(startMarker,"directionMarker");

            List<LatLng> stepList = new ArrayList<>();

            PolylineOptions options = new PolylineOptions()
                    .width(25)
                    .color(Color.BLUE)
                    .geodesic(true)
                    .clickable(true)
                    .visible(true);

            List<PatternItem> pattern;

            pattern = Collections.singletonList(
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

    public void clearUI() {

        mGoogleMap.clear();
        getPlacesUrl();

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