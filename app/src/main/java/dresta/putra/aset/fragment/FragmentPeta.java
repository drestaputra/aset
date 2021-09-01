package dresta.putra.aset.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.vimalcvs.switchdn.DayNightSwitch;
import com.vimalcvs.switchdn.DayNightSwitchListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.peta.AdapterMarker;
import dresta.putra.aset.peta.PetaMarkerPojo;
import dresta.putra.aset.peta.PetaPojo;
import dresta.putra.aset.peta.PetaResponsePojo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class FragmentPeta extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private AdapterMarker adapter;
    private ViewPager viewPager;
    private FloatingActionButton FabCurrentLocation;
    private SupportMapFragment MvPetaAset;
    private SearchView searchView;
    final  String TAG = "tesdebug";
    public FragmentPeta() {
        // Required empty public constructor
    }
    private GoogleMap mMap;
    public Location mLocation;
    GoogleApiClient mGoogleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 15000;  /* 15 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    public static final int REQUEST_CODE_PERMISSIONS = 101;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private String pencarian = "";
    private Marker mapMarker;

//    private NightModeButton nightModeButton;
    private DayNightSwitch dayNightSwitch;
    private boolean isNightMode = false;
    private List<PetaPojo> petaPojos;
    private List<PetaPojo> markerPetaPojos = new ArrayList<>();
    private ClusterManager<PetaMarkerPojo> clusterManager;
    private PrefManager prefManager;





    interface APIFragmentPeta{
        @FormUrlEncoded
        @POST("api/aset/data_aset")
        Call<PetaResponsePojo> getDataAset(
                @Field("pencarian") String pencarian,
                @Field("page") int page,
                @Field("perPage") int perPage
        );
    }
    private APIFragmentPeta apiFragmentPeta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_peta, container, false);
        prefManager = new PrefManager(getContext());
        FabCurrentLocation = view.findViewById(R.id.FabCurrentLocation);
        searchView = view.findViewById(R.id.mSearchView);
        viewPager = view.findViewById(R.id.viewPager);
        apiFragmentPeta = RetrofitClientInstance.getRetrofitInstance(getContext()).create(APIFragmentPeta.class);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }else{
            buildGoogleApiClient();
        }
        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String lokasi = query.toString();

                List<Address> addressList = null;
                if (!query.equals("")){
                    try {
                        addressList = geocoder.getFromLocationName(lokasi, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addressList != null && addressList.size() != 0){
                        Address address = addressList.get(0);
                        if (mMap !=null){

                            mapMarker.remove();
                            // Set the color of the marker to green
                            BitmapDescriptor defaultMarker = BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ic_search_marker",100,100));
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
                        if (mMap != null){
//                            mMap.clear();
                        }
                        Toast.makeText(getActivity().getApplicationContext(), "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show();
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
        FabCurrentLocation.setOnClickListener(new View.OnClickListener() {
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
        getChildFragmentManager().beginTransaction().replace(R.id.map, MvPetaAset).commit();

        dayNightSwitch = view.findViewById(R.id.switch_item);
        dayNightSwitch.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean is_night) {
                if (mMap != null){

                    if(is_night){
                        isNightMode = true;
                        //Function to change color
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity().getApplicationContext(), R.raw.mapstyle_night));
                    }else {
                        isNightMode = false;
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity().getApplicationContext(), R.raw.mapstyle_standar));
                    }
                }
            }
        });
        settingsrequest();
        //        slider
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }
    public void initDataMarker(){

        if (mMap !=null){
            mMap.clear();
            mapMarker =  mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(0,1)).visible(false)
                    .title("")
                    .snippet(""));
        }else{
            mGoogleApiClient.connect();
        }
        if (prefManager.getKontakPojo() != null && prefManager.getKontakPojo().getApp_is_aset_show()!=null && prefManager.getKontakPojo().getApp_is_aset_show().equals("0")){
            Toast.makeText(getContext(), "Maaf saat ini data aset sedang dibatasi aksesnya", Toast.LENGTH_SHORT).show();
        }else{
            Call<PetaResponsePojo> petaResponsePojoCall = apiFragmentPeta.getDataAset(pencarian,0,10000);
            petaResponsePojoCall.enqueue(new Callback<PetaResponsePojo>() {
                @Override
                public void onResponse(Call<PetaResponsePojo> call, Response<PetaResponsePojo> response) {
                    if (response.body() != null){

                        if (response.body().getStatus() == 200){
                            petaPojos = response.body().getData();
                            mMap.setOnCameraIdleListener(clusterManager);
                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(@NonNull LatLng latLng) {
//                                set agar viewpager tidak menampilkan apapun ketika dikluk outside marker
                                    List<PetaPojo> petaPojosNull = new ArrayList<PetaPojo>();
                                    adapter = new AdapterMarker(petaPojosNull, Objects.requireNonNull(getActivity()).getApplicationContext());
                                    viewPager.setAdapter(adapter);
                                    viewPager.setPadding(0, 0, 0, 0);
                                }
                            });
                            initClusterManager(petaPojos);
                        }
                    }
                }

                @Override
                public void onFailure(Call<PetaResponsePojo> call, Throwable t) {

                }
            });
        }
    }

    private void initClusterManager(List<PetaPojo> petaPojosParam){
        clusterManager = new ClusterManager<PetaMarkerPojo>(getContext(), mMap);
        mMap.setOnCameraIdleListener(clusterManager);
        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<PetaMarkerPojo>() {
            @Override
            public boolean onClusterItemClick(PetaMarkerPojo item) {
//                                CameraUpdate cameraUpdate = CameraUpdateFactory.zoomIn();
//                                mMap.animateCamera(cameraUpdate);
                markerPetaPojos = new ArrayList<PetaPojo>();
                if (petaPojos != null){
                    for (int i = 0; i < petaPojos.size(); i++) {
                        if (petaPojos.get(i).getId_aset().equals(item.getId_aset())){
                            markerPetaPojos.add(petaPojos.get(i));
                        }
                    }
                }
                if (markerPetaPojos != null){
                    adapter = new AdapterMarker(markerPetaPojos, Objects.requireNonNull(getActivity()).getApplicationContext());
                    viewPager.setAdapter(adapter);
                    viewPager.setPadding(0, 0, 30, 0);
                }
                return false;
            }
        });
        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<PetaMarkerPojo>() {
            @Override
            public boolean onClusterClick(Cluster<PetaMarkerPojo> cluster) {
                return false;
            }
        });
        double lat = -7.989800;
        double lng = 111.377350;
        if (petaPojosParam != null){
            for (int i = 0; i < petaPojosParam.size(); i++) {
                lat = Double.parseDouble(petaPojosParam.get(i).getLatitude());
                lng = Double.parseDouble(petaPojosParam.get(i).getLongitude());
                String namaAset = (petaPojosParam.get(i).getNama_aset().length() > 50) ? petaPojosParam.get(i).getNama_aset().substring(0, 49) : petaPojosParam.get(i).getNama_aset();
                String keterangan = (petaPojosParam.get(i).getKeterangan().length() > 50) ? petaPojosParam.get(i).getKeterangan().substring(0, 49) : petaPojosParam.get(i).getKeterangan();

                PetaMarkerPojo offsetItem = new PetaMarkerPojo(petaPojosParam.get(i).getId_aset(), lat, lng, namaAset, keterangan);
                clusterManager.addItem(offsetItem);
            }
        }
    }


    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getActivity().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    @Override
    public void onResume() {
        super.onResume();
        dayNightSwitch.setIsNight(isNightMode);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }else{
            buildGoogleApiClient();
        }
        settingsrequest();
//        nightModeButton.init(getActivity().getApplicationContext());
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

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity().getApplicationContext(), R.raw.mapstyle_standar));

        connectClient(mMap);

    }

    public void connectClient(GoogleMap googleMap)
    {
        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(false);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        initDataMarker();


    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

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

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            settingsrequest();
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLocation != null){
                LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                mMap.animateCamera(cameraUpdate);
                startLocationUpdates();
            }else{
                Toast.makeText(getContext(), "Mohon aktifkan layanan lokasi untuk menggunakan fitur ini.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void settingsrequest()
    {
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
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        currentLocation();
                        if (petaPojos == null){
                            initDataMarker();
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
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



    private void addItems(List<PetaPojo> petaPojosParam) {
        // Set some lat/lng coordinates to start with.


    }


    public  void currentLocation(){
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Toast.makeText(getContext(), "Tidak bisa mengakses lokasi", Toast.LENGTH_SHORT).show();
        }else{
            Location CurrLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (CurrLoc != null){
                LatLng latLng = new LatLng(CurrLoc.getLatitude(), CurrLoc.getLongitude());
                updateLocation(latLng);
            }else{
//                checkLocation();
            }
        }
    }

    public void updateLocation(LatLng latLng){
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
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity().getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else
//                finish();

            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity().getApplicationContext(), "Enable Permissions", Toast.LENGTH_LONG).show();
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
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
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
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


    public void stopLocationUpdates()
    {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }
}
