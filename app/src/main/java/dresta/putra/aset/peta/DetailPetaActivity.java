package dresta.putra.aset.peta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.ClusterManager;
import com.vimalcvs.switchdn.DayNightSwitch;
import com.vimalcvs.switchdn.DayNightSwitchListener;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.utils.DirectionsJSONParser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DetailPetaActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private AdapterMarker adapter;
    private SupportMapFragment MvPetaAset;
    final String TAG = "tesdebug";
    private String id_aset = "";

    private GoogleMap mMap;
    public Location mLocation;
    GoogleApiClient mGoogleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    public static final int REQUEST_CODE_PERMISSIONS = 101;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private String pencarian = "";

    //    private NightModeButton nightModeButton;
    private DayNightSwitch dayNightSwitch;
    private boolean isNightMode = false;
    private PetaPojo petaPojos;
    private List<PetaPojo> markerPetaPojos = new ArrayList<>();
    private Marker mapMarker;
    private Polyline mPolyline;
    private FloatingActionButton FabDrawRoute;
    private LatLng mOrigin;
    private LatLng mDestination;
    private CardView CvFotoAset;

    TextView TxvNamaAset,TxvDeskripsiAset, TxvLuasAset, TxvAlamatAset, TxvHak;
    private AdapterFotoMarker adapterFotoMarker;
    private ViewPager viewPager;


    interface APIFragmentPeta {
        @FormUrlEncoded
        @POST("api/aset/detail_aset")
        Call<DetailAsetActivity.ResponseDetailAsetPojo> getDetailAset(@Field("id_aset") String id_aset);
    }

    private APIFragmentPeta apiFragmentPeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_peta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());
        id_aset = getIntent().getStringExtra("id_aset");
        FloatingActionButton fabCurrentLocation = findViewById(R.id.FabCurrentLocation);
//        FabDrawRoute = findViewById(R.id.FabDrawRoute);
        SearchView searchView = findViewById(R.id.mSearchView);
        CvFotoAset = findViewById(R.id.CvFotoAset);
        TxvNamaAset = findViewById(R.id.TxvNamaAset);
        TxvAlamatAset = findViewById(R.id.TxvAlamatAset);
        TxvLuasAset = findViewById(R.id.TxvLuasAset);
        TxvHak = findViewById(R.id.TxvHak);
        viewPager = findViewById(R.id.viewPager);


//        FabDrawRoute.setOnClickListener(v -> {
//            if (mMap != null) {
//                drawRoute();
//            }
//        });
        apiFragmentPeta = RetrofitClientInstance.getRetrofitInstance(DetailPetaActivity.this).create(APIFragmentPeta.class);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else {
            buildGoogleApiClient();
        }
        Button BtnDirection = findViewById(R.id.BtnDirection);
        BtnDirection.setOnClickListener(v->{
            Intent intent = new Intent(DetailPetaActivity.this,RuteActivity.class);
            intent.putExtra("id_aset", id_aset);
            startActivity(intent);
        });
        Geocoder geocoder = new Geocoder(DetailPetaActivity.this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String lokasi = query.toString();

                List<Address> addressList = null;
                if (lokasi != null && !lokasi.equals("")) {
                    try {
                        addressList = geocoder.getFromLocationName(lokasi, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    if (addressList != null && addressList.size() != 0) {
                        Address address = addressList.get(0);
                        if (mMap != null) {
                            mapMarker.remove();
                            // Set the color of the marker to green
                            BitmapDescriptor defaultMarker = BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ic_search_marker", 100, 100));
//                            BitmapDescriptor defaultMarker =
//                                    BitmapDescriptorFactory.fromResource(R.drawable.ic_search_marker);
                            LatLng listingPosition = new LatLng(address.getLatitude(), address.getLongitude());
                            mapMarker = mMap.addMarker(new MarkerOptions()
                                    .position(listingPosition)
                                    .title(query.toUpperCase())
                                    .snippet(address.getAddressLine(0))
                                    .icon(defaultMarker));
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            updateLocation(latLng);
                        }
                    }else if (addressList != null && addressList.size() == 0){
                        if (mMap != null) {
//                            mMap.clear();
                        }
                        Toast.makeText(DetailPetaActivity.this, "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        if (MvPetaAset == null) {
            MvPetaAset = SupportMapFragment.newInstance();
            MvPetaAset.getMapAsync(this);
        }
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        fabCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLocation();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        // R.id.map is a FrameLayout, not a Fragment
//        this.getChildFragmentManager()
        getSupportFragmentManager().beginTransaction().replace(R.id.map, MvPetaAset).commit();

        dayNightSwitch = findViewById(R.id.switch_item);
        dayNightSwitch.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean is_night) {
                if (mMap != null) {

                    if (is_night) {
                        isNightMode = true;
                        //Function to change color
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(DetailPetaActivity.this, R.raw.mapstyle_night));
                    } else {
                        isNightMode = false;
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(DetailPetaActivity.this, R.raw.mapstyle_standar));
                    }
                }
            }
        });
        settingsrequest();

        Button BtnStreetView = findViewById(R.id.BtnStreetView);
        BtnStreetView.setOnClickListener(v->{
            Intent intent = new Intent(DetailPetaActivity.this, StreetViewActivity.class);
            intent.putExtra("id_aset", id_aset);
            startActivity(intent);
        });

        //        slider

    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    public void initDataMarker() {
        if (mMap != null) {
            mapMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(0, 1))
                    .title("").visible(false)
                    .snippet(""));
        }
        Call<DetailAsetActivity.ResponseDetailAsetPojo> petaResponsePojoCall = apiFragmentPeta.getDetailAset(id_aset);
        petaResponsePojoCall.enqueue(new Callback<DetailAsetActivity.ResponseDetailAsetPojo>() {
            @Override
            public void onResponse(@NonNull Call<DetailAsetActivity.ResponseDetailAsetPojo> call, @NonNull Response<DetailAsetActivity.ResponseDetailAsetPojo> response) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        petaPojos = response.body().getData();

                        if (petaPojos.getFoto_aset() == null){
                            CvFotoAset.setVisibility(View.GONE);
                        }else{
                            adapterFotoMarker = new AdapterFotoMarker(petaPojos.getFoto_aset(), DetailPetaActivity.this);
                            viewPager.setAdapter(adapterFotoMarker);
                            CvFotoAset.setVisibility(View.VISIBLE);
                        }
                        clusterManager = new ClusterManager<>(DetailPetaActivity.this, mMap);
                        mMap.setOnCameraIdleListener(clusterManager);

                        addItems(petaPojos);
                        if (petaPojos != null) {
                            TxvNamaAset.setText(petaPojos.getNama_aset());
                            TxvAlamatAset.setText(petaPojos.getAlamat());
                            TxvLuasAset.setText(petaPojos.getLuas_tanah()+" M persegi");
                            TxvHak.setText(petaPojos.getJenis_hak());
                            LatLng position = new LatLng(Double.parseDouble(petaPojos.getLatitude()), Double.parseDouble(petaPojos.getLongitude()));
                            mDestination = position;
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 14);
                            mMap.animateCamera(cameraUpdate);
                        }else{
                            finish();
                        }

                    }
                }else{
                    Toast.makeText(DetailPetaActivity.this, "Detail Aset tidak ditemukan", Toast.LENGTH_LONG).show();
                    finish();

                }

            }

            @Override
            public void onFailure(@NonNull Call<DetailAsetActivity.ResponseDetailAsetPojo> call, @NonNull Throwable t) {
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        dayNightSwitch.setIsNight(isNightMode);
        initDataMarker();
//        nightModeButton.init(DetailPetaActivity.this);
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
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        if (ActivityCompat.checkSelfPermission(DetailPetaActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DetailPetaActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(DetailPetaActivity.this, R.raw.mapstyle_standar));

        connectClient(mMap);
        initDataMarker();
    }

    public void connectClient(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(false);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(DetailPetaActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(DetailPetaActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (!checkPlayServices()) {
//            Toast.makeText(getApplicationContext(),"Please install google play services",Toast.LENGTH_LONG).show();
//        }
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(DetailPetaActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DetailPetaActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLocation != null) {
                LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                mMap.animateCamera(cameraUpdate);
                startLocationUpdates();
            } else {
                Toast.makeText(DetailPetaActivity.this, "Mohon aktifkan layanan lokasi untuk menggunakan fitur ini.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void settingsrequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        initDataMarker();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(DetailPetaActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    // Declare a variable for the cluster manager.
    private ClusterManager<PetaMarkerPojo> clusterManager;


    private void addItems(PetaPojo petaPojosParam) {
        // Set some lat/lng coordinates to start with.
        double lat = -7.989800;
        double lng = 111.377350;
        if (petaPojosParam != null) {
                lat = Double.parseDouble(petaPojosParam.getLatitude());
                lng = Double.parseDouble(petaPojosParam.getLongitude());
                String namaAset = (petaPojosParam.getNama_aset().length() > 50) ? petaPojosParam.getNama_aset().substring(0, 49) : petaPojosParam.getNama_aset();
                String keterangan = petaPojosParam.getKeterangan();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    keterangan =  Html.fromHtml(keterangan, HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
                }else{
                    keterangan = Jsoup.parse(keterangan).text();
                }
                keterangan = (keterangan.length() > 50) ? keterangan.substring(0, 49) : keterangan;
                PetaMarkerPojo offsetItem = new PetaMarkerPojo(petaPojosParam.getId_aset(), lat, lng, namaAset, keterangan);
                clusterManager.addItem(offsetItem);
        }

    }


    public void currentLocation() {

        if (ActivityCompat.checkSelfPermission(DetailPetaActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DetailPetaActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            settingsrequest();
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (mGoogleApiClient == null) {
            Toast.makeText(DetailPetaActivity.this, "Tidak bisa mengakses lokasi", Toast.LENGTH_SHORT).show();
        } else {
            Location CurrLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (CurrLoc != null) {
                LatLng latLng = new LatLng(CurrLoc.getLatitude(), CurrLoc.getLongitude());
                updateLocation(latLng);
            } else {
//                checkLocation();
            }
        }
    }

    public void updateLocation(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
//        setiap update camera akan fokus ke locasi sekarang
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        updateLocation(latLng);

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(DetailPetaActivity.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(DetailPetaActivity.this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else
//                finish();

                return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        /* 15 secs */
        long UPDATE_INTERVAL = 15000;
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        /* 5 secs */
        long FASTEST_INTERVAL = 5000;
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(DetailPetaActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DetailPetaActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(DetailPetaActivity.this, "Enable Permissions", Toast.LENGTH_LONG).show();
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (ActivityCompat.checkSelfPermission(DetailPetaActivity.this, permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == ALL_PERMISSIONS_RESULT) {
            for (String perms : permissionsToRequest) {
                if (!hasPermission(perms)) {
                    permissionsRejected.add(perms);
                }
            }

            if (permissionsRejected.size() > 0) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                        showMessageOKCancel("Aplikasi ini membutuhkan akses agar semua fitur berjalan normal. Izinkan akses?",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(permissionsRejected.toArray(new String[0]), ALL_PERMISSIONS_RESULT);
                                    }
                                });
                    }
                }

            }
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DetailPetaActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        stopLocationUpdates();
//    }


    public void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }



    private void drawRoute() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            settingsrequest();
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location CurrLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (CurrLoc != null){
            mOrigin = new LatLng(CurrLoc.getLatitude(), CurrLoc.getLongitude());

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(mOrigin, mDestination);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }else{
            Toast.makeText(this, "Anda belum mengaktifkan layanan lokasi", Toast.LENGTH_SHORT).show();
        }

    }


    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=" + getString(R.string.google_maps_key);

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb  = new StringBuilder();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception ignored){

        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";


            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception ignored){

            }

            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }

            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            if (result != null){
                for(int i=0;i<result.size();i++){
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for(int j=0;j<path.size();j++){
                        HashMap<String,String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(8);
                    lineOptions.color(Color.RED);
                }
            }else{
                Toast.makeText(DetailPetaActivity.this, "Jalur tidak ditemukan", Toast.LENGTH_SHORT).show();
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            }else
                Toast.makeText(getApplicationContext(),"Jalur tidak ditemukan", Toast.LENGTH_LONG).show();
        }
    }
}