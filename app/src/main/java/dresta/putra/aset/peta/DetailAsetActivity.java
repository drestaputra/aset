package dresta.putra.aset.peta;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import dresta.putra.aset.AboutActivity;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.WebActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import dresta.putra.aset.R;
public class DetailAsetActivity extends AppCompatActivity {
    private ViewPager viewPager;
    ImageView ivGambarBerita;
    private TextView TxvJarak, TxvNamaAset, TxvLuasAset, TxvAlamatAset, TxvHak;

    Button BtnMap, BtnStreetView, BtnRute;
    WebView WvKeterangan;
    Toolbar toolbar;
    private AdapterFotoMarker adapterFotoMarker;

    interface MyAPIService{
        @FormUrlEncoded
        @POST("api/aset/detail_aset")
        Call<ResponseDetailAsetPojo> getDetailArtikel(@Field("id_aset") String id_aset);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_aset);

        viewPager = findViewById(R.id.viewPager);
        ivGambarBerita =  findViewById(R.id.IvGambarArtikel);

        TxvNamaAset = findViewById(R.id.TxvNamaAset);
        TxvLuasAset = findViewById(R.id.TxvLuasAset);
        TxvAlamatAset = findViewById(R.id.TxvAlamatAset);
        TxvHak = findViewById(R.id.TxvHak);

        BtnMap = findViewById(R.id.BtnMap);
        BtnRute = findViewById(R.id.BtnRute);
        BtnStreetView = findViewById(R.id.BtnStreetView);
        String id_aseti = getIntent().getStringExtra("id_aset");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());
        BtnMap.setOnClickListener(v -> {
            Intent intentl= new Intent(DetailAsetActivity.this, DetailPetaActivity.class);
            intentl.putExtra("id_aset", id_aseti);
            startActivity(intentl);
        });
        BtnStreetView.setOnClickListener(v -> {
            Intent intentl= new Intent(DetailAsetActivity.this, StreetViewActivity.class);
            intentl.putExtra("id_aset", id_aseti);
            startActivity(intentl);
        });
        BtnRute.setOnClickListener(v -> {
            Intent intentl= new Intent(DetailAsetActivity.this, RuteActivity.class);
            intentl.putExtra("id_aset", id_aseti);
            startActivity(intentl);
        });

//        WvKeterangan = (WebView) findViewById(R.id.WvKeterangan);

        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance(getApplicationContext()).create(MyAPIService.class);

        Call<ResponseDetailAsetPojo> call = myAPIService.getDetailArtikel(id_aseti);
        call.enqueue(new Callback<ResponseDetailAsetPojo>() {

            @Override
            public void onResponse(Call<ResponseDetailAsetPojo> call, Response<ResponseDetailAsetPojo> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()==200){

                        PetaPojo informasiProgramPojo = response.body().getData();
                        if (informasiProgramPojo.getLatitude() != null && informasiProgramPojo.getLongitude() != null){
                            BtnMap.setVisibility(View.VISIBLE);
                        }
                        showDetailAset(informasiProgramPojo);
                    }
                }else{
                    finish();
                    Toast.makeText(DetailAsetActivity.this, "Aset tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseDetailAsetPojo> call, Throwable throwable) {
                finish();
                Toast.makeText(DetailAsetActivity.this, "Aset tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();


    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    private void showDetailAset(PetaPojo details) {
        if (details.getFoto_aset() != null) {
            adapterFotoMarker = new AdapterFotoMarker(details.getFoto_aset(), DetailAsetActivity.this);
            viewPager.setAdapter(adapterFotoMarker);
        }
        if ((details.getLatitude() != null && !details.getLatitude().equals("0")) && (details.getLongitude() != null && !details.getLongitude().equals("0"))){
            BtnMap.setVisibility(View.VISIBLE);
            BtnRute.setVisibility(View.VISIBLE);
            BtnStreetView.setVisibility(View.VISIBLE);
        }else{
            BtnMap.setVisibility(View.GONE);
            BtnRute.setVisibility(View.GONE);
            BtnStreetView.setVisibility(View.GONE);
        }

        TxvNamaAset.setText(details.getNama_aset());
        TxvLuasAset.setText(details.getLuas_tanah()+" meter persegi");
        TxvAlamatAset.setText(details.getAlamat());
        TxvHak.setText(details.getJenis_hak());
    }

    class ResponseDetailAsetPojo{
        @SerializedName("status")
        int status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        PetaPojo data;

        public ResponseDetailAsetPojo(int status, String msg, PetaPojo data) {
            this.status = status;
            this.msg = msg;
            this.data = data;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public PetaPojo getData() {
            return data;
        }

        public void setData(PetaPojo data) {
            this.data = data;
        }
    }


}
