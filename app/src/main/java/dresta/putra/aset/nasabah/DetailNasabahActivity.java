package dresta.putra.aset.nasabah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.oper_berkas.OperActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class DetailNasabahActivity extends AppCompatActivity {
    private String id_nasabah;
    private TextView  TxvUsername, TxvEmail, TxvNamaNasabah, TxvNoHp,  TxvJenisKelamin, TxvTempatTglLahir, TxvAlamatRumah, TxvProvinsi, TxvKabupaten, TxvKecamatan, TxvKelurahan, Txvwarga_negara, TxvPekerjaan, TxvAlamatTempatKerja, TxvNamaUsaha, TxvAgama, TxvGolonganDarah, TxvHobi, TxvStatus, TxvTglBergabung,TxvPinjaman, TxvAngsuran ,TxvSimpanan, TxvMakananKesukaan;
    private ImageView IvFotoNasabah,IvBack,IvEdit, IvRiwayatNasabah;
    interface APIDetailNasabah{
        @FormUrlEncoded
        @POST("api/nasabah/detail_nasabah")
        Call<NasabahPojo> getDetailNasabah(@Field("id_nasabah") String id_nasabah);
        @FormUrlEncoded
        @POST("api/nasabah/balance_nasabah")
        Call<BalanceNasabahPojo> getDetailBalance(@Field("id_nasabah") String id_nasabah);
    }
    private APIDetailNasabah apiDetailNasabah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_nasabah);
        final Locale currentLocale = new Locale("id", "id");
        TxvPinjaman = findViewById(R.id.TxvPinjaman);
        IvEdit = findViewById(R.id.IvEdit);
        IvBack = findViewById(R.id.IvBack);
        IvRiwayatNasabah = findViewById(R.id.IvRiwayatNasabah);
        TxvAngsuran = findViewById(R.id.TxvAngsuran);
        TxvSimpanan = findViewById(R.id.TxvSimpanan);
        TxvUsername = findViewById(R.id.TxvUsername);
        TxvEmail = findViewById(R.id.TxvEmail);
        TxvNamaNasabah = findViewById(R.id.TxvNamaNasabah);
        TxvNoHp = findViewById(R.id.TxvNoHp);
        TxvJenisKelamin = findViewById(R.id.TxvJenisKelamin);
        TxvTempatTglLahir = findViewById(R.id.TxvTempatTglLahir);
        TxvAlamatRumah = findViewById(R.id.TxvAlamatRumah);
        TxvProvinsi = findViewById(R.id.TxvProvinsi);
        TxvKabupaten = findViewById(R.id.TxvKabupaten);
        TxvKecamatan = findViewById(R.id.TxvKecamatan);
        TxvKelurahan = findViewById(R.id.TxvKelurahan);
        Txvwarga_negara = findViewById(R.id.Txvwarga_negara);
        TxvPekerjaan = findViewById(R.id.TxvPekerjaan);
        TxvAlamatTempatKerja = findViewById(R.id.TxvAlamatTempatKerja);
        TxvNamaUsaha = findViewById(R.id.TxvNamaUsaha);
        TxvAgama = findViewById(R.id.TxvAgama);
        TxvGolonganDarah = findViewById(R.id.TxvGolonganDarah);
        TxvHobi = findViewById(R.id.TxvHobi);
        TxvStatus = findViewById(R.id.TxvStatus);
        TxvTglBergabung = findViewById(R.id.TxvTglBergabung);
        IvFotoNasabah = findViewById(R.id.IvFotoNasabah);
        TxvMakananKesukaan = findViewById(R.id.TxvMakananKesukaan);
        id_nasabah = getIntent().getStringExtra("id_nasabah");
        Locale localeID = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        apiDetailNasabah = RetrofitClientInstance.getRetrofitInstance(DetailNasabahActivity.this).create(APIDetailNasabah.class);
        Call<BalanceNasabahPojo> balanceNasabahPojoCall = apiDetailNasabah.getDetailBalance(id_nasabah);
        balanceNasabahPojoCall.enqueue(new Callback<BalanceNasabahPojo>() {
            @Override
            public void onResponse(Call<BalanceNasabahPojo> call, Response<BalanceNasabahPojo> response) {
                if (response.body()!= null){
                    TxvSimpanan.setText(formatRupiah.format(response.body().getSimpanan()));
                    TxvAngsuran.setText(formatRupiah.format(response.body().getAngsuran()));
                    TxvPinjaman.setText(formatRupiah.format(response.body().getPinjaman()));
                }
            }

            @Override
            public void onFailure(Call<BalanceNasabahPojo> call, Throwable t) {

            }
        });
        initDataProfil();
        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        IvRiwayatNasabah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IRiwayat = new Intent(DetailNasabahActivity.this, RiwayatNasabahActivity.class);
                IRiwayat.putExtra("id_nasabah", id_nasabah);
                overridePendingTransition(0,0);
                startActivity(IRiwayat);
            }
        });
        ImageView IvOper = findViewById(R.id.IvOper);
        IvOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IEdit = new Intent(DetailNasabahActivity.this, OperActivity.class);
                IEdit.putExtra("id_nasabah",id_nasabah);
                startActivity(IEdit);
                overridePendingTransition(0,0);
            }
        });
        IvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IEdit = new Intent(DetailNasabahActivity.this,EditNasabahActivity.class);
                IEdit.putExtra("id_nasabah",id_nasabah);
                startActivity(IEdit);
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
        Call<NasabahPojo> nasabahPojoCall = apiDetailNasabah.getDetailNasabah(id_nasabah);
        nasabahPojoCall.enqueue(new Callback<NasabahPojo>() {
            @Override
            public void onResponse(Call<NasabahPojo> call, Response<NasabahPojo> response) {
                if (response.body()!=null){
                    if (!response.body().getId_nasabah().equals("")){
                        NasabahPojo result = response.body();
                        if (result.getFoto_nasabah() != null && result.getFoto_nasabah().length() > 0) {
                            Picasso.with(DetailNasabahActivity.this).load(result.getFoto_nasabah()).placeholder(R.color.greycustom2).into(IvFotoNasabah);
                        }
                        TxvUsername.setText(result.getUsername());
                        TxvEmail.setText(result.getEmail());
                        TxvNamaNasabah.setText(result.getNama_nasabah());
                        TxvNoHp.setText(result.getNo_hp());
                        TxvJenisKelamin.setText(result.getJenis_kelamin());
                        TxvTempatTglLahir.setText(result.getTempat_lahir()+", "+result.getTanggal_lahir());
                        TxvAlamatRumah.setText(result.getAlamat_rumah());
                        TxvProvinsi.setText(result.getProvinsi());
                        TxvKabupaten.setText(result.getKabupaten());
                        TxvKecamatan.setText(result.getKecamatan());
                        TxvKelurahan.setText(result.getKelurahan());
                        Txvwarga_negara.setText(result.getWarga_negara());
                        TxvPekerjaan.setText(result.getPekerjaan());
                        TxvAlamatTempatKerja.setText(result.getAlamat_tempat_kerja());
                        TxvNamaUsaha.setText(result.getNama_usaha());
                        TxvAgama.setText(result.getAgama());
                        TxvGolonganDarah.setText(result.getGolongan_darah());
                        TxvHobi.setText(result.getHobi());
                        TxvMakananKesukaan.setText(result.getMakanan_kesukaan());
                        TxvStatus.setText(result.getStatus());
                        TxvTglBergabung.setText(result.getTgl_bergabung());

                    }
                }
            }

            @Override
            public void onFailure(Call<NasabahPojo> call, Throwable t) {

            }
        });
    }
}
