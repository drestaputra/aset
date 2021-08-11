package dresta.putra.aset.kolektor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class ProfilKolektorActivity extends AppCompatActivity {
    private String id_nasabah;
    private TextView TxvUsername, TxvEmail, TxvNamaKolektor, TxvNoHp, TxvAlamat, TxvProvinsi, TxvKabupaten, TxvKecamatan, Txvwarga_negara,TxvNoKtp, TxvNasabah,TxvPinjaman, TxvSimpanan;
    private ImageView IvFotoNasabah,IvBack;
    private KolektorPojo kolektorPojo;
    private LinearLayout NoConn;
    private Button btRefresh;
    SwipeRefreshLayout swipe;
    interface APIProfilKolektor{
        @GET("api/kolektor/profil")
        Call<ProfilKolektorPojoResponse> getProfil();
        @GET("api/kolektor/summary_kolektor")
        Call<SummaryKolektorPojo> getSummaryKolektor();
    }
    private APIProfilKolektor servicePojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_kolektor);
        ImageView IvBack = findViewById(R.id.IvBack);
        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipe = findViewById(R.id.swipe);
        TxvUsername = findViewById(R.id.TxvUsername);
        TxvEmail = findViewById(R.id.TxvEmail);
        TxvNamaKolektor = findViewById(R.id.TxvNamaKolektor);
        TxvNoHp = findViewById(R.id.TxvNoHp);
        TxvAlamat = findViewById(R.id.TxvAlamat);
        TxvProvinsi = findViewById(R.id.TxvProvinsi);
        TxvKabupaten = findViewById(R.id.TxvKabupaten);
        TxvKecamatan = findViewById(R.id.TxvKecamatan);
        Txvwarga_negara = findViewById(R.id.Txvwarga_negara);
        TxvNoKtp = findViewById(R.id.TxvNoKtp);
        TxvNasabah = findViewById(R.id.TxvNasabah);
        TxvPinjaman = findViewById(R.id.TxvPinjaman);
        TxvSimpanan = findViewById(R.id.TxvSimpanan);
        NoConn = findViewById(R.id.NoConn);
        btRefresh = findViewById(R.id.btRefresh);
        servicePojo = RetrofitClientInstance.getRetrofitInstance(ProfilKolektorActivity.this).create(APIProfilKolektor.class);
        initDataProfil();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDataProfil();
                swipe.setRefreshing(false);
            }
        });
        ImageView IvEdit = findViewById(R.id.IvEdit);
        IvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Iedit = new Intent(ProfilKolektorActivity.this,EditProfilKolektorActivity.class);
                startActivity(Iedit);
                overridePendingTransition(0,0);
            }
        });
        ImageView IvEditPassword = findViewById(R.id.IvEditPassword);
        IvEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Iedit = new Intent(ProfilKolektorActivity.this,EditPasswordKolektorActivity.class);
                startActivity(Iedit);
                overridePendingTransition(0,0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDataProfil();
    }

    private void initDataProfil(){
        Call<ProfilKolektorPojoResponse> kolektorPojoCall = servicePojo.getProfil();
        kolektorPojoCall.enqueue(new Callback<ProfilKolektorPojoResponse>() {
            @Override
            public void onResponse(Call<ProfilKolektorPojoResponse> call, Response<ProfilKolektorPojoResponse> response) {
                if (response.body()!=null){
                    if (response.body().getStatus()==200){
                        NoConn.setVisibility(View.GONE);
                        kolektorPojo = response.body().getData();
                        TxvUsername.setText(kolektorPojo.getUsername());
                        TxvEmail.setText(kolektorPojo.getEmail());
                        TxvNamaKolektor.setText(kolektorPojo.getNama());
                        TxvNoHp.setText(kolektorPojo.getNo_hp());
                        TxvAlamat.setText(kolektorPojo.getAlamat());
                        TxvProvinsi.setText(kolektorPojo.getLabel_provinsi());
                        TxvKabupaten.setText(kolektorPojo.getLabel_kabupaten());
                        TxvKecamatan.setText(kolektorPojo.getLabel_kecamatan());
                        Txvwarga_negara.setText(kolektorPojo.getwarga_negara());
                        TxvNoKtp.setText(kolektorPojo.getNo_ktp());
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfilKolektorPojoResponse> call, Throwable t) {
//                Toast.makeText(ProfilKolektorActivity.this, "Terjadi gangguan jaringan", Toast.LENGTH_SHORT).show();
                NoConn.setVisibility(View.VISIBLE);
                btRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initDataProfil();
                    }
                });
            }
        });
        Call<SummaryKolektorPojo> summaryKolektorPojoCall = servicePojo.getSummaryKolektor();
        summaryKolektorPojoCall.enqueue(new Callback<SummaryKolektorPojo>() {
            @Override
            public void onResponse(Call<SummaryKolektorPojo> call, Response<SummaryKolektorPojo> response) {
                if (response.body()!=null){
                    TxvSimpanan.setText(response.body().getSimpanan().toString());
                    TxvNasabah.setText(response.body().getPinjaman().toString());
                    TxvPinjaman.setText(response.body().getPinjaman().toString());
                }

            }

            @Override
            public void onFailure(Call<SummaryKolektorPojo> call, Throwable t) {

            }
        });
    }

    class ProfilKolektorPojoResponse{
        @SerializedName("status")
        Integer status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        KolektorPojo data;

        public ProfilKolektorPojoResponse(Integer status, String msg, KolektorPojo data) {
            this.status = status;
            this.msg = msg;
            this.data = data;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public KolektorPojo getData() {
            return data;
        }

        public void setData(KolektorPojo data) {
            this.data = data;
        }
    }
}
