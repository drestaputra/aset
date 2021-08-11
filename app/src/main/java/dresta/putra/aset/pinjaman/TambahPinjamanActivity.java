package dresta.putra.aset.pinjaman;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.github.razir.progressbutton.ProgressParams;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dresta.putra.aset.R;
import dresta.putra.aset.ResponsePojo;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.nasabah.DetailNasabahActivity;
import dresta.putra.aset.nasabah.IdNasabahResponsePojo;
import dresta.putra.aset.nasabah.NasabahPojo;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class TambahPinjamanActivity extends AppCompatActivity {
    private List<NasabahPojo> nasabahPojos;
    private Spinner SpIdNasabah;
    private Button BtnTambah,BtnDetail;
    private TextView TxvNamaNasabah,TxvEmail,TxvNoHp,TxvTglBergabung,TxvAlamat,SpError;
    private EditText EtJumlahPinjaman,EtNote,EtLamaAngsuran;
    private ImageView IvNasabah;
    String terpilih = "harian";
    private Button[] btn = new Button[4];
    private Button btn_unfocus;
    private int[] btn_id = {R.id.BtnHarian, R.id.BtnMingguan, R.id.BtnBulanan, R.id.BtnTahunan};
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    interface APITambahPinjaman{
        @FormUrlEncoded
        @POST("api/pinjaman/validasi_tambah_pinjaman")
        Call<ResponsePojoValidasiTambahPinjaman> tambahPinjaman(
                @Field("id_nasabah") String id_nasabah,
                @Field("jumlah_pinjaman") String jumlah_pinjaman,
                @Field("periode_angsuran") String periode_angsuran,
                @Field("lama_angsuran") String lama_angsuran,
                @Field("note") String note
                );
        @FormUrlEncoded
        @POST("api/pinjaman/tambah_pinjaman")
        Call<ResponsePojo> tambahPinjamanSave(
                @Field("id_nasabah") String id_nasabah,
                @Field("jumlah_pinjaman") String jumlah_pinjaman,
                @Field("periode_angsuran") String periode_angsuran,
                @Field("lama_angsuran") String lama_angsuran,
                @Field("note") String note
        );
        @GET("api/pinjaman/get_all_id_nasabah_for_pinjaman")
        Call<IdNasabahResponsePojo> getAllIdPinjaman();
        @FormUrlEncoded
        @POST("api/nasabah/detail_nasabah")
        Call<NasabahPojo> getDetailNasabah(@Field("id_nasabah") String id_nasabah);
    }
    private APITambahPinjaman ServicePojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pinjaman);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SpIdNasabah = findViewById(R.id.SpIdNasabah);
        BtnTambah = findViewById(R.id.BtnTambah);
        TxvNamaNasabah = findViewById(R.id.TxvNamaNasabah);
        TxvEmail = findViewById(R.id.TxvEmail);
        TxvNoHp = findViewById(R.id.TxvNoHp);
        TxvTglBergabung = findViewById(R.id.TxvTglBergabung);
        TxvAlamat = findViewById(R.id.TxvAlamat);
        IvNasabah = findViewById(R.id.IvNasabah);
        BtnDetail = findViewById(R.id.BtnDetail);
        EtJumlahPinjaman = findViewById(R.id.EtJumlahPinjaman);
        EtNote = findViewById(R.id.EtNote);
        SpError = findViewById(R.id.SpError);
        EtLamaAngsuran = findViewById(R.id.EtLamaAngsuran);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        ServicePojo = RetrofitClientInstance.getRetrofitInstance(TambahPinjamanActivity.this).create(APITambahPinjaman.class);
        Call<IdNasabahResponsePojo> idSimpananResponsePojoCall = ServicePojo.getAllIdPinjaman();
        idSimpananResponsePojoCall.enqueue(new Callback<IdNasabahResponsePojo>() {
            @Override
            public void onResponse(Call<IdNasabahResponsePojo> call, Response<IdNasabahResponsePojo> response) {
                if (response.body()!=null){
                    if (response.body().getStatus()==200){
                        nasabahPojos = response.body().getData();
                        List<String> IdPinjamanUn = new ArrayList<String>();
                        for (int i = 0; i < nasabahPojos.size(); i++){
                            IdPinjamanUn.add(nasabahPojos.get(i).getUsername()+" - "+nasabahPojos.get(i).getNama_nasabah());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                R.layout.spinner_black, IdPinjamanUn);
                        adapter.setDropDownViewResource(R.layout.spinner_black);
                        SpIdNasabah.setAdapter(adapter);
                        BtnTambah.setEnabled(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<IdNasabahResponsePojo> call, Throwable t) {

            }
        });
        SpIdNasabah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String id_nasabah = nasabahPojos.get(position).getId_nasabah();
                Call<NasabahPojo> pinjamanPojoCall = ServicePojo.getDetailNasabah(id_nasabah);
                pinjamanPojoCall.enqueue(new Callback<NasabahPojo>() {
                    @Override
                    public void onResponse(Call<NasabahPojo> call, Response<NasabahPojo> response) {
                        if(response.body()!=null){
                            final NasabahPojo result = response.body();
                            //        TxvNamaNasabah,TxvEmail,TxvNoHp,TxvTglBergabung,TxvAlamat;
                            TxvNamaNasabah.setText("Nama : "+result.getNama_nasabah());
                            TxvEmail.setText("Email : "+result.getEmail());
                            TxvNoHp.setText("No. HP : "+result.getNo_hp());
                            TxvTglBergabung.setText("Tgl. Bergabung : "+result.getTgl_bergabung());
                            TxvAlamat.setText("Alamat : "+ result.getAlamat_rumah());
                            if (result.getFoto_nasabah() != null && result.getFoto_nasabah().length() > 0) {
                                Picasso.with(getApplicationContext()).load(result.getFoto_nasabah()).placeholder(R.color.greycustom2).into(IvNasabah);
                            } else {
                                Picasso.with(getApplicationContext()).load(R.color.greycustom2).into(IvNasabah);
                                Picasso.with(getApplicationContext()).load(R.drawable.ic_user).placeholder(R.color.greycustom2).into(IvNasabah);
                            }
                            BtnDetail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent Idetail = new Intent(TambahPinjamanActivity.this, DetailNasabahActivity.class);
                                    Idetail.putExtra("id_nasabah",result.getId_nasabah());
                                    startActivity(Idetail);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<NasabahPojo> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        BtnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahPinjaman(BtnTambah);
            }
        });
        Button BtnClose = findViewById(R.id.BtnClose);
        BtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                BtnTambah.setEnabled(true);
            }
        });
        Button tes = findViewById(R.id.BtnMingguan);
        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button) findViewById(btn_id[i]);
            setFocus(tes, btn[0]);
//            btn[i].setBackgroundColor(Color.rgb(207, 207, 207));
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.BtnHarian :
                            terpilih = "harian";
                            setFocus(btn_unfocus, btn[0]);
                            break;

                        case R.id.BtnMingguan :
                            terpilih = "mingguan";
                            setFocus(btn_unfocus, btn[1]);
                            break;

                        case R.id.BtnBulanan :
                            terpilih = "bulanan";
                            setFocus(btn_unfocus, btn[2]);
                            break;

                        case R.id.BtnTahunan :
                            terpilih = "tahunan";
                            setFocus(btn_unfocus, btn[3]);
                            break;
                    }
                }
            });
        }
        btn_unfocus = btn[0];
    }
    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.rgb(3, 106, 150));
        this.btn_unfocus = btn_focus;

    }
    private void tambahPinjaman(final Button button){
        DrawableButtonExtensionsKt.showProgress(button, new Function1<ProgressParams, Unit>() {
            @Override
            public Unit invoke(ProgressParams progressParams) {
                progressParams.setButtonTextRes(R.string.loading);
                progressParams.setProgressColor(Color.WHITE);
                return Unit.INSTANCE;
            }
        });

        button.setEnabled(false);
        final String SpIdNasabahs = (nasabahPojos.size()!=0) ? nasabahPojos.get(SpIdNasabah.getSelectedItemPosition()).getId_nasabah() : null;
        final String EtJumlahPinjamans = EtJumlahPinjaman.getText().toString();
        final String EtNotes = EtNote.getText().toString();
        final String EtLamaAngsurans = EtLamaAngsuran.getText().toString();
        if (TextUtils.isEmpty(SpIdNasabahs)) {
            SpError.setText("Nasabah tidak ditemukan");
            SpError.setTextColor(Color.RED);
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Kirim");
            return;
        }
        if (TextUtils.isEmpty(EtJumlahPinjamans)) {
            EtJumlahPinjaman.setError("Jumlah pinjaman masih kosong");
            EtJumlahPinjaman.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Kirim");
            return;
        }
        if (TextUtils.isEmpty(EtLamaAngsurans)) {
            EtLamaAngsuran.setError("Lama angsuran masih kosong");
            EtLamaAngsuran.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Kirim");
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<ResponsePojoValidasiTambahPinjaman> responsePojoCall = ServicePojo.tambahPinjaman(SpIdNasabahs,EtJumlahPinjamans,terpilih,EtLamaAngsurans,EtNotes);
                responsePojoCall.enqueue(new Callback<ResponsePojoValidasiTambahPinjaman>() {
                    @Override
                    public void onResponse(Call<ResponsePojoValidasiTambahPinjaman> call, Response<ResponsePojoValidasiTambahPinjaman> response) {
                        ValidasiTambahPinjamanPojo validasiTambahPinjamanPojo = response.body().getData();
                        TextView TxvAngsuran = findViewById(R.id.TxvAngsuran);
                        TxvAngsuran.setText(": "+validasiTambahPinjamanPojo.getAngsuran());
                        TextView TxvPeriode = findViewById(R.id.TxvPeriode);
                        TxvPeriode.setText(": "+validasiTambahPinjamanPojo.getPeriode());
                        TextView TxvLamaAngsuran = findViewById(R.id.TxvLamaAngsuran);
                        TxvLamaAngsuran.setText(": "+validasiTambahPinjamanPojo.getLama_angsuran());
                        TextView TxvBungaPinjaman = findViewById(R.id.TxvBungaPinjaman);
                        TxvBungaPinjaman.setText(": "+validasiTambahPinjamanPojo.getBunga_pinjaman());
                        TextView TxvJumlahDiterima = findViewById(R.id.TxvJumlahDiterima);
                        TxvJumlahDiterima.setText(": "+validasiTambahPinjamanPojo.getJumlah_diterima());
                        TextView TxvBiayaAdministrasi = findViewById(R.id.TxvBiayaAdministrasi);
                        TxvBiayaAdministrasi.setText(": "+validasiTambahPinjamanPojo.getBiaya_administrasi());
                        TextView TxvBiayaSimpanan = findViewById(R.id.TxvBiayaSimpanan);
                        TxvBiayaSimpanan.setText(": "+validasiTambahPinjamanPojo.getBiaya_simpanan());
                        TextView TxvTglPinjaman = findViewById(R.id.TxvTglPinjaman);
                        TxvTglPinjaman.setText(": "+validasiTambahPinjamanPojo.getTgl_pinjaman());
                        TextView TxvNote = findViewById(R.id.TxvNote);
                        TxvNote.setText(response.body().getMsg());
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, R.string.progressRight);
                        final Button BtnKonfirmasi = findViewById(R.id.BtnKonfirmasi);
                        if (response.body() != null){
                            if (response.body().getStatus()==200){
                                BtnKonfirmasi.setText("Konfirmasi");
                                TxvNote.setTextColor(Color.BLACK);
                                BtnKonfirmasi.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        simpanPinjaman(BtnKonfirmasi,SpIdNasabahs, EtJumlahPinjamans,EtLamaAngsurans,EtNotes);
                                    }
                                });
                            }else{
                                BtnKonfirmasi.setText("Tutup");
                                TxvNote.setTextColor(Color.RED);
                                BtnKonfirmasi.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsePojoValidasiTambahPinjaman> call, Throwable t) {
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, "Gagal, coba lagi");
                        Toast.makeText(TambahPinjamanActivity.this, "Terjadi gangguan jaringan", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        },500);
    }
    private void simpanPinjaman(final Button button, final String id_nasabah, final String jumlah_pinjaman, final String lama_angsuran, final String note){
        DrawableButtonExtensionsKt.showProgress(button, new Function1<ProgressParams, Unit>() {
            @Override
            public Unit invoke(ProgressParams progressParams) {
                progressParams.setButtonTextRes(R.string.loading);
                progressParams.setProgressColor(Color.WHITE);
                return Unit.INSTANCE;
            }
        });
        button.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<ResponsePojo> responsePojoCall = ServicePojo.tambahPinjamanSave(id_nasabah,jumlah_pinjaman,terpilih,lama_angsuran,note);
                responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                    @Override
                    public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, R.string.progressRight);
                        final Button BtnKonfirmasi = findViewById(R.id.BtnKonfirmasi);
                        if (response.body() != null){
                            if (response.body().getStatus()==200){
                                Toast.makeText(TambahPinjamanActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                button.setEnabled(true);
                                Toast.makeText(TambahPinjamanActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsePojo> call, Throwable t) {
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, "Gagal, coba lagi");
                        Toast.makeText(TambahPinjamanActivity.this, "Terjadi gangguan jaringan", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        },500);
    }

    class ResponsePojoValidasiTambahPinjaman{
        @SerializedName("status")
        Integer status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        ValidasiTambahPinjamanPojo data;

        public ResponsePojoValidasiTambahPinjaman(Integer status, String msg, ValidasiTambahPinjamanPojo data) {
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

        public ValidasiTambahPinjamanPojo getData() {
            return data;
        }

        public void setData(ValidasiTambahPinjamanPojo data) {
            this.data = data;
        }
    }
    class ValidasiTambahPinjamanPojo{
        @SerializedName("angsuran")
        String angsuran;
        @SerializedName("periode")
        String periode;
        @SerializedName("lama_angsuran")
        String lama_angsuran;
        @SerializedName("tgl_pinjaman")
        String tgl_pinjaman;
        @SerializedName("bunga_pinjaman")
        String bunga_pinjaman;
        @SerializedName("jumlah_diterima")
        String jumlah_diterima;
        @SerializedName("biaya_administrasi")
        String biaya_administrasi;
        @SerializedName("biaya_simpanan")
        String biaya_simpanan;

        public ValidasiTambahPinjamanPojo(String angsuran, String periode, String lama_angsuran, String tgl_pinjaman, String bunga_pinjaman, String jumlah_diterima, String biaya_administrasi, String biaya_simpanan) {
            this.angsuran = angsuran;
            this.periode = periode;
            this.lama_angsuran = lama_angsuran;
            this.tgl_pinjaman = tgl_pinjaman;
            this.bunga_pinjaman = bunga_pinjaman;
            this.jumlah_diterima = jumlah_diterima;
            this.biaya_administrasi = biaya_administrasi;
            this.biaya_simpanan = biaya_simpanan;
        }

        public ValidasiTambahPinjamanPojo() {
        }

        public String getAngsuran() {
            return angsuran;
        }

        public void setAngsuran(String angsuran) {
            this.angsuran = angsuran;
        }

        public String getPeriode() {
            return periode;
        }

        public void setPeriode(String periode) {
            this.periode = periode;
        }

        public String getLama_angsuran() {
            return lama_angsuran;
        }

        public void setLama_angsuran(String lama_angsuran) {
            this.lama_angsuran = lama_angsuran;
        }

        public String getTgl_pinjaman() {
            return tgl_pinjaman;
        }

        public void setTgl_pinjaman(String tgl_pinjaman) {
            this.tgl_pinjaman = tgl_pinjaman;
        }

        public String getBunga_pinjaman() {
            return bunga_pinjaman;
        }

        public void setBunga_pinjaman(String bunga_pinjaman) {
            this.bunga_pinjaman = bunga_pinjaman;
        }

        public String getJumlah_diterima() {
            return jumlah_diterima;
        }

        public void setJumlah_diterima(String jumlah_diterima) {
            this.jumlah_diterima = jumlah_diterima;
        }

        public String getBiaya_administrasi() {
            return biaya_administrasi;
        }

        public void setBiaya_administrasi(String biaya_administrasi) {
            this.biaya_administrasi = biaya_administrasi;
        }

        public String getBiaya_simpanan() {
            return biaya_simpanan;
        }

        public void setBiaya_simpanan(String biaya_simpanan) {
            this.biaya_simpanan = biaya_simpanan;
        }
    }

}
