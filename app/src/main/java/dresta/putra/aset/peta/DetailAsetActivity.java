package dresta.putra.aset.peta;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.google.gson.annotations.SerializedName;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dresta.putra.aset.ResponsePojo;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.peta.model.SaranPemanfaatanPojo;
import dresta.putra.aset.peta.model.SaranPemanfaatanResponsePojo;
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
    ChipsInput chipsInput;
    List<SaranPemanfaatanPojo> contactList = new ArrayList<>();
    List<String> selectedIdsaranPemanfaatan = new ArrayList<>();


    interface MyAPIService{
        @FormUrlEncoded
        @POST("api/aset/detail_aset")
        Call<ResponseDetailAsetPojo> getDetailArtikel(@Field("id_aset") String id_aset);

        @FormUrlEncoded
        @POST("api/aset/data_saran_pemanfaatan")
        Call<SaranPemanfaatanResponsePojo> getDataSaranPemanfaatan(
                @Field("pencarian") String pencarian,
                @Field("page") int page,
                @Field("perPage") int perPage
        );

      @FormUrlEncoded
      @POST("api/aset/kirim_saran_pemanfaatan")
      Call<ResponsePojo> kirimSaranPemanfaatan(@Field("id_aset") String id_aset, @Field("id_saran_pemanfaatan[]") List<String> id_saran_pemanfaatan);
    }
    private MyAPIService myAPIService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_aset);
        myAPIService = RetrofitClientInstance.getRetrofitInstance(getApplicationContext()).create(MyAPIService.class);
        viewPager = findViewById(R.id.viewPager);
        ivGambarBerita =  findViewById(R.id.IvGambarArtikel);

        TxvNamaAset = findViewById(R.id.TxvNamaAset);
        TxvLuasAset = findViewById(R.id.TxvLuasAset);
        TxvAlamatAset = findViewById(R.id.TxvAlamatAset);
        TxvHak = findViewById(R.id.TxvHak);
        LinearLayout LlSaranPemanfaatan = findViewById(R.id.LlSaranPemanfaatan);

        Button BtnKirim = findViewById(R.id.BtnKirim);

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
        BtnKirim.setOnClickListener(v-> {
            List<? extends ChipInterface> saranPemanfaatanPojosSelected = chipsInput.getSelectedChipList();
            for (int i = 0; i < saranPemanfaatanPojosSelected.size();i++){
                selectedIdsaranPemanfaatan.add(saranPemanfaatanPojosSelected.get(i).getId().toString());
             }
            Call<ResponsePojo> responsePojoCall = myAPIService.kirimSaranPemanfaatan(id_aseti,selectedIdsaranPemanfaatan);
            responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                @Override
                public void onResponse(@NonNull Call<ResponsePojo> call, @NonNull Response<ResponsePojo> response) {
                    if (response.body() != null){
                        if (response.body().getStatus() == 200){
                            Toast.makeText(DetailAsetActivity.this, "Saran Pemanfaatan Berhasil Dikirim", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(DetailAsetActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(DetailAsetActivity.this, "Saran Pemanfaatan Gagal Dikirim", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponsePojo> call, @NonNull Throwable t) {
                    Toast.makeText(DetailAsetActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                }
            });
        });

        chipsInput = (ChipsInput) findViewById(R.id.chips_input);
        chipsInput.clearFocus();
        NestedScrollView SvDetailAset = findViewById(R.id.SvDetailAset);

        chipsInput.addChipsListener(new ChipsInput.ChipsListener() {

            @Override
            public void onChipRemoved(ChipInterface chip, int newSize) {
                // chip removed
                // newSize is the size of the updated selected chip list
            }

            @Override
            public void onChipAdded(ChipInterface chip, int newSize) {

            }

            @Override
            public void onTextChanged(CharSequence text) {
                // text changed
            }
        });





//        WvKeterangan = (WebView) findViewById(R.id.WvKeterangan);



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
                        if (informasiProgramPojo.getStatus_aset().equals("idle")){
                            //        jika status aset = idle
                            LlSaranPemanfaatan.setVisibility(View.VISIBLE);
                            SvDetailAset.post(() -> SvDetailAset.fullScroll(View.FOCUS_DOWN));
                            initDataSaranPemanfaatan();
                        }else{
                            LlSaranPemanfaatan.setVisibility(View.GONE);
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
    private void initDataSaranPemanfaatan(){
        Call<SaranPemanfaatanResponsePojo> saranPemanfaatanResponsePojoCall = myAPIService.getDataSaranPemanfaatan("",0,9999);
        saranPemanfaatanResponsePojoCall.enqueue(new Callback<SaranPemanfaatanResponsePojo>() {
            @Override
            public void onResponse(Call<SaranPemanfaatanResponsePojo> call, Response<SaranPemanfaatanResponsePojo> response) {
                if (response.body() != null){
                    if (response.body().getStatus() == 200){
                        List<SaranPemanfaatanPojo> saranPemanfaatanPojos = response.body().getData();
                        chipsInput.setFilterableList(saranPemanfaatanPojos);
                    }
                }
            }

            @Override
            public void onFailure(Call<SaranPemanfaatanResponsePojo> call, Throwable t) {

            }
        });

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
