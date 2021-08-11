package dresta.putra.aset.pinjaman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.angsuran.BayarAngsuranActivity;
import dresta.putra.aset.angsuran.RiwayatAngsuranActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class DetailPinjamanActivity extends AppCompatActivity {
    private String id_pinjaman;
    private ImageView IvBack,IvNasabah,IvRiwayat;
    private TextView TxvJumlahPinjaman,TxvJumlahDiterima,TxvPersentaseBunga,TxvJumlahPinjamanSetelahBunga,TxvJumlahTerbayar,TxvPeriodeAngsuran,
            TxvJumlahPerangsuran,TxvTglPinjaman,TxvTglTerakhirAngsuran,TxvAngsuranKe,TxvNamaNasabah;
    private Button BtnOper;
    interface APIDetailPinjaman{
        @FormUrlEncoded
        @POST("api/pinjaman/detail_pinjaman")
        Call<PinjamanPojo> getDetailPinjaman(@Field("id_pinjaman") String id_pinjaman);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pinjaman);
        TxvJumlahPinjaman = findViewById(R.id.TxvJumlahPinjaman);
        TxvJumlahDiterima = findViewById(R.id.TxvJumlahDiterima);
        TxvPersentaseBunga = findViewById(R.id.TxvPersentaseBunga);
        TxvJumlahPinjamanSetelahBunga = findViewById(R.id.TxvJumlahPinjamanSetelahBunga);
        TxvJumlahTerbayar = findViewById(R.id.TxvJumlahTerbayar);
        TxvPeriodeAngsuran = findViewById(R.id.TxvPeriodeAngsuran);
        TxvJumlahPerangsuran = findViewById(R.id.TxvJumlahPerangsuran);
        TxvTglPinjaman = findViewById(R.id.TxvTglPinjaman);
        TxvTglTerakhirAngsuran = findViewById(R.id.TxvTglTerakhirAngsuran);
        TxvAngsuranKe = findViewById(R.id.TxvAngsuranKe);
        TxvNamaNasabah = findViewById(R.id.TxvNamaNasabah);
        IvNasabah = findViewById(R.id.IvNasabah);
        IvRiwayat = findViewById(R.id.IvRiwayat);
        final Button BtnAngsur  = findViewById(R.id.BtnAngsur);
        id_pinjaman = getIntent().getStringExtra("id_pinjaman");
        APIDetailPinjaman apiDetailPinjaman = RetrofitClientInstance.getRetrofitInstance(DetailPinjamanActivity.this).create(APIDetailPinjaman.class);
        Call<PinjamanPojo> pinjamanPojoCall = apiDetailPinjaman.getDetailPinjaman(id_pinjaman);
        Locale localeID = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        pinjamanPojoCall.enqueue(new Callback<PinjamanPojo>() {
            @Override
            public void onResponse(Call<PinjamanPojo> call, Response<PinjamanPojo> response) {
                if (response.body()!=null){
                    PinjamanPojo data = response.body();
                    TxvNamaNasabah.setText(data.getNasabah().getNama_nasabah());
                    TxvJumlahPinjaman.setText(formatRupiah.format(Float.parseFloat(data.getJumlah_pinjaman())));
                    TxvJumlahDiterima.setText(formatRupiah.format(Float.parseFloat(data.getJumlah_diterima())));
                    TxvPersentaseBunga.setText(data.getPersentase_bunga());
                    TxvJumlahPinjamanSetelahBunga.setText(formatRupiah.format(Float.parseFloat(data.getJumlah_pinjaman_setelah_bunga())));
                    TxvJumlahTerbayar.setText(formatRupiah.format(Float.parseFloat(data.getJumlah_terbayar())));
                    TxvPeriodeAngsuran.setText(data.getLama_angsuran()+" x "+data.getPeriode_angsuran());
                    TxvJumlahPerangsuran.setText(formatRupiah.format(Float.parseFloat(data.getJumlah_perangsuran())));
                    TxvTglPinjaman.setText(data.getTgl_pinjaman());
                    TxvTglTerakhirAngsuran.setText(data.getTgl_terakhir_angsuran());
                    TxvAngsuranKe.setText(data.getAngsuran_ke());
                    if (data.getNasabah().getFoto_nasabah() != null && data.getNasabah().getFoto_nasabah().length() > 0) {
                        Picasso.with(getApplicationContext()).load(data.getNasabah().getFoto_nasabah()).placeholder(R.color.greycustom2).into(IvNasabah);
                    } else {
                        Picasso.with(getApplicationContext()).load(R.color.greycustom2).into(IvNasabah);
                        Picasso.with(getApplicationContext()).load(R.drawable.ic_user).placeholder(R.color.greycustom2).into(IvNasabah);
                    }
                    if (data.getStatus_pinjaman().equals("lunas")){
                        BtnAngsur.setVisibility(View.GONE);
                    }else{
                        BtnAngsur.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<PinjamanPojo> call, Throwable t) {

            }
        });
        IvBack = findViewById(R.id.IvBack);
        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        BtnAngsur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IAngsur = new Intent(DetailPinjamanActivity.this, BayarAngsuranActivity.class);
                IAngsur.putExtra("id_pinjaman",id_pinjaman);
                startActivity(IAngsur);
                overridePendingTransition(0, 0);
            }
        });
        IvRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IAngsur = new Intent(DetailPinjamanActivity.this, RiwayatAngsuranActivity.class);
                IAngsur.putExtra("id_pinjaman",id_pinjaman);
                startActivity(IAngsur);
                overridePendingTransition(0, 0);
            }
        });
    }
}
