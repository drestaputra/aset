package dresta.putra.aset.request;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.github.razir.progressbutton.ProgressParams;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.util.List;
import java.util.Objects;
import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.ResponsePojo;
import dresta.putra.aset.RetrofitClientInstance;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class KonfirmasiActivity extends AppCompatActivity  implements EasyPermissions.PermissionCallbacks{
    private static final int RC_EXTERNAL_STORAGE= 10;
    private String id_request = "",id_provinsi="",id_kabupaten="",id_kecamatan = "",id_kelurahan = "",tgl_lahir, device_id="99999", id_pembayaran = "";
    private EditText EtNamaPembayar, EtNoHpPembayar, EtEmailPembayar, EtJumlahPembayaran;
    private PrefManager prefManager;
    private Button BtnKonfirmasi, BtnImage;
    private ImageView IvBuktiPembayaran;

    public static final int REQUEST_IMAGE = 100;
    public Boolean IsImageUploaded = false;
    private ProgressBar main_progress;
    Uri uri;

    interface APIEditNasabah{
        @FormUrlEncoded
        @POST("api/request/konfirmasi_pembayaran")
        Call<ResponsePojo> konfirmasiPembayaran(
                @Field("id_pembayaran") String id_pembayaran,
                @Field("nama_pembayar") String nama_pembayar,
                @Field("no_hp_pembayar") String no_hp_pembayar,
                @Field("email_pembayar") String email_pembayar,
                @Field("jumlah_pembayaran") String jumlah_pembayaran,
                @Field("id_request") String id_request,
                @Field("device_id") String device_id
        );
        @Multipart
        @POST("api/request/upload_bukti_pembayaran")
        Call<ResponsePojo> uploadFoto(@Part MultipartBody.Part file2,
                                      @Part("id_request") RequestBody id_request,
                                      @Part("device_id") RequestBody device_id
        );
        @FormUrlEncoded
        @POST("api/request/get_image_bukti_pembayaran")
        Call<ResponsePojo> getImage(@Field("id_request") String id_request, @Field("device_id") String device_id, @Field("id_pembayaran") String id_pembayaran);
    }
    private APIEditNasabah servicePojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi);
        prefManager = new PrefManager(KonfirmasiActivity.this);
        device_id = prefManager.getDeviceId();
        id_request = getIntent().getStringExtra("id_request");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        BtnKonfirmasi = findViewById(R.id.BtnKonfirmasi);
        BtnImage = findViewById(R.id.BtnImage);
        main_progress = findViewById(R.id.main_progress);
        IvBuktiPembayaran = findViewById(R.id.IvBuktiPembayaran);
        EtNamaPembayar = findViewById(R.id.EtNamaPembayar);
        EtNoHpPembayar = findViewById(R.id.EtNoHpPembayar);
        EtEmailPembayar = findViewById(R.id.EtEmailPembayar);
        EtJumlahPembayaran = findViewById(R.id.EtJumlahPembayaran);
        dismissLoading();
        servicePojo = RetrofitClientInstance.getRetrofitInstance(KonfirmasiActivity.this).create(APIEditNasabah.class);
        BtnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                edit(BtnKonfirmasi);
            }
        });
        BtnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execute(REQUEST_IMAGE);
            }
        });
    }
    void execute(int requestCode){
        switch (requestCode){
            case REQUEST_IMAGE:
                if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                    openGalleryIntent.setType("image/*");
                    startActivityForResult(openGalleryIntent, REQUEST_IMAGE);
                    break;
                }else{
                    EasyPermissions.requestPermissions(this,"Izinkan Aplikasi Mengakses Storage?",REQUEST_IMAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
                }
        }
    }
    private void reloadImage(){
        showLoading();
        Call<ResponsePojo> responsePojoCall = servicePojo.getImage(id_request,device_id,id_pembayaran);
        responsePojoCall.enqueue(new Callback<ResponsePojo>() {
            @Override
            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                if (response.body()!=null){
                    if (response.body().getStatus()==200){
                        if (response.body().getMsg() != null && response.body().getMsg().length() > 0) {
                            Picasso.with(KonfirmasiActivity.this).load(response.body().getMsg()).placeholder(R.color.greycustom2).into(IvBuktiPembayaran);
                        } else {
                            Picasso.with(KonfirmasiActivity.this).load(R.color.greycustom2).into(IvBuktiPembayaran);
                        }
                    }
                }
                dismissLoading();
            }

            @Override
            public void onFailure(Call<ResponsePojo> call, Throwable t) {
                Toast.makeText(KonfirmasiActivity.this, "Gangguan jaringan", Toast.LENGTH_SHORT).show();
                dismissLoading();
            }
        });
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    void uploadFile(Uri contentURI){
        showLoading();
        String filePath = getRealPathFromURIPath(contentURI,KonfirmasiActivity.this);
        File file = new File(filePath);

        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file",file.getName(),mFile);

        APIEditNasabah service  = RetrofitClientInstance.getRetrofitInstance(KonfirmasiActivity.this).create(APIEditNasabah.class);

        MultipartBody.Part body2 = MultipartBody.Part.createFormData("bukti_pembayaran", file.getName(), mFile);
        RequestBody id_requestRB = RequestBody.create(okhttp3.MultipartBody.FORM, id_request);
        RequestBody device_idRB = RequestBody.create(okhttp3.MultipartBody.FORM, device_id);
        Call<ResponsePojo> uploadGambar = service.uploadFoto(body2,id_requestRB,device_idRB);
        uploadGambar.enqueue(new Callback<ResponsePojo>() {
            @Override
            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                if (response.body()!=null){
                    if (response.body().getStatus()==200){
                        IsImageUploaded = true;
                        id_pembayaran = response.body().getMsg();
                        reloadImage();
                    }else{
                        Toast.makeText(KonfirmasiActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                }
                dismissLoading();
            }

            @Override
            public void onFailure(Call<ResponsePojo> call, Throwable t) {
                Toast.makeText(KonfirmasiActivity.this, "Gagal, gangguan jaringan", Toast.LENGTH_LONG).show();
                dismissLoading();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE && resultCode == RESULT_OK){
            uri = data.getData();
            uploadFile(uri);
        }
    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
            } else {
//                finish();
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(requestCode == REQUEST_IMAGE){
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(requestCode == REQUEST_IMAGE){
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
    private void edit(final Button button){
        DrawableButtonExtensionsKt.showProgress(button, new Function1<ProgressParams, Unit>() {
            @Override
            public Unit invoke(ProgressParams progressParams) {
                progressParams.setButtonTextRes(R.string.loading);
                progressParams.setProgressColor(Color.WHITE);
                return Unit.INSTANCE;
            }
        });
        button.setEnabled(false);
        final String EtNamaPembayars = EtNamaPembayar.getText().toString();
        final String EtNoHpPembayars = EtNoHpPembayar.getText().toString();
        final String EtEmailPembayars = EtEmailPembayar.getText().toString();
        final String EtJumlahPembayarans = EtJumlahPembayaran.getText().toString();
        if (!IsImageUploaded) {
            dismissLoading();
            Toast.makeText(this, "Anda belum mengupload foto bukti pembayaran", Toast.LENGTH_LONG).show();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Konfirmasi");
            return;
        }
        if (id_pembayaran.equals("")){
            dismissLoading();
            Toast.makeText(this, "Anda belum mengupload foto bukti pembayaran", Toast.LENGTH_LONG).show();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Konfirmasi");
            return;
        }
        if (id_request.equals("")){
            dismissLoading();
            Toast.makeText(this, "Data request tidak ditemukan", Toast.LENGTH_LONG).show();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Konfirmasi");
            return;
        }
        if (TextUtils.isEmpty(EtNamaPembayars)) {
            dismissLoading();
            EtNamaPembayar.setError("Email masih kosong");
            EtNamaPembayar.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Konfirmasi");
            return;
        }
        if (TextUtils.isEmpty(EtNoHpPembayars)) {
            dismissLoading();
            EtNoHpPembayar.setError("Email masih kosong");
            EtNoHpPembayar.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Konfirmasi");
            return;
        }
        if (TextUtils.isEmpty(EtEmailPembayars)) {
            dismissLoading();
            EtEmailPembayar.setError("Email masih kosong");
            EtEmailPembayar.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Konfirmasi");
            return;
        }
        if (TextUtils.isEmpty(EtJumlahPembayarans)) {
            dismissLoading();
            EtJumlahPembayaran.setError("Email masih kosong");
            EtJumlahPembayaran.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Konfirmasi");
            return;
        }

        button.setEnabled(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<ResponsePojo> responsePojoCall = servicePojo.konfirmasiPembayaran(
                        id_pembayaran,EtNamaPembayars,EtNoHpPembayars,EtEmailPembayars,EtJumlahPembayarans,id_request,device_id);
                responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                    @Override
                    public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                        if (response.body()!= null){
                            if (response.body().getStatus()==200){
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, R.string.progressRight);
                                Toast.makeText(KonfirmasiActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, "Konfirmasi");
                                Toast.makeText(KonfirmasiActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }
                        dismissLoading();
                    }

                    @Override
                    public void onFailure(Call<ResponsePojo> call, Throwable t) {
                        dismissLoading();
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, "Gagal, KLIK lagi");
                        Toast.makeText(KonfirmasiActivity.this, "Terjadi gangguan jaringan", Toast.LENGTH_LONG).show();
                    }
                });

            }
        }, 1000);
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
