package dresta.putra.aset.simpanan;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class SimpanUangActivity extends AppCompatActivity {
    private Spinner SpIdSimpanan;
    private Button BtnSimpan,BtnDetail;
    private ImageView IvNasabah;
    private TextView TxvNamaNasabah,TxvJumlahSimpanan,TxvTglSimpanan,TxvIdSimpanan,TxvLastUpdate,TxvAlamat,SpError;
    private List<IdSimpananPojo> idSimpananPojos = new ArrayList<IdSimpananPojo>();
    private String id_simpanaIntent;
    private EditText EtJumlahSimpan,EtKeterangan;
    private Locale localeID;
    private NumberFormat formatRupiah;
    interface APISimpanUang{
        @FormUrlEncoded
        @POST("api/simpanan/tambah_simpan")
        Call<ResponsePojo> tambahSimpan(@Field("id_simpanan") String id_pinjaman, @Field("jumlah_riwayat_simpanan") String jumlah_riwayat_simpanan, @Field("keterangan") String keterangan);
        @FormUrlEncoded
        @POST("api/simpanan/get_all_id_simpanan")
        Call<IdSimpananResponsePojo> getAllIdSimpanan(@Field("id_simpanan") String id_simpanan);
        @FormUrlEncoded
        @POST("api/simpanan/detail_simpanan")
        Call<SimpananPojo> getDetailSimpanan(@Field("id_simpanan") String id_simpanan);
    }
    private APISimpanUang apiSimpanUang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpan_uang);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SpIdSimpanan = findViewById(R.id.SpIdSimpanan);
        BtnSimpan = findViewById(R.id.BtnSimpan);
        TxvNamaNasabah = findViewById(R.id.TxvNamaNasabah);
        TxvJumlahSimpanan = findViewById(R.id.TxvJumlahSimpanan);
        TxvTglSimpanan = findViewById(R.id.TxvTglSimpanan);
        TxvIdSimpanan = findViewById(R.id.TxvIdSimpanan);
        BtnDetail = findViewById(R.id.BtnDetail);
        IvNasabah = findViewById(R.id.IvNasabah);
        TxvLastUpdate = findViewById(R.id.TxvLastUpdate);
        TxvAlamat = findViewById(R.id.TxvAlamat);
        EtJumlahSimpan = findViewById(R.id.EtJumlahSimpan);
        SpError = findViewById(R.id.SpError);
        EtKeterangan = findViewById(R.id.EtKeterangan);
        if (getIntent().getExtras() != null){
            id_simpanaIntent = getIntent().getStringExtra("id_simpanan");
        }
        localeID = new Locale("in", "ID");
        formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        apiSimpanUang = RetrofitClientInstance.getRetrofitInstance(SimpanUangActivity.this).create(APISimpanUang.class);
        Call<IdSimpananResponsePojo> idSimpananResponsePojoCall = apiSimpanUang.getAllIdSimpanan(id_simpanaIntent);
        idSimpananResponsePojoCall.enqueue(new Callback<IdSimpananResponsePojo>() {
            @Override
            public void onResponse(Call<IdSimpananResponsePojo> call, Response<IdSimpananResponsePojo> response) {
                if (response.body()!=null){
                    if (response.body().getStatus() == 200){
                        idSimpananPojos = response.body().getData();
                        List<String> IdPinjamanUn = new ArrayList<String>();
                        for (int i = 0; i < idSimpananPojos.size(); i++){
                            IdPinjamanUn.add(idSimpananPojos.get(i).getId_simpanan().toUpperCase()+" - "+idSimpananPojos.get(i).getNama_nasabah()+" - "
                                    +formatRupiah.format(Float.parseFloat(idSimpananPojos.get(i).getJumlah_simpanan())));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                R.layout.spinner_black, IdPinjamanUn);
                        adapter.setDropDownViewResource(R.layout.spinner_black);
                        SpIdSimpanan.setAdapter(adapter);
                        BtnSimpan.setEnabled(true);
                    }
                }
            }
            @Override
            public void onFailure(Call<IdSimpananResponsePojo> call, Throwable t) {

            }
        });

        SpIdSimpanan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String id_simpanan = idSimpananPojos.get(position).getId_simpanan();
                Call<SimpananPojo> pinjamanPojoCall = apiSimpanUang.getDetailSimpanan(id_simpanan);
                pinjamanPojoCall.enqueue(new Callback<SimpananPojo>() {
                    @Override
                    public void onResponse(Call<SimpananPojo> call, Response<SimpananPojo> response) {
                        if(response.body()!=null){
                            final SimpananPojo result = response.body();
                            TxvNamaNasabah.setText(result.getNasabah().getNama_nasabah());
                            TxvIdSimpanan.setText("ID Simpanan : #"+result.getId_simpanan());
                            TxvJumlahSimpanan.setText(formatRupiah.format(Float.parseFloat(result.getJumlah_simpanan())));
                            TxvTglSimpanan.setText("Tgl. buka simpanan : "+ result.getTgl_simpanan());
                            TxvLastUpdate.setText("Tgl. terakhir simpanan : "+ result.getLast_update());
                            TxvAlamat.setText("Alamat : "+ result.getNasabah().getAlamat_rumah());
                            if (result.getNasabah().getFoto_nasabah() != null && result.getNasabah().getFoto_nasabah().length() > 0) {
                                Picasso.with(getApplicationContext()).load(result.getNasabah().getFoto_nasabah()).placeholder(R.color.greycustom2).into(IvNasabah);
                            } else {
                                Picasso.with(getApplicationContext()).load(R.color.greycustom2).into(IvNasabah);
                                Picasso.with(getApplicationContext()).load(R.drawable.ic_user).placeholder(R.color.greycustom2).into(IvNasabah);
                            }
                            BtnDetail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent Idetail = new Intent(SimpanUangActivity.this,RiwayatSimpananActivity.class);
                                    Idetail.putExtra("id_simpanan",result.getId_simpanan());
                                    startActivity(Idetail);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<SimpananPojo> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        BtnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanUang(BtnSimpan);
            }
        });
    }
    private void simpanUang(final Button button){
        DrawableButtonExtensionsKt.showProgress(button, new Function1<ProgressParams, Unit>() {
            @Override
            public Unit invoke(ProgressParams progressParams) {
                progressParams.setButtonTextRes(R.string.loading);
                progressParams.setProgressColor(Color.WHITE);
                return Unit.INSTANCE;
            }
        });

        button.setEnabled(false);
        final String SpIdPinjamans = (idSimpananPojos.size()!=0) ? idSimpananPojos.get(SpIdSimpanan.getSelectedItemPosition()).getId_simpanan() : null;
        final String EtJumlahSimpans = EtJumlahSimpan.getText().toString();
        final String EtKeterangans = EtKeterangan.getText().toString();
        if (TextUtils.isEmpty(SpIdPinjamans)) {
            SpError.setText("ID Pinjaman tidak ditemukan");
            SpError.setTextColor(Color.RED);
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Kirim");
            return;
        }
        if (TextUtils.isEmpty(EtJumlahSimpans)) {
            EtJumlahSimpan.setError("Jumlah uang yang ingin disimpan masih kosong");
            EtJumlahSimpan.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Kirim");
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<ResponsePojo> responsePojoCall = apiSimpanUang.tambahSimpan(SpIdPinjamans,EtJumlahSimpans,EtKeterangans);
                responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                    @Override
                    public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                        if (response.body() != null){
                            if (response.body().getStatus()==200){
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, R.string.progressRight);
                                finish();
                                Toast.makeText(SimpanUangActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }else{
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, R.string.progressRight);
                                Toast.makeText(SimpanUangActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsePojo> call, Throwable t) {
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, "Gagal, coba lagi");
                        Toast.makeText(SimpanUangActivity.this, "Terjadi gangguan jaringan", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        },1000);
    }
    class IdSimpananPojo{
        @SerializedName("id_simpanan")
        String id_simpanan;
        @SerializedName("id_nasabah")
        String id_nasabah;
        @SerializedName("nama_nasabah")
        String nama_nasabah;
        @SerializedName("jumlah_simpanan")
        String jumlah_simpanan;

        public IdSimpananPojo(String id_simpanan, String id_nasabah, String nama_nasabah, String jumlah_simpanan) {
            this.id_simpanan = id_simpanan;
            this.id_nasabah = id_nasabah;
            this.nama_nasabah = nama_nasabah;
            this.jumlah_simpanan = jumlah_simpanan;
        }

        public IdSimpananPojo() {
        }

        public String getId_simpanan() {
            return id_simpanan;
        }

        public void setId_simpanan(String id_simpanan) {
            this.id_simpanan = id_simpanan;
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

        public String getJumlah_simpanan() {
            return jumlah_simpanan;
        }

        public void setJumlah_simpanan(String jumlah_simpanan) {
            this.jumlah_simpanan = jumlah_simpanan;
        }
    }
    class IdSimpananResponsePojo{
        @SerializedName("status")
        Integer status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        List<IdSimpananPojo> data;

        public IdSimpananResponsePojo(Integer status, String msg, List<IdSimpananPojo> data) {
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

        public List<IdSimpananPojo> getData() {
            return data;
        }

        public void setData(List<IdSimpananPojo> data) {
            this.data = data;
        }
    }
}
