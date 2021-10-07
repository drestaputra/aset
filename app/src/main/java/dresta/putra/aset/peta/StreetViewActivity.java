package dresta.putra.aset.peta;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class StreetViewActivity extends AppCompatActivity {
    WebView webView;
    private ViewPager viewPager;
    // George St, Sydney
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private AdapterFotoMarker adapterFotoMarker;
    private StreetViewPanoramaView streetViewPanoramaView;
    private String id_aset = "5";
    private static final String STREETVIEW_BUNDLE_KEY = "StreetViewBundleKey";
    private FrameLayout frame;

    interface APIStreetView{
        @FormUrlEncoded
        @POST("api/aset/detail_aset")
        Call<DetailAsetActivity.ResponseDetailAsetPojo> getDetailAset(@Field("id_aset") String id_aset);
    }

    private APIStreetView apiStreetView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_view);
//        StreetViewPanoramaOptions options = new StreetViewPanoramaOptions();
        if (savedInstanceState == null) {
//            options.position(SYDNEY);
        }
        FloatingActionButton FabBack = findViewById(R.id.FabBack);
        FabBack.setOnClickListener(v->{
            finish();
        });
        ImageView IvMap =  findViewById(R.id.IvMap);
        IvMap.setOnClickListener(v -> {
            Intent intent = new Intent(StreetViewActivity.this,DetailPetaActivity.class);
            intent.putExtra("id_aset", id_aset);
            startActivity(intent);
        });
        viewPager = findViewById(R.id.viewPager);
        apiStreetView = RetrofitClientInstance.getRetrofitInstance(StreetViewActivity.this).create(APIStreetView.class);
        Call<DetailAsetActivity.ResponseDetailAsetPojo> apiStreetViewCall =  apiStreetView.getDetailAset(id_aset);
        apiStreetViewCall.enqueue(new Callback<DetailAsetActivity.ResponseDetailAsetPojo>() {
            @Override
            public void onResponse(@NonNull Call<DetailAsetActivity.ResponseDetailAsetPojo> call, @NonNull Response<DetailAsetActivity.ResponseDetailAsetPojo> response) {
                if (response.body() != null){
                    if (response.body().getStatus() == 200){

                        if (response.body().getData().getFoto_aset() != null) {
                            adapterFotoMarker = new AdapterFotoMarker(response.body().getData().getFoto_aset(), StreetViewActivity.this);
                            viewPager.setAdapter(adapterFotoMarker);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DetailAsetActivity.ResponseDetailAsetPojo> call, @NonNull Throwable t) {

            }
        });
//        streetViewPanoramaView = new StreetViewPanoramaView(this, options);

//        frame = (FrameLayout) findViewById(R.id.FrStreetView);



//        // *** IMPORTANT ***
//        // StreetViewPanoramaView requires that the Bundle you pass contain _ONLY_
//        // StreetViewPanoramaView SDK objects or sub-Bundles.
//        Bundle streetViewBundle = null;
//        if (savedInstanceState != null) {
//            streetViewBundle = savedInstanceState.getBundle(STREETVIEW_BUNDLE_KEY);
//        }
//        streetViewPanoramaView.onCreate(streetViewBundle);
//        frame.addView(streetViewPanoramaView);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        changeStatusBarColor();
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
//        String url=getIntent().getStringExtra("url");
        String url= "https://www.google.com/maps/@-7.8013805,110.3647722,3a,75y,264.64h,79.17t/data=!3m6!1e1!3m4!1sS8pzmXVWA79BiQY5qmW-oQ!2e0!7i16384!8i8192!5m1!1e1";

        webView.loadUrl(url);
//        swipe.setRefreshing(true);
        webView.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                progressBar.setVisibility(View.VISIBLE);
            }


            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                String url=getIntent().getStringExtra("url");
//                String url= "https://www.google.com/maps/@-7.8057008,110.3847409,3a,75y,293.2h,55.74t/data=!3m7!1e1!3m5!1sApABV6NCLV8BkCjtNZWgog!2e0!6shttps:%2F%2Fstreetviewpixels-pa.googleapis.com%2Fv1%2Fthumbnail%3Fpanoid%3DApABV6NCLV8BkCjtNZWgog%26cb_client%3Dmaps_sv.tactile.gps%26w%3D203%26h%3D100%26yaw%3D182.28693%26pitch%3D0%26thumbfov%3D100!7i13312!8i6656";
                String url= "https://youtube.com";
                webView.setAlpha(0);
                webView.loadUrl(url);
                Log.d("url", url);
            }

            public void onLoadResource(WebView view, String url) { //Doesn't work
                //swipe.setRefreshing(true);
            }

            public void onPageFinished(WebView view, String url) {

                //Hide the SwipeReefreshLayout
//                progressBar.setVisibility(View.GONE);
//                swipe.setRefreshing(false);
                if (url.contains("sukses"))
                {
                    //call intent to navigate to activity
//                    setResult(RESULT_OK, bundle);
                    Intent Iback= new Intent(getApplication().getApplicationContext(), LoginActivity.class);
                    Iback.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(Iback);
//                    WebActivity.this.finish();
                }
            }

        });


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


    }


//    @Override
//    protected void onResume() {
//        streetViewPanoramaView.onResume();
//        super.onResume();
//    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
//
//    @Override
//    protected void onPause() {
//        streetViewPanoramaView.onPause();
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        streetViewPanoramaView.onDestroy();
//        super.onDestroy();
//    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        Bundle streetViewBundle = outState.getBundle(STREETVIEW_BUNDLE_KEY);
//        if (streetViewBundle == null) {
//            streetViewBundle = new Bundle();
//            outState.putBundle(STREETVIEW_BUNDLE_KEY, streetViewBundle);
//        }
//
//        streetViewPanoramaView.onSaveInstanceState(streetViewBundle);
//    }
}