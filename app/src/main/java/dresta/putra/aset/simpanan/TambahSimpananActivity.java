package dresta.putra.aset.simpanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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

public class TambahSimpananActivity extends AppCompatActivity {
    private List<NasabahPojo> nasabahPojos;
    private Spinner SpIdNasabah;
    private Button BtnTambah,BtnDetail;
    private TextView TxvNamaNasabah,TxvEmail,TxvNoHp,TxvTglBergabung,TxvAlamat,SpError;
    private EditText EtJumlahSimpanan,EtNote;
    private ImageView IvNasabah;
    interface APITambahSimpanan{
        @FormUrlEncoded
        @POST("api/simpanan/tambah_simpanan")
        Call<ResponsePojo> tambahSimpanan(@Field("id_nasabah") String id_nasabah, @Field("jumlah_simpanan") String jumlah_simpanan, @Field("note") String note);
        @GET("api/simpanan/get_all_id_nasabah_for_simpanan")
        Call<IdNasabahResponsePojo> getAllIdSimpanan();
        @FormUrlEncoded
        @POST("api/nasabah/detail_nasabah")
        Call<NasabahPojo> getDetailNasabah(@Field("id_nasabah") String id_nasabah);
    }
    private APITambahSimpanan ServicePojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_simpanan);
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
        EtJumlahSimpanan = findViewById(R.id.EtJumlahSimpanan);
        EtNote = findViewById(R.id.EtNote);
        SpError = findViewById(R.id.SpError);
        ServicePojo = RetrofitClientInstance.getRetrofitInstance(TambahSimpananActivity.this).create(APITambahSimpanan.class);
        Call<IdNasabahResponsePojo> idSimpananResponsePojoCall = ServicePojo.getAllIdSimpanan();
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
                Log.d("tesid_nasabah", id_nasabah);
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
                                    Intent Idetail = new Intent(TambahSimpananActivity.this, DetailNasabahActivity.class);
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
                tambahSimpanan(BtnTambah);
            }
        });

    }
    private void tambahSimpanan(final Button button){
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
        final String EtJumlahSimpanans = EtJumlahSimpanan.getText().toString();
        final String EtNotes = EtNote.getText().toString();
        if (TextUtils.isEmpty(SpIdNasabahs)) {
            SpError.setText("Nasabah tidak ditemukan");
            SpError.setTextColor(Color.RED);
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Kirim");
            return;
        }
        if (TextUtils.isEmpty(EtJumlahSimpanans)) {
            EtJumlahSimpanan.setError("Jumlah simpanan awal masih kosong");
            EtJumlahSimpanan.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Kirim");
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<ResponsePojo> responsePojoCall = ServicePojo.tambahSimpanan(SpIdNasabahs,EtJumlahSimpanans,EtNotes);
                responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                    @Override
                    public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                        if (response.body() != null){
                            if (response.body().getStatus()==200){
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, R.string.progressRight);
                                finish();
                                Toast.makeText(TambahSimpananActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }else{
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, R.string.progressRight);
                                Toast.makeText(TambahSimpananActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsePojo> call, Throwable t) {
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, "Gagal, coba lagi");
                        Toast.makeText(TambahSimpananActivity.this, "Terjadi gangguan jaringan", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        },1000);
    }

}
