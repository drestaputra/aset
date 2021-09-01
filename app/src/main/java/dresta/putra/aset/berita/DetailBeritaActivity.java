package dresta.putra.aset.berita;

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
public class DetailBeritaActivity extends AppCompatActivity {

    ImageView ivGambarBerita;
    TextView TxvTglArtikel,TxvPenulisArtikel,TxvJudulArtikel;
    WebView WvIsiArtikel;
    Toolbar toolbar;

    interface MyAPIService{
        @FormUrlEncoded
        @POST("api/berita/detail_berita")
        Call<ResponseDetailInformasiProgramPojo> getDetailArtikel(@Field("id_berita") String id_berita);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);


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
        String id_beritai = getIntent().getStringExtra("id_berita");
        Call<ResponseDetailInformasiProgramPojo> call = myAPIService.getDetailArtikel(id_beritai);
        call.enqueue(new Callback<ResponseDetailInformasiProgramPojo>() {

            @Override
            public void onResponse(Call<ResponseDetailInformasiProgramPojo> call, Response<ResponseDetailInformasiProgramPojo> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()==200){
                        BeritaPojo informasiProgramPojo = response.body().getData();
                        showDetailBerita(informasiProgramPojo);
                    }
                }else{
                    finish();
                    Toast.makeText(DetailBeritaActivity.this, "Artikel tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseDetailInformasiProgramPojo> call, Throwable throwable) {
                finish();
                Toast.makeText(DetailBeritaActivity.this, "Artikel tidak ditemukan", Toast.LENGTH_SHORT).show();
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


    private void showDetailBerita(BeritaPojo details) {
        // Tangkap data dari intent
        String judul_artikel= details.getJudul_berita();
        String tgl_berita = details.getTgl_berita();
        String isi_berita = details.getDeskripsi_berita();
        String foto_berita = details.getFoto_berita();
        TxvJudulArtikel.setText(judul_artikel);
        TxvTglArtikel.setText(tgl_berita);

        if (details.getFoto_berita() != null && details.getFoto_berita().length() > 0) {
            Picasso.with(this).load(details.getFoto_berita()).placeholder(R.color.greycustom2).into(ivGambarBerita);
        } else {
            Picasso.with(this).load(R.color.greycustom2).into(ivGambarBerita);
        }
        Picasso.with(this).load(foto_berita).into(ivGambarBerita);
        // Set isi berita sebagai html ke WebView

        WvIsiArtikel.loadData(details.getDeskripsi_berita(), "text/html; charset=utf-8", "UTF-8");
//        WvIsiArtikel.loadDataWithBaseURL(null, details.getDeskripsi_berita(), "text/html", "utf-8",null);
    }

    class ResponseDetailInformasiProgramPojo{
        @SerializedName("status")
        int status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        BeritaPojo data;

        public ResponseDetailInformasiProgramPojo(int status, String msg, BeritaPojo data) {
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

        public BeritaPojo getData() {
            return data;
        }

        public void setData(BeritaPojo data) {
            this.data = data;
        }
    }


}
