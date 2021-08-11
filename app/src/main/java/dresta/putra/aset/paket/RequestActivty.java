package dresta.putra.aset.paket;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.ResponsePojo;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.rekening.RekeningPojo;
import dresta.putra.aset.rekening.RekeningPojoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class RequestActivty extends AppCompatActivity {
    private EditText EtUsername,EtNoHp,EtPassword,EtConfPassword,EtEmail;
    private String id_paket,id_rekening, device_id;
    private Spinner SpRekening;
    private List<RekeningPojo> rekeningPojos;
    private PaketPojo paketPojo;
    private LinearLayout LlRequest;
    private TextView TxvNamaPaket, TxvDurasiPaket, TxvDeskripsiPaket, TxvHargaPaket;
    private ProgressDialog pg;
    private PrefManager prefManager;
    interface APIRequest{
        @FormUrlEncoded
        @POST("api/paket/request")
        Call<ResponsePojo> requestOwner(
                @Field("username") String username,
                @Field("email") String email,
                @Field("no_hp") String no_hp,
                @Field("password") String password,
                @Field("id_paket") String id_paket,
                @Field("id_rekening") String id_rekening,
                @Field("device_id") String device_id);
        @FormUrlEncoded
        @POST("api/paket/detail_paket")
        Call<PaketPojo> getDetailPaket(@Field("id_paket") String id_paket);
        @FormUrlEncoded
        @POST("api/rekening/data_rekening")
        Call<RekeningPojoResponse> getRekening (
                @Field("pencarian") String pencarian,
                @Field("page") int page,
                @Field("perPage") int perPage
        );
    }
    private APIRequest servicePojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        id_paket = getIntent().getStringExtra("id_paket");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        servicePojo =  RetrofitClientInstance.getRetrofitInstance(RequestActivty.this).create(APIRequest.class);
//        BtnRequest = findViewById(R.id.BtnRequest);
        prefManager = new PrefManager(this);
        EtPassword = findViewById(R.id.EtPassword);
        EtConfPassword = findViewById(R.id.EtConfPassword);
        EtUsername = findViewById(R.id.EtUsername);
        EtEmail = findViewById(R.id.EtEmail);
        EtNoHp = findViewById(R.id.EtNoHp);
        SpRekening = findViewById(R.id.SpRekening);
        LlRequest =  findViewById(R.id.LlRequest);
        TxvNamaPaket =  findViewById(R.id.TxvNamaPaket);
        TxvDurasiPaket =  findViewById(R.id.TxvDurasiPaket);
        TxvDeskripsiPaket =  findViewById(R.id.TxvDeskripsiPaket);
        TxvHargaPaket =  findViewById(R.id.TxvHargaPaket);


        device_id = prefManager.getDeviceId();
        initDetailPaket();
        initRekening();
        LlRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request(LlRequest);
            }
        });
    }
    private void initDetailPaket(){
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(RequestActivty.this);
        progressDoalog.setMax(100);
        progressDoalog.setCancelable(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        progressDoalog.show();
        Call<PaketPojo> paketPojoCall = servicePojo.getDetailPaket(id_paket);
        paketPojoCall.enqueue(new Callback<PaketPojo>() {
            @Override
            public void onResponse(Call<PaketPojo> call, Response<PaketPojo> response) {
                if (response.body()!=null){
                    if (response.body().getId_paket()!=null){
                        paketPojo = response.body();
                        TxvNamaPaket.setText(paketPojo.getNama_paket());
                        TxvDurasiPaket.setText("  "+paketPojo.getDurasi_paket());
                        TxvDeskripsiPaket.setText(HtmlCompat.fromHtml(paketPojo.getDeskripsi_paket(),HtmlCompat.FROM_HTML_MODE_LEGACY));
                        Locale localeID = new Locale("in", "ID");
                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                        TxvHargaPaket.setText(formatRupiah.format(Float.parseFloat(paketPojo.getHarga_paket())));
                    }
                }
                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call<PaketPojo> call, Throwable t) {
                progressDoalog.dismiss();
            }
        });
    }
    private void initRekening(){
        Call<RekeningPojoResponse> rekeningPojoResponseCall = servicePojo.getRekening("",0,9999);
        rekeningPojoResponseCall.enqueue(new Callback<RekeningPojoResponse>() {
            @Override
            public void onResponse(Call<RekeningPojoResponse> call, Response<RekeningPojoResponse> response) {
                if (response.body() != null){
                    if (response.body().getStatus()==200){
                        rekeningPojos = response.body().getData();
                        List<String> listSpinner = new ArrayList<String>();
                        int selected = 0;
                        for (int i = 0; i < rekeningPojos.size(); i++){
                            listSpinner.add(rekeningPojos.get(i).getNama_bank().toUpperCase()+" - "+rekeningPojos.get(i).getNo_rekening());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RequestActivty.this,
                                android.R.layout.simple_spinner_item, listSpinner);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SpRekening.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<RekeningPojoResponse> call, Throwable t) {

            }
        });
    }
    private void request(final LinearLayout button){
        /*DrawableButtonExtensionsKt.showProgress(button, new Function1<ProgressParams, Unit>() {
            @Override
            public Unit invoke(ProgressParams progressParams) {
                progressParams.setButtonTextRes(R.string.loading);
                progressParams.setProgressColor(Color.BLACK);
                return Unit.INSTANCE;
            }
        });*/
        button.setEnabled(false);
        final String EtPasswords = EtPassword.getText().toString();
        final String EtConfPasswords = EtConfPassword.getText().toString();
        final String EtUsernames = EtUsername.getText().toString();
        final String EtEmails = EtEmail.getText().toString();
        final String EtNoHps = EtNoHp.getText().toString();
        id_rekening = rekeningPojos.get(SpRekening.getSelectedItemPosition()).getId_rekening();
        if (TextUtils.isEmpty(EtUsernames)) {
            EtUsername.setError("Username masih kosong");
            EtUsername.requestFocus();
            button.setEnabled(true);
//            DrawableButtonExtensionsKt.hideProgress(button, "Request");
            return;
        }
        if (TextUtils.isEmpty(EtEmails)) {
            EtEmail.setError("Email masih kosong");
            EtEmail.requestFocus();
            button.setEnabled(true);
//            DrawableButtonExtensionsKt.hideProgress(button, "Request");
            return;
        }
        if (TextUtils.isEmpty(EtNoHps)) {
            EtNoHp.setError("Nomor HP masih kosong");
            EtNoHp.requestFocus();
            button.setEnabled(true);
//            DrawableButtonExtensionsKt.hideProgress(button, "Request");
            return;
        }
        if (TextUtils.isEmpty(EtPasswords)) {
            EtPassword.setError("Password masih kosong");
            EtPassword.requestFocus();
            button.setEnabled(true);
//            DrawableButtonExtensionsKt.hideProgress(button, "Request");
            return;
        }
        if (TextUtils.isEmpty(EtConfPasswords)) {
            EtConfPassword.setError("Konfirmasi password masih kosong");
            EtConfPassword.requestFocus();
            button.setEnabled(true);
//            DrawableButtonExtensionsKt.hideProgress(button, "Request");
            return;
        }
        if (!EtPasswords.equals(EtConfPasswords)) {
            EtConfPassword.setError("Konfirmasi password tidak sama");
            EtConfPassword.requestFocus();
            button.setEnabled(true);
//            DrawableButtonExtensionsKt.hideProgress(button, "Request");
            return;
        }
        if (id_rekening.equals("")){
            Toast.makeText(this, "Anda belum memilih rekening pembayaran", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rekeningPojos==null){
            Toast.makeText(this, "Anda belum memilih rekening pembayaran", Toast.LENGTH_SHORT).show();
            return;
        }
        if (SpRekening.getSelectedItemPosition() == -1 ){
            Toast.makeText(this, "Anda belum memilih rekening pembayaran", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(RequestActivty.this)
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah Anda sudah yakin mengisi formulir dengan benar?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                pg = new ProgressDialog(RequestActivty.this);
                                pg.setMax(100);
                                pg.setTitle("Proses");
                                pg.setCancelable(false);
                                pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                pg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
                                pg.show();
                                Call<ResponsePojo> responsePojoCall = servicePojo.requestOwner(EtUsernames,EtEmails,EtNoHps,EtPasswords,id_paket,id_rekening, device_id);
                                responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                                    @Override
                                    public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                                        if (response.body()!=null){
                                            if (response.body().getStatus() == 200){
                                                pg.dismiss();
                                                button.setEnabled(true);
                                                Toast.makeText(RequestActivty.this, "Permintaan berhasil, Anda akan segera kami hubungi", Toast.LENGTH_LONG).show();
                                                finish();
                                            }else{
                                                pg.dismiss();
                                                button.setEnabled(true);
                                                Toast.makeText(RequestActivty.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponsePojo> call, Throwable t) {
                                        pg.dismiss();
                                        button.setEnabled(true);
                                        Toast.makeText(RequestActivty.this, "Gagal, coba lagi", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                button.setEnabled(true);
                            }
                        }).show();
            }
        },0);
    }
}
