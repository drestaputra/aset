package dresta.putra.aset.angsuran;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.github.razir.progressbutton.ProgressParams;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import dresta.putra.aset.R;
import dresta.putra.aset.ResponsePojo;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.pinjaman.DetailPinjamanActivity;
import dresta.putra.aset.pinjaman.PinjamanPojo;
import dresta.putra.aset.simpanan.SimpananPojo;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class BayarAngsuranActivity extends AppCompatActivity {
    private Spinner SpIdPinjaman;
    private List<IdPinjamanPojo> idPinjamanPojos;
    private TextView SpError,TxvTerbayar, TxvKekurangan, TxvAngsuran, TxvPeriode, TxvLamaAngsuran, TxvTglPinjaman,TxvNamaNasabah, TxvJumlahPinjaman, TxvIdPinjaman,TxvJumlahSimpanan,TxvIdSimpanan,TxvLastUpdate,TxvTglSimpanan;
    private ImageView IvNasabah;
    private EditText EtJumlahAngsuran,EtKeterangan,EtJumlahSimpanan;
    private LinearLayout LlAmbilSimpanan;
    private Button BtnDetail,BtnBayar;
    private Switch SwAmbilSimpanan;
    private String id_nasabah;
    private Float jumlah_max_simpanan,jumlah_max_angsuran;
    interface APIBayarAngsuran{
        @FormUrlEncoded
        @POST("api/pinjaman/bayar_angsuran")
        Call<ResponsePojo> bayarAngsuran(@Field("id_pinjaman") String id_pinjaman, @Field("jumlah_pembayaran") String jumlah_pembayaran, @Field("keterangan") String keterangan,@Field("jumlah_simpanan") String jumlah_simpanan);
        @GET("api/pinjaman/get_all_id_pinjaman")
        Call<IdPinjamanResponsePojo> getAllIdPinjaman();
        @FormUrlEncoded
        @POST("api/pinjaman/detail_pinjaman")
        Call<PinjamanPojo> getDetailPinjaman(@Field("id_pinjaman") String id_pinjaman);
        @FormUrlEncoded
        @POST("api/simpanan/detail_simpanan_by_id_nasabah")
        Call<SimpananPojo> getDetailSimpananByIdNasabah(@Field("id_nasabah") String id_nasabah);
    }
    private APIBayarAngsuran apiBayarAngsuran;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar_angsuran);
        Toolbar toolbar = findViewById(R.id.toolbar);
        EtJumlahAngsuran =  findViewById(R.id.EtJumlahAngsuran);
        EtKeterangan =  findViewById(R.id.EtKeterangan);
        TxvTerbayar =  findViewById(R.id.TxvTerbayar);
        TxvKekurangan =  findViewById(R.id.TxvKekurangan);
        TxvAngsuran =  findViewById(R.id.TxvAngsuran);
        TxvPeriode =  findViewById(R.id.TxvPeriode);
        TxvLamaAngsuran =  findViewById(R.id.TxvLamaAngsuran);
        TxvTglPinjaman =  findViewById(R.id.TxvTglPinjaman);
        TxvNamaNasabah =  findViewById(R.id.TxvNamaNasabah);
        TxvJumlahPinjaman =  findViewById(R.id.TxvJumlahPinjaman);
        TxvIdPinjaman =  findViewById(R.id.TxvIdPinjaman);
        IvNasabah =  findViewById(R.id.IvNasabah);
        BtnDetail = findViewById(R.id.BtnDetail);
        BtnBayar = findViewById(R.id.BtnBayar);
        TxvJumlahSimpanan = findViewById(R.id.TxvJumlahSimpanan);
        TxvIdSimpanan = findViewById(R.id.TxvIdSimpanan);
        TxvLastUpdate = findViewById(R.id.TxvLastUpdate);
        TxvTglSimpanan = findViewById(R.id.TxvTglSimpanan);
        LlAmbilSimpanan = findViewById(R.id.LlAmbilSimpanan);
        EtJumlahSimpanan = findViewById(R.id.EtJumlahSimpanan);
        BtnBayar.setEnabled(false);
        SpError = findViewById(R.id.SpError);
        BtnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bayarAngsuran(BtnBayar);
            }
        });
        SwAmbilSimpanan = findViewById(R.id.SwAmbilSimpanan);
        SwAmbilSimpanan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if  (isChecked){
                    initDataSimpanan();
                }else{
                    LlAmbilSimpanan.setVisibility(View.GONE);
                    TxvJumlahSimpanan.setText("");
                }
            }
        });
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SpIdPinjaman = findViewById(R.id.SpIdPinjaman);
        apiBayarAngsuran = RetrofitClientInstance.getRetrofitInstance(BayarAngsuranActivity.this).create(APIBayarAngsuran.class);
        Call<IdPinjamanResponsePojo> idPinjamanResponsePojoCall = apiBayarAngsuran.getAllIdPinjaman();
        idPinjamanResponsePojoCall.enqueue(new Callback<IdPinjamanResponsePojo>() {
            @Override
            public void onResponse(Call<IdPinjamanResponsePojo> call, Response<IdPinjamanResponsePojo> response) {
                if (response.body()!=null){
                    if (response.body().getStatus() == 200){
                        idPinjamanPojos = response.body().getData();
                        List<String> IdPinjamanUn = new ArrayList<String>();
                        for (int i = 0; i < idPinjamanPojos.size(); i++){
                            IdPinjamanUn.add(idPinjamanPojos.get(i).getId_pinjaman().toUpperCase()+" - "+idPinjamanPojos.get(i).getNama_nasabah()+" @"
                                    +idPinjamanPojos.get(i).getJumlah_perangsuran());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                R.layout.spinner_black, IdPinjamanUn);
                            adapter.setDropDownViewResource(R.layout.spinner_black);
                            SpIdPinjaman.setAdapter(adapter);
                        BtnBayar.setEnabled(true);
                    }
                }
            }
            @Override
            public void onFailure(Call<IdPinjamanResponsePojo> call, Throwable t) {

            }
        });
        SpIdPinjaman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String id_pinjaman = idPinjamanPojos.get(position).getId_pinjaman();
                Call<PinjamanPojo> pinjamanPojoCall = apiBayarAngsuran.getDetailPinjaman(id_pinjaman);
                pinjamanPojoCall.enqueue(new Callback<PinjamanPojo>() {
                    @Override
                    public void onResponse(Call<PinjamanPojo> call, Response<PinjamanPojo> response) {
                        if(response.body()!=null){

                            Locale localeID = new Locale("in", "ID");
                            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                            final PinjamanPojo result = response.body();
                            id_nasabah = result.getId_nasabah();
                            Float jumlahTerbayar = Float.parseFloat(result.getJumlah_terbayar());
                            Float jumlahPinjaman = Float.parseFloat(result.getJumlah_pinjaman_setelah_bunga());
                            jumlah_max_angsuran = jumlahPinjaman-jumlahTerbayar;
                            TxvNamaNasabah.setText(result.getNasabah().getNama_nasabah());
                            TxvIdPinjaman.setText("#"+result.getId_pinjaman());
                            TxvJumlahPinjaman.setText(formatRupiah.format(Float.parseFloat(result.getJumlah_pinjaman_setelah_bunga())));
                            TxvTerbayar.setText("Terbayar : "+ formatRupiah.format(Float.parseFloat(result.getJumlah_terbayar())));
                            float kurang = Float.parseFloat(result.getJumlah_pinjaman_setelah_bunga()) - Float.parseFloat(result.getJumlah_terbayar());
                            TxvKekurangan.setText("Kekurangan : "+ formatRupiah.format(kurang));
                            TxvAngsuran.setText("Angsuran : @ "+ formatRupiah.format(Float.parseFloat(result.getJumlah_perangsuran())));
                            TxvPeriode.setText("Periode : "+result.getPeriode_angsuran());
                            TxvLamaAngsuran.setText("Lama Angsuran : "+ result.getLama_angsuran());
                            TxvTglPinjaman.setText("Tgl. Pinjaman "+ result.getTgl_pinjaman());
                            if (result.getNasabah().getFoto_nasabah() != null && result.getNasabah().getFoto_nasabah().length() > 0) {
                                Picasso.with(getApplicationContext()).load(result.getNasabah().getFoto_nasabah()).placeholder(R.color.greycustom2).into(IvNasabah);
                            } else {
                                Picasso.with(getApplicationContext()).load(R.color.greycustom2).into(IvNasabah);
                                Picasso.with(getApplicationContext()).load(R.drawable.ic_user).placeholder(R.color.greycustom2).into(IvNasabah);
                            }
                            BtnDetail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent Idetail = new Intent(BayarAngsuranActivity.this, DetailPinjamanActivity.class);
                                    Idetail.putExtra("id_pinjaman",result.getId_pinjaman());
                                    startActivity(Idetail);
                                }
                            });
                            initDataSimpanan();
                        }
                    }

                    @Override
                    public void onFailure(Call<PinjamanPojo> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private void initDataSimpanan(){
        if (SwAmbilSimpanan.isChecked()){
            Call<SimpananPojo> pinjamanPojoCall = apiBayarAngsuran.getDetailSimpananByIdNasabah(id_nasabah);
            pinjamanPojoCall.enqueue(new Callback<SimpananPojo>() {
                @Override
                public void onResponse(Call<SimpananPojo> call, Response<SimpananPojo> response) {
                    if(response.body()!=null){
                        if  (!response.body().getJumlah_simpanan().equals("0")) {
                            LlAmbilSimpanan.setVisibility(View.VISIBLE);
                            final SimpananPojo result = response.body();
                            jumlah_max_simpanan = Float.parseFloat(result.getJumlah_simpanan());
                            TxvIdSimpanan.setText("ID Simpanan : #" + result.getId_simpanan());
                            Locale localeID = new Locale("in", "ID");
                            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                            TxvJumlahSimpanan.setText(formatRupiah.format(Float.parseFloat(result.getJumlah_simpanan())));
                            TxvTglSimpanan.setText("Tgl. buka simpanan : " + result.getTgl_simpanan());
                            TxvLastUpdate.setText("Tgl. terakhir simpanan : " + result.getLast_update());
                        }else{
                            Toast.makeText(BayarAngsuranActivity.this, "Data simpanan masih kosong", Toast.LENGTH_SHORT).show();
                            TxvJumlahSimpanan.setText("");
                        }
                    }
                }
                @Override
                public void onFailure(Call<SimpananPojo> call, Throwable t) {

                }
            });
        }else{
            LlAmbilSimpanan.setVisibility(View.GONE);
            TxvJumlahSimpanan.setText("");
        }
    }
    private void bayarAngsuran(final Button button){
        DrawableButtonExtensionsKt.showProgress(button, new Function1<ProgressParams, Unit>() {
            @Override
            public Unit invoke(ProgressParams progressParams) {
                progressParams.setButtonTextRes(R.string.loading);
                progressParams.setProgressColor(Color.WHITE);
                return Unit.INSTANCE;
            }
        });

        button.setEnabled(false);
        final String SpIdPinjamans = (idPinjamanPojos.size()!=0) ? idPinjamanPojos.get(SpIdPinjaman.getSelectedItemPosition()).getId_pinjaman() : null;
        final String EtJumlahAngsurans = EtJumlahAngsuran.getText().toString();
        final String EtKeterangans = EtKeterangan.getText().toString();
        final String EtJumlahSimpanans = EtJumlahSimpanan.getText().toString();
        if (TextUtils.isEmpty(SpIdPinjamans)) {
            SpError.setText("ID Pinjaman tidak ditemukan");
            SpError.setTextColor(Color.RED);
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Kirim");
            return;
        }
        if (TextUtils.isEmpty(EtJumlahAngsurans)) {
            EtJumlahAngsuran.setError("Jumlah Angsuran masih kosong");
            EtJumlahAngsuran.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Kirim");
            return;
        }
        if (!TextUtils.isEmpty(EtJumlahAngsurans) && Float.parseFloat(EtJumlahAngsurans)>jumlah_max_angsuran){
            EtJumlahAngsuran.setError("Jumlah Angsuran melebihi batas pelunasan");
            EtJumlahAngsuran.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Kirim");
            return;
        }
        if (!TextUtils.isEmpty(EtJumlahSimpanans) && Float.parseFloat(EtJumlahSimpanans)>jumlah_max_simpanan) {
            EtJumlahSimpanan.setError("Jumlah uang yang diambil dari simpanan melebihi batas");
            EtJumlahSimpanan.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Kirim");
            return;
        }
        if (!TextUtils.isEmpty(EtJumlahSimpanans) && Float.parseFloat(EtJumlahSimpanans)>jumlah_max_simpanan){
            EtJumlahSimpanan.setError("Jumlah uang yang diambil dari simpanan melebihi batas");
            EtJumlahSimpanan.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Kirim");
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<ResponsePojo> responsePojoCall = apiBayarAngsuran.bayarAngsuran(SpIdPinjamans,EtJumlahAngsurans,EtKeterangans, EtJumlahSimpanans);
                responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                    @Override
                    public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                        if (response.body() != null){
                            if (response.body().getStatus()==200){
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, R.string.progressRight);
                                finish();
                                Toast.makeText(BayarAngsuranActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }else{
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, R.string.progressRight);
                                Toast.makeText(BayarAngsuranActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsePojo> call, Throwable t) {
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, "Gagal, coba lagi");
                        Toast.makeText(BayarAngsuranActivity.this, "Terjadi gangguan jaringan", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        },1000);


    }
    class IdPinjamanResponsePojo{
        @SerializedName("status")
        Integer status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        List<IdPinjamanPojo> data;

        public IdPinjamanResponsePojo(Integer status, String msg, List<IdPinjamanPojo> data) {
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

        public List<IdPinjamanPojo> getData() {
            return data;
        }

        public void setData(List<IdPinjamanPojo> data) {
            this.data = data;
        }
    }
    class IdPinjamanPojo{
        @SerializedName("id_pinjaman")
        String id_pinjaman;
        @SerializedName("id_nasabah")
        String id_nasabah;
        @SerializedName("nama_nasabah")
        String nama_nasabah;
        @SerializedName("jumlah_perangsuran")
        String jumlah_perangsuran;

        public IdPinjamanPojo() {
        }

        public String getId_pinjaman() {
            return id_pinjaman;
        }

        public void setId_pinjaman(String id_pinjaman) {
            this.id_pinjaman = id_pinjaman;
        }

        public String getId_nasabah() {
            return id_nasabah;
        }

        public void setId_nasabah(String id_nasabah) {
            this.id_nasabah = id_nasabah;
        }

        public String getNama_nasabah() {
            return nama_nasabah;
        }

        public void setNama_nasabah(String nama_nasabah) {
            this.nama_nasabah = nama_nasabah;
        }

        public String getJumlah_perangsuran() {
            return jumlah_perangsuran;
        }

        public void setJumlah_perangsuran(String jumlah_perangsuran) {
            this.jumlah_perangsuran = jumlah_perangsuran;
        }
    }
}
