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
    TextView TxvNamaAset, TxvKodeBarang, TxvRegister, TxvLuasTanah, TxvTahunPerolehan, TxvAlamat, TxvJenisHak,
            TxvTanggalSertifikat, TxvNomorSertifikat, TxvPenggunaan, TxvAsalPerolehan, TxvHargaPerolehan;

    ImageView BtnMap;
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
        TxvNamaAset= findViewById(R.id.TxvNamaAset);
        TxvKodeBarang = findViewById(R.id.TxvKodeBarang);
        TxvRegister = findViewById(R.id.TxvRegister);
        TxvLuasTanah = findViewById(R.id.TxvLuasTanah);
        TxvTahunPerolehan = findViewById(R.id.TxvTahunPerolehan);

        TxvAlamat = findViewById(R.id.TxvAlamat);
        TxvJenisHak = findViewById(R.id.TxvJenisHak);
        TxvTanggalSertifikat = findViewById(R.id.TxvTanggalSertifikat);
        TxvNomorSertifikat = findViewById(R.id.TxvNomorSertifikat);
        TxvPenggunaan = findViewById(R.id.TxvPenggunaan);
        TxvAsalPerolehan = findViewById(R.id.TxvAsalPerolehan);
        TxvHargaPerolehan = findViewById(R.id.TxvHargaPerolehan);
        BtnMap = findViewById(R.id.BtnMap);
        String id_aseti = getIntent().getStringExtra("id_aset");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        BtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentl= new Intent(DetailAsetActivity.this, DetailPetaActivity.class);
                intentl.putExtra("id_aset", id_aseti);
                startActivity(intentl);
            }
        });

        WvKeterangan = (WebView) findViewById(R.id.WvKeterangan);

        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance(getApplicationContext()).create(MyAPIService.class);

        Call<ResponseDetailAsetPojo> call = myAPIService.getDetailArtikel(id_aseti);
        call.enqueue(new Callback<ResponseDetailAsetPojo>() {

            @Override
            public void onResponse(Call<ResponseDetailAsetPojo> call, Response<ResponseDetailAsetPojo> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()==200){
                        PetaPojo informasiProgramPojo = response.body().getData();
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

        TxvNamaAset.setText(details.getNama_aset());
        TxvKodeBarang.setText("Kode Barang: "+details.getKode_barang());
        TxvRegister.setText("Register: "+details.getRegister());
        TxvLuasTanah.setText("Luas tanah: "+details.getLuas_tanah()+" M persegi");
        TxvTahunPerolehan.setText("Tahun Perolehan: "+details.getTahun_perolehan());
        TxvAlamat.setText("Alamat: "+details.getAlamat());
        TxvJenisHak.setText("Jenis Hak: "+details.getJenis_hak());
        TxvTanggalSertifikat.setText("Tanggal Sertifikat: "+details.getTanggal_sertifikat());
        TxvNomorSertifikat.setText("Nomor Sertifikat: "+details.getNomor_sertifikat());
        TxvPenggunaan.setText("Pengunaan: "+details.getPenggunaan());
        TxvAsalPerolehan.setText("Asal Perolehan: "+details.getAsal_perolehan());
        TxvHargaPerolehan.setText("Harga Perolehan: "+details.getHarga_perolehan());

//        // Set isi berita sebagai html ke WebView
        WvKeterangan.loadData(details.getKeterangan(), "text/html; charset=utf-8", "UTF-8");
//        WvIsiArtikel.loadDataWithBaseURL(null, details.getDeskripsi_informasi_program(), "text/html", "utf-8",null);
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
