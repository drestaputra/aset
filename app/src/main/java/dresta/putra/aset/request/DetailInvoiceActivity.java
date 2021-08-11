package dresta.putra.aset.request;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.github.razir.progressbutton.ProgressParams;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.ResponsePojo;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.paket.PaketPojo;
import dresta.putra.aset.rekening.RekeningPojo;
import dresta.putra.aset.rekening.RekeningPojoResponse;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class DetailInvoiceActivity extends AppCompatActivity {
    private String id_request,id_rekening,id_paket, device_id="999";
    private ProgressBar main_progress;
    private RequestPojo requestPojo;
    private RekeningPojo rekeningPojo;
    private PaketPojo paketPojo;
    private PrefManager prefManager;
    private Button BtnUbahRekening, BtnKonfirmasi, BtnUbahPembayaran;
    private TextView TxvNoInvoice,TxvTotalTagihanInvoice, TxvTglRequest,TxvNamaPaket, TxvHargaPaket, TxvDeskripsiPaket
            ,TxvNomorRekening, TxvNamaBank, TxvNamaPemilikRekening, TxvUsername, TxvEmail, TxvPassword, TxvNoHp;
    private Locale localeID;
    private  NumberFormat formatRupiah;
    private ImageView IvBank;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    private List<RekeningPojo> rekeningPojoList;
    private Spinner SpRekening;
    interface APIDetailInvoice{
        @FormUrlEncoded
        @POST("api/request/detail_request")
        Call<RequestPojo> getDetailInvoice(@Field("id_request") String id_request, @Field("device_id") String device_id);
        @FormUrlEncoded()
        @POST("api/rekening/detail_rekening")
        Call<RekeningPojo> getDetailRekening(@Field("id_rekening") String id_rekening);
        @FormUrlEncoded
        @POST("api/paket/detail_paket")
        Call<PaketPojo> getDetailPaket(@Field("id_paket") String id_paket);
        @FormUrlEncoded
        @POST("api/request/ubah_rekening")
        Call<ResponsePojo> ubahRekening(@Field("id_request") String id_request, @Field("id_rekening") String id_rekening, @Field("device_id") String device_id);
        @FormUrlEncoded
        @POST("api/rekening/data_rekening")
        Call<RekeningPojoResponse> getRekening (
                @Field("pencarian") String pencarian,
                @Field("page") int page,
                @Field("perPage") int perPage
        );
    }
    private APIDetailInvoice apiDetailInvoice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_invoice);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id_request = getIntent().getStringExtra("id_request");
        BtnKonfirmasi = findViewById(R.id.BtnKonfirmasi);
        BtnKonfirmasi.setEnabled(false);
        BtnUbahRekening = findViewById(R.id.BtnUbahRekening);
        BtnUbahPembayaran = findViewById(R.id.BtnUbahPembayaran);
        BtnUbahRekening.setEnabled(false);
        TxvNoInvoice = findViewById(R.id.TxvNoInvoice);
        TxvTotalTagihanInvoice = findViewById(R.id.TxvTotalTagihanInvoice);
        TxvTglRequest = findViewById(R.id.TxvTglRequest);
        TxvNamaPaket = findViewById(R.id.TxvNamaPaket);
        TxvHargaPaket = findViewById(R.id.TxvHargaPaket);
        TxvDeskripsiPaket = findViewById(R.id.TxvDeskripsiPaket);
        TxvNomorRekening = findViewById(R.id.TxvNomorRekening);
        TxvNamaBank = findViewById(R.id.TxvNamaBank);
        IvBank = findViewById(R.id.IvBank);
        TxvNamaPemilikRekening = findViewById(R.id.TxvNamaPemilikRekening);
        TxvUsername = findViewById(R.id.TxvUsername);
        TxvEmail = findViewById(R.id.TxvEmail);
        TxvPassword = findViewById(R.id.TxvPassword);
        TxvNoHp = findViewById(R.id.TxvNoHp);
        SpRekening = findViewById(R.id.SpRekening);
        prefManager = new PrefManager(this);
        device_id = prefManager.getDeviceId();
        apiDetailInvoice = RetrofitClientInstance.getRetrofitInstance(DetailInvoiceActivity.this).create(APIDetailInvoice.class);
        main_progress = findViewById(R.id.main_progress);
        localeID = new Locale("in", "ID");
        formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        initDetailInvoice();
        BtnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailInvoiceActivity.this,KonfirmasiActivity.class);
                intent.putExtra("id_request",id_request);
                startActivity(intent);
            }
        });
        BtnUbahRekening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                initDataSemuaRekening();
            }
        });
        Button BtnClose = findViewById(R.id.BtnClose);
        BtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        BtnUbahPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailInvoiceActivity.this)
                        .setTitle("Konfirmasi")
                        .setMessage("Yakin mengubah rekening pembayaran?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ubahRekening(BtnUbahPembayaran);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDetailInvoice();
    }
    private void ubahRekening(final Button button){
        DrawableButtonExtensionsKt.showProgress(button, new Function1<ProgressParams, Unit>() {
            @Override
            public Unit invoke(ProgressParams progressParams) {
                progressParams.setButtonTextRes(R.string.loading);
                progressParams.setProgressColor(Color.BLACK);
                return Unit.INSTANCE;
            }
        });
        button.setEnabled(false);
        showLoading();
        final String SpIdRekenings = (rekeningPojoList.size()!=0) ? rekeningPojoList.get(SpRekening.getSelectedItemPosition()).getId_rekening() : null;
        Call<ResponsePojo> responsePojoCall = apiDetailInvoice.ubahRekening(id_request, SpIdRekenings, device_id);
        responsePojoCall.enqueue(new Callback<ResponsePojo>() {
            @Override
            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {

                dismissLoading();
                button.setEnabled(true);
                if (response.body()!=null){
                    if (response.body().getStatus() == 200){
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        initDetailInvoice();
                        DrawableButtonExtensionsKt.hideProgress(button, "Ubah Pembayaran");
                        Toast.makeText(DetailInvoiceActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(DetailInvoiceActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, "Ubah Pembayaran");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePojo> call, Throwable t) {
                Toast.makeText(DetailInvoiceActivity.this, "Gangguan Jaringan", Toast.LENGTH_SHORT).show();
                dismissLoading();
                button.setEnabled(true);
                DrawableButtonExtensionsKt.hideProgress(button, "Ubah Pembayaran");
            }
        });

    }
    private void initDetailInvoice(){
        showLoading();
        final Call<RequestPojo> requestPojoCall = apiDetailInvoice.getDetailInvoice(id_request,device_id);
        requestPojoCall.enqueue(new Callback<RequestPojo>() {
            @Override
            public void onResponse(Call<RequestPojo> call, Response<RequestPojo> response) {
                if (response.body()!=null){
                    if (!response.body().getId_request().equals("")){
                        requestPojo = response.body();
                        id_rekening = requestPojo.getId_rekening();
                        initDataRekening();
                        id_paket = requestPojo.getId_paket();
                        initDataPaket();
                        TxvNoInvoice.setText(requestPojo.getNo_invoice());
                        TxvTotalTagihanInvoice.setText(formatRupiah.format(Float.parseFloat(requestPojo.getTotal_tagihan_invoice())));
                        TxvTglRequest.setText(requestPojo.getTgl_request());
                        TxvUsername.setText(requestPojo.getUsername());
                        TxvEmail.setText(requestPojo.getEmail());
                        TxvPassword.setText(requestPojo.getPassword());
                        TxvNoHp.setText(requestPojo.getNo_hp());
                        if (requestPojo.getStatus().equals("proses") || requestPojo.getStatus().equals("tolak")) {
                            BtnKonfirmasi.setVisibility(View.VISIBLE);
                            BtnUbahRekening.setVisibility(View.VISIBLE);
                        }
                        BtnKonfirmasi.setEnabled(true);
                        BtnUbahRekening.setEnabled(true);
                    }else{
                        finishMsg("Invoice tidak ditemukan");
                    }
                }else{
                    finishMsg("Invoice tidak ditemukan");
                }
                dismissLoading();
            }

            @Override
            public void onFailure(Call<RequestPojo> call, Throwable t) {
                dismissLoading();
                finishMsg("Gangguan Jaringan");
            }
        });
    }
    private void finishMsg(String msg){
        finish();
        Toast.makeText(DetailInvoiceActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
    private void initDataSemuaRekening(){
        showLoading();
        Call<RekeningPojoResponse> rekeningPojoResponseCall = apiDetailInvoice.getRekening("",0,9999);
        rekeningPojoResponseCall.enqueue(new Callback<RekeningPojoResponse>() {
            @Override
            public void onResponse(Call<RekeningPojoResponse> call, Response<RekeningPojoResponse> response) {
                dismissLoading();
                if (response.body()!=null){
                    if (response.body().getStatus()==200){
                        rekeningPojoList = response.body().getData();
                        List<String> listSpinner = new ArrayList<String>();
                        int selected = 0;
                        for (int i = 0; i < rekeningPojoList.size(); i++){
                            listSpinner.add(rekeningPojoList.get(i).getNama_bank().toUpperCase()+" - "+rekeningPojoList.get(i).getNo_rekening());
                            if (requestPojo!=null && requestPojo.getId_rekening().equals(rekeningPojoList.get(i).getId_rekening())){
                                selected = i;
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetailInvoiceActivity.this,
                                android.R.layout.simple_spinner_item, listSpinner);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SpRekening.setAdapter(adapter);
                        SpRekening.setSelection(selected);
                    }
                }
            }

            @Override
            public void onFailure(Call<RekeningPojoResponse> call, Throwable t) {
                dismissLoading();
            }
        });
    }
    private void initDataRekening(){
        showLoading();
        Call<RekeningPojo> rekeningPojoCall = apiDetailInvoice.getDetailRekening(id_rekening);
        rekeningPojoCall.enqueue(new Callback<RekeningPojo>() {
            @Override
            public void onResponse(Call<RekeningPojo> call, Response<RekeningPojo> response) {
                if (response.body()!=null){
                    if (!response.body().getId_rekening().equals("")){
                        rekeningPojo = response.body();
                        TxvNomorRekening.setText(rekeningPojo.getNo_rekening());
                        TxvNamaBank.setText(rekeningPojo.getNama_bank());
                        TxvNamaPemilikRekening.setText(rekeningPojo.getNama_pemilik_rekening());
//                        IvBank
                        if (rekeningPojo.getGambar_bank() != null && rekeningPojo.getGambar_bank().length() > 0) {
                            Picasso.with(getApplicationContext()).load(rekeningPojo.getGambar_bank()).placeholder(R.color.greycustom2).into(IvBank);
                        } else {
                            Picasso.with(getApplicationContext()).load(R.color.greycustom2).into(IvBank);
                        }

                    }else{
                        finishMsg("Rekening tidak ditemukan");
                    }
                }else{
                    finishMsg("Rekening tidak ditemukan");
                }
                dismissLoading();
            }

            @Override
            public void onFailure(Call<RekeningPojo> call, Throwable t) {
                finishMsg("Rekening tidak ditemukan");
            }
        });
    }
    private void  initDataPaket(){
        showLoading();
        final Call<PaketPojo> paketPojoCall = apiDetailInvoice.getDetailPaket(id_paket);
        paketPojoCall.enqueue(new Callback<PaketPojo>() {
            @Override
            public void onResponse(Call<PaketPojo> call, Response<PaketPojo> response) {
                if (response.body()!=null){
                    if (!response.body().getId_paket().equals("")){
                        paketPojo = response.body();
                        TxvNamaPaket.setText(paketPojo.getNama_paket());
                        TxvDeskripsiPaket.setText(HtmlCompat.fromHtml(paketPojo.getDeskripsi_paket(),HtmlCompat.FROM_HTML_MODE_LEGACY));
                        TxvHargaPaket.setText(formatRupiah.format(Float.parseFloat(paketPojo.getHarga_paket())));

                    }else{
                        finishMsg("Paket tidak ditemukan");
                    }
                }else{
                    finishMsg("Paket tidak ditemukan");
                }
                dismissLoading();
            }

            @Override
            public void onFailure(Call<PaketPojo> call, Throwable t) {
                dismissLoading();
                finishMsg("Paket tidak ditemukan");
            }
        });
    }
    void showLoading(){
        main_progress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    void dismissLoading(){
        main_progress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}