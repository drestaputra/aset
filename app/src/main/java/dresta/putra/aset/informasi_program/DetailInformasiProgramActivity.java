package dresta.putra.aset.informasi_program;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import dresta.putra.aset.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import dresta.putra.aset.R;
public class DetailInformasiProgramActivity extends AppCompatActivity {

    ImageView ivGambarBerita;
    TextView TxvTglArtikel,TxvPenulisArtikel,TxvJudulArtikel;
    WebView WvIsiArtikel;
    Toolbar toolbar;

    interface MyAPIService{
        @FormUrlEncoded
        @POST("api/informasi_program/detail_informasi_program")
        Call<ResponseDetailInformasiProgramPojo> getDetailArtikel(@Field("id_informasi_program") String id_informasi_program);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_informasi_program);


        ivGambarBerita =  findViewById(R.id.IvGambarArtikel);
        TxvTglArtikel= findViewById(R.id.TxvTglArtikel);
        TxvJudulArtikel = findViewById(R.id.TxvJudulArtikel);
        TxvPenulisArtikel=  findViewById(R.id.TxvPenulisArtikel);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        WvIsiArtikel = (WebView) findViewById(R.id.wvKontenBerita);

        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance(getApplicationContext()).create(MyAPIService.class);
        String id_informasi_programi = getIntent().getStringExtra("id_informasi_program");
        Call<ResponseDetailInformasiProgramPojo> call = myAPIService.getDetailArtikel(id_informasi_programi);
        call.enqueue(new Callback<ResponseDetailInformasiProgramPojo>() {

            @Override
            public void onResponse(Call<ResponseDetailInformasiProgramPojo> call, Response<ResponseDetailInformasiProgramPojo> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()==200){
                        InformasiProgramPojo informasiProgramPojo = response.body().getData();
                        showDetailBerita(informasiProgramPojo);
                    }
                }else{
                    finish();
                    Toast.makeText(DetailInformasiProgramActivity.this, "Artikel tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseDetailInformasiProgramPojo> call, Throwable throwable) {
                finish();
                Toast.makeText(DetailInformasiProgramActivity.this, "Artikel tidak ditemukan", Toast.LENGTH_SHORT).show();
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


    private void showDetailBerita(InformasiProgramPojo details) {
        // Tangkap data dari intent
        String judul_artikel= details.getJudul_informasi_program();
        String tgl_berita = details.getTgl_informasi_program();
        String isi_berita = details.getDeskripsi_informasi_program();
        String foto_berita = details.getFoto_informasi_program();
        TxvJudulArtikel.setText(judul_artikel);
        TxvTglArtikel.setText(tgl_berita);

        if (details.getFoto_informasi_program() != null && details.getFoto_informasi_program().length() > 0) {
            Picasso.with(this).load(details.getFoto_informasi_program()).placeholder(R.color.greycustom2).into(ivGambarBerita);
        } else {
            Picasso.with(this).load(R.color.greycustom2).into(ivGambarBerita);
        }
        Picasso.with(this).load(foto_berita).into(ivGambarBerita);
        // Set isi berita sebagai html ke WebView

        WvIsiArtikel.loadData(details.getDeskripsi_informasi_program(), "text/html; charset=utf-8", "UTF-8");
//        WvIsiArtikel.loadDataWithBaseURL(null, details.getDeskripsi_informasi_program(), "text/html", "utf-8",null);
    }

    class ResponseDetailInformasiProgramPojo{
        @SerializedName("status")
        int status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        InformasiProgramPojo data;

        public ResponseDetailInformasiProgramPojo(int status, String msg, InformasiProgramPojo data) {
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

        public InformasiProgramPojo getData() {
            return data;
        }

        public void setData(InformasiProgramPojo data) {
            this.data = data;
        }
    }


}
