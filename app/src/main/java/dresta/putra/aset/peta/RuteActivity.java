package dresta.putra.aset.peta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.PolyUtil;
import com.vimalcvs.switchdn.DayNightSwitch;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.peta.util.GeocodedWaypointsItem;
import dresta.putra.aset.peta.util.RoutesItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class RuteActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private GoogleMap map;
    private SupportMapFragment MvPetaAset;
    private LatLng fkip;
    private LatLng monas;
    private String id_aset;
    private LatLng lokasiDari; 
    private LatLng lokasiKe;
    private boolean isNightMode = false;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    public static final int REQUEST_CODE_PERMISSIONS = 101;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private final ArrayList<String> permissions = new ArrayList<>();
    private PetaPojo petaPojo;
    GoogleApiClient mGoogleApiClient;
    public Location mLocation;
    private TextView TxvJarak, TxvNamaAset, TxvLuasAset, TxvAlamatAset, TxvHak;


    @Override
    public void onConnectionSuspended(int i) {

    }

    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rute);
        id_aset = getIntent().getStringExtra("id_aset");
        fkip = new LatLng(-6.3037978, 106.8693713);
        monas = new LatLng(-6.1890511, 106.8251573);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        ArrayList<String> permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        FloatingActionButton fabCurrentLocation = findViewById(R.id.FabCurrentLocation);
        fabCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLocation();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasiDari, 10f));
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else {
            buildGoogleApiClient();
        }


        if (MvPetaAset == null) {
            MvPetaAset = SupportMapFragment.newInstance();
            MvPetaAset.getMapAsync(this);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.map, MvPetaAset).commit();

        DayNightSwitch dayNightSwitch = findViewById(R.id.switch_item);
        dayNightSwitch.setListener(is_night -> {
            if (map != null) {

                if (is_night) {
                    isNightMode = true;
                    //Function to change color
                    map.setMapStyle(MapStyleOptions.loadRawResourceStyle(RuteActivity.this, R.raw.mapstyle_night));
                } else {
                    isNightMode = false;
                    map.setMapStyle(MapStyleOptions.loadRawResourceStyle(RuteActivity.this, R.raw.mapstyle_standar));
                }
            }
        });
        settingsrequest();
        if (lokasiDari == null){
            currentLocation();
        }
        if (petaPojo == null && map!=null){
            createRute();
        }
        TxvJarak = findViewById(R.id.TxvJarak);
        Button BtnMap = findViewById(R.id.BtnMap);
        BtnMap.setOnClickListener(v->{
            Intent intent = new Intent(RuteActivity.this,DetailPetaActivity.class);
            intent.putExtra("id_aset", id_aset);
            startActivity(intent);
        });
        Button BtnStreetView = findViewById(R.id.BtnStreetView);
        BtnStreetView.setOnClickListener(v->{
            Intent intent = new Intent(RuteActivity.this,StreetViewActivity.class);
            intent.putExtra("id_aset", id_aset);
            startActivity(intent);
        });

        TxvNamaAset = findViewById(R.id.TxvNamaAset);
        TxvLuasAset = findViewById(R.id.TxvLuasAset);
        TxvAlamatAset = findViewById(R.id.TxvAlamatAset);
        TxvHak = findViewById(R.id.TxvHak);

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
                        if (petaPojo == null && map!=null && map.isMyLocationEnabled()){
                            createRute();
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(RuteActivity.this, REQUEST_CHECK_SETTINGS);
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
    public void currentLocation() {

        if (ActivityCompat.checkSelfPermission(RuteActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RuteActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Toast.makeText(RuteActivity.this, "Tidak bisa mengakses lokasi", Toast.LENGTH_SHORT).show();
        } else {
            Location CurrLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (CurrLoc != null) {
                LatLng latLng = new LatLng(CurrLoc.getLatitude(), CurrLoc.getLongitude());
                lokasiDari = latLng;
            } else {
//                checkLocation();
            }
        }
    }
    public void setDistanceBetween(LatLng first, LatLng second) {
        Location loc1 = new Location(LocationManager.GPS_PROVIDER);
        Location loc2 = new Location(LocationManager.GPS_PROVIDER);
        loc1.setLatitude(first.latitude);
        loc1.setLongitude(first.longitude);
        loc2.setLatitude(second.latitude);
        loc2.setLongitude(second.longitude);
        float distance = loc1.distanceTo(loc2);
        String distanceString;

        if (distance < 1000)
            if (distance < 1)
                distanceString = String.format(Locale.US, "%.2f M", 1);
            else
                distanceString = String.format(Locale.US, "%.2f M", distance);
        else if (distance > 10000)
            if (distance < 1000000)
                distanceString = String.format(Locale.US, "%.2f KM", distance / 1000);
            else
                distanceString = "FAR";
        else
            distanceString = String.format(Locale.US, "%.2f KM", distance / 1000);

//        Location.distanceBetween(first.latitude, first.longitude, second.latitude, second.longitude, distance);
        TxvJarak.setText(distanceString +" dari lokasi awal");
    }
    
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(RuteActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    private void createRute(){
        ApiServices apiServices = RetrofitClient.apiServices(this);
        ApiServices apiServicesAset = RetrofitClientInstance.getRetrofitInstance(RuteActivity.this).create(ApiServices.class);
        Call<DetailAsetActivity.ResponseDetailAsetPojo> detailAsetPojoCall = apiServicesAset.getDetailAset(id_aset);
        detailAsetPojoCall.enqueue(new Callback<DetailAsetActivity.ResponseDetailAsetPojo>() {
            @Override
            public void onResponse(@NonNull Call<DetailAsetActivity.ResponseDetailAsetPojo> call, @NonNull Response<DetailAsetActivity.ResponseDetailAsetPojo> response) {
                Log.d("tesdebug3", response.raw().toString());
                if (response.body() != null){
                    if (response.body().getStatus() == 200){
                        petaPojo = response.body().getData();
                        lokasiKe = new LatLng(Double.parseDouble(petaPojo.getLatitude()), Double.parseDouble(petaPojo.getLongitude()));
                        //        TxvNamaAset, TxvLuasAset, TxvAlamatAset, TxvHak
                        TxvNamaAset.setText(petaPojo.getNama_aset());
                        TxvLuasAset.setText(petaPojo.getLuas_tanah()+" meter persegi");
                        TxvAlamatAset.setText(petaPojo.getAlamat());
                        TxvHak.setText(petaPojo.getJenis_hak());
                        MarkerOptions markerKe = new MarkerOptions()
                                .position(lokasiKe)
                                .title(petaPojo.getNama_aset());
                        if (lokasiDari != null){
                            //buat request ke API rute
                            MarkerOptions markerDari = new MarkerOptions()
                                    .position(lokasiDari)
                                    .title("Saya");


                            map.addMarker(markerDari);
                            map.addMarker(markerKe);
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasiDari, 10f));
                            setDistanceBetween(lokasiDari, lokasiKe);
                            String lokasiDariS = String.valueOf(lokasiDari.latitude) + "," + String.valueOf(lokasiDari.longitude);
                            String lokasiKeS = String.valueOf(lokasiKe.latitude) + "," + String.valueOf(lokasiKe.longitude);

//                            apiServices.getDirection(lokasiDariS, lokasiKeS, getString(R.string.google_maps_route_key))
//                                    .enqueue(new Callback<DirectionResponses>() {
//                                        @Override
//                                        public void onResponse(@NonNull Call<DirectionResponses> call, @NonNull Response<DirectionResponses> response) {
//                                            drawPolyline(response);
//                                        }
//
//                                        @Override
//                                        public void onFailure(@NonNull Call<DirectionResponses> call, @NonNull Throwable t) {
//
//                                        }
//                                    });
                        }else{
                            currentLocation();
                            createRute();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DetailAsetActivity.ResponseDetailAsetPojo> call, @NonNull Throwable t) {

            }
        });
    }
    

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        if (petaPojo == null){
            createRute();
        }
        if (lokasiDari == null){
            currentLocation();
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(RuteActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RuteActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                map.animateCamera(cameraUpdate);
                startLocationUpdates();
                lokasiDari = latLng;
            } else {
                currentLocation();
                Toast.makeText(RuteActivity.this, "Mohon aktifkan layanan lokasi untuk menggunakan fitur ini.", Toast.LENGTH_SHORT).show();
            }

        }
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
        if (ActivityCompat.checkSelfPermission(RuteActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RuteActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(RuteActivity.this, "Enable Permissions", Toast.LENGTH_LONG).show();
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


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
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (ActivityCompat.checkSelfPermission(RuteActivity.this, permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void drawPolyline(@NonNull Response<DirectionResponses> response) {
        if (response.body() != null) {

            String shape = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
            PolylineOptions polyline = new PolylineOptions()
                    .addAll(PolyUtil.decode(shape))
                    .width(8f)
                    .color(Color.RED);
            map.addPolyline(polyline);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Gagal terhubung ke server", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        currentLocation();
    }

    private interface ApiServices {
        @GET("maps/api/directions/json")
        Call<DirectionResponses> getDirection(@Query("origin") String origin,
                                              @Query("destination") String destination,
                                              @Query("key") String apiKey);
        @FormUrlEncoded
        @POST("api/aset/detail_aset")
        Call<DetailAsetActivity.ResponseDetailAsetPojo> getDetailAset(@Field("id_aset") String id_aset);
    }

    private static class RetrofitClient {
        static ApiServices apiServices(Context context) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(context.getResources().getString(R.string.base_url_gmap))
                    .build();

            return retrofit.create(ApiServices.class);
        }
    }
    static class DirectionResponses{

        @SerializedName("routes")
        private List<RoutesItem> routes;

        @SerializedName("geocoded_waypoints")
        private List<GeocodedWaypointsItem> geocodedWaypoints;

        @SerializedName("status")
        private String status;

        public void setRoutes(List<RoutesItem> routes){
            this.routes = routes;
        }

        public List<RoutesItem> getRoutes(){
            return routes;
        }

        public void setGeocodedWaypoints(List<GeocodedWaypointsItem> geocodedWaypoints){
            this.geocodedWaypoints = geocodedWaypoints;
        }

        public List<GeocodedWaypointsItem> getGeocodedWaypoints(){
            return geocodedWaypoints;
        }

        public void setStatus(String status){
            this.status = status;
        }

        public String getStatus(){
            return status;
        }
    }
}