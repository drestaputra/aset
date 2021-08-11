package dresta.putra.aset.nasabah;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.github.razir.progressbutton.ProgressParams;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.ResponsePojo;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.alamat.KabupatenPojo;
import dresta.putra.aset.alamat.KecamatanPojo;
import dresta.putra.aset.alamat.KelurahanPojo;
import dresta.putra.aset.alamat.ProvinsiPojo;
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
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class EditNasabahActivity extends AppCompatActivity  implements EasyPermissions.PermissionCallbacks{
    private static final int RC_EXTERNAL_STORAGE= 10;
    private EditText EtEmail,EtNamaNasabah,EtNamaIbuKandung,EtNoHp,EtTempatLahir,EtTglLahir,EtAlamatRumah,EtWargaNegara
            ,EtPekerjaan,EtNamaPerusahaan,EtAlamatTempatKerja,EtHobi,EtMakananKesukaan;
    private Spinner  SpAgama,SpJenisKelamin,SpProvinsi,SpKabupaten,SpKecamatan,SpKelurahan,SpGolonganDarah;
    private String id_nasabah = "",id_provinsi="",id_kabupaten="",id_kecamatan = "",id_kelurahan = "",tgl_lahir;
    private ArrayAdapter<String> adapterAgama,adapterGoldar,adapterJenisKelamin;
    private List<ProvinsiPojo> provinsiPojos = null;
    private List<KabupatenPojo> kabupatenPojos = null;
    private List<KecamatanPojo> kecamatanPojos = null;
    private List<KelurahanPojo> kelurahanPojos = null;
    private DatePickerDialog datePickerDialog;
    private PrefManager prefManager;
    private NasabahPojo nasabahPojo = null;
    private String[] arraySpinnerJenisKelamin,arraySpinnerAgama,arraySpinnerGoldar;
    private Call<ProvinsiPojoResponse> provinsiPojoCall;
    private Call<KabupatenPojoResponse> kabupatenPojoResponseCall;
    private Call<KecamatanPojoResponse> kecamatanPojoCall;
    private Call<KelurahanPojoResponse> kelurahanPojoResponseCall;
    private Button BtnSimpan, BtnImage;
    private ImageView IvFotoNasabah;
    private TextView EtUsername;
    public final static String BASE_URL = "http://192.168.168.11/api-kompikaleng/";
    public static final int REQUEST_IMAGE = 100;
    Uri uri;

    interface APIEditNasabah{
        @FormUrlEncoded
        @POST("api/nasabah/edit_nasabah")
        Call<ResponsePojo> editNasabah(
                @Field("id_nasabah") String id_nas,
                @Field("email") String email,
                @Field("nama_nasabah") String nama_nasabah,
                @Field("nama_ibu_kandung") String nama_ibu_kandung,
                @Field("no_hp") String no_hp,
                @Field("jenis_kelamin") String jenis_kelamin,
                @Field("tempat_lahir") String tempat_lahir,
                @Field("tanggal_lahir") String tanggal_lahir,
                @Field("alamat_rumah") String alamat_rumah,
                @Field("provinsi") String provinsi,
                @Field("kabupaten") String kabupaten,
                @Field("kecamatan") String kecamatan,
                @Field("kelurahan") String kelurahan,
                @Field("warga_negara") String warga_negara,
                @Field("pekerjaan") String pekerjaan,
                @Field("alamat_tempat_kerja") String alamat_tempat_kerja,
                @Field("nama_usaha") String nama_usaha,
                @Field("agama") String agama,
                @Field("golongan_darah") String golongan_darah,
                @Field("hobi") String hobi,
                @Field("makanan_kesukaan") String makanan_kesukaan
        );
        @FormUrlEncoded
        @POST("api/nasabah/detail_nasabah")
        Call<NasabahPojo> getDetailNasabah(@Field("id_nasabah") String id_n);
        @GET("api/alamat/provinsi")
        Call<ProvinsiPojoResponse> getProvinsi();
        @FormUrlEncoded
        @POST("api/alamat/kabupaten")
        Call<KabupatenPojoResponse> getKabupaten(@Field("id_provinsi") String id_provinsi);
        @FormUrlEncoded
        @POST("api/alamat/kecamatan")
        Call<KecamatanPojoResponse> getKecamatan(@Field("id_kabupaten") String id_kabupaten);
        @FormUrlEncoded
        @POST("api/alamat/kelurahan")
        Call<KelurahanPojoResponse> getKelurahan(@Field("id_kecamatan") String id_kecamatan);
        @Multipart

        @POST("api/nasabah/upload_foto")
        Call<ResponsePojo> uploadFoto(@Part MultipartBody.Part file2,
                                      @Part("id_nasabah") RequestBody id_nasabah);
    }
    private APIEditNasabah servicePojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nasabah);
        prefManager = new PrefManager(EditNasabahActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id_nasabah = getIntent().getStringExtra("id_nasabah");
        EtUsername = findViewById(R.id.EtUsername);
        EtEmail = findViewById(R.id.EtEmail);
        EtNamaNasabah = findViewById(R.id.EtNamaNasabah);
        EtNamaIbuKandung = findViewById(R.id.EtNamaIbuKandung);
        EtTglLahir = findViewById(R.id.EtTglLahir);
        EtNoHp = findViewById(R.id.EtNoHp);
        EtTempatLahir = findViewById(R.id.EtTempatLahir);
        SpProvinsi = findViewById(R.id.SpProvinsi);
        SpKabupaten = findViewById(R.id.SpKabupaten);
        SpKecamatan = findViewById(R.id.SpKecamatan);
        SpKelurahan = findViewById(R.id.SpKelurahan);
        EtAlamatRumah = findViewById(R.id.EtAlamatRumah);
        EtWargaNegara = findViewById(R.id.EtWargaNegara);
        EtPekerjaan = findViewById(R.id.EtPekerjaan);
        BtnImage = findViewById(R.id.BtnImage);
        IvFotoNasabah = findViewById(R.id.IvFotoNasabah);

        EtNamaPerusahaan = findViewById(R.id.EtNamaPerusahaan);
        EtAlamatTempatKerja = findViewById(R.id.EtAlamatTempatKerja);
        EtHobi = findViewById(R.id.EtHobi);
        EtMakananKesukaan = findViewById(R.id.EtMakananKesukaan);
        BtnSimpan = findViewById(R.id.BtnSimpan);
        servicePojo = RetrofitClientInstance.getRetrofitInstance(EditNasabahActivity.this).create(APIEditNasabah.class);
        EtTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateStartDialog();
            }
        });
        SpAgama = findViewById(R.id.SpAgama);
        arraySpinnerAgama = new String[] {
                "Buddha", "Hindu","Islam","Katolik","Khonghucu","Protestan"
        };
         adapterAgama = new ArrayAdapter<String>(EditNasabahActivity.this,
                R.layout.spinner_black, arraySpinnerAgama);
        adapterAgama.setDropDownViewResource(R.layout.spinner_black);
        SpAgama.setAdapter(adapterAgama);
        SpJenisKelamin = findViewById(R.id.SpJenisKelamin);
        arraySpinnerJenisKelamin = new String[] {
                "laki-laki", "perempuan"
        };
        adapterJenisKelamin = new ArrayAdapter<String>(EditNasabahActivity.this,
                R.layout.spinner_black, arraySpinnerJenisKelamin);
        adapterJenisKelamin.setDropDownViewResource(R.layout.spinner_black);
        SpJenisKelamin.setAdapter(adapterJenisKelamin);
        SpGolonganDarah = findViewById(R.id.SpGolonganDarah);
        arraySpinnerGoldar = new String[] {
                "A", "B","AB","O"
        };
        adapterGoldar = new ArrayAdapter<String>(EditNasabahActivity.this,
                R.layout.spinner_black, arraySpinnerGoldar);
        adapterGoldar.setDropDownViewResource(R.layout.spinner_black);
        SpGolonganDarah.setAdapter(adapterGoldar);
        initDataProfil();
        SpProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_provinsi = provinsiPojos.get(position).getId();
                initDataKabupaten();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SpKabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_kabupaten = kabupatenPojos.get(position).getId();
                initDataKecamatan();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SpKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_kecamatan = kecamatanPojos.get(position).getId();
                initDataKelurahan();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SpKelurahan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_kelurahan = kelurahanPojos.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        BtnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(BtnSimpan);
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

        String filePath = getRealPathFromURIPath(contentURI,EditNasabahActivity.this);
        File file = new File(filePath);
        Log.d("File",""+file.getName());

        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file",file.getName(),mFile);

        APIEditNasabah service  = RetrofitClientInstance.getRetrofitInstance(EditNasabahActivity.this).create(APIEditNasabah.class);

        MultipartBody.Part body2 = MultipartBody.Part.createFormData("foto_nasabah", file.getName(), mFile);
        RequestBody id_nasabahRB = RequestBody.create(okhttp3.MultipartBody.FORM, id_nasabah);
        Call<ResponsePojo> uploadGambar = service.uploadFoto(body2,id_nasabahRB);
        uploadGambar.enqueue(new Callback<ResponsePojo>() {
            @Override
            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                if (response.body()!=null){
                    if (response.body().getStatus()==200){
                        initDataProfil();
                    }else{
                        Toast.makeText(EditNasabahActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePojo> call, Throwable t) {
                Toast.makeText(EditNasabahActivity.this, "Gagal, gangguan jaringan", Toast.LENGTH_SHORT).show();
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
        final String EtEmails = EtEmail.getText().toString();
        final String EtNamaNasabahs = EtNamaNasabah.getText().toString();
        final String EtNamaIbuKandungs = EtNamaIbuKandung.getText().toString();
        final String EtNoHps = EtNoHp.getText().toString();
        final String EtTempatLahirs = EtTempatLahir.getText().toString();
        final String EtTglLahirs = EtTglLahir.getText().toString();
        final String EtAlamatRumahs = EtAlamatRumah.getText().toString();
        final String EtWargaNegaras = EtWargaNegara.getText().toString();
        final String EtPekerjaans = EtPekerjaan.getText().toString();
        final String EtNamaPerusahaans = EtNamaPerusahaan.getText().toString();
        final String EtAlamatTempatKerjas = EtAlamatTempatKerja.getText().toString();
        final String EtHobis = EtHobi.getText().toString();
        final String EtMakananKesukaans = EtMakananKesukaan.getText().toString();
        final String SpJenisKelamins = SpJenisKelamin.getSelectedItem().toString();
        final String SpProvinsis = (provinsiPojos.size()!=0) ? provinsiPojos.get(SpProvinsi.getSelectedItemPosition()).getId() : null;
        final String SpKabupatens = (kabupatenPojos.size()!=0) ? kabupatenPojos.get(SpKabupaten.getSelectedItemPosition()).getId() : null;
        final String SpKecamatans = (kecamatanPojos.size()!=0) ? kecamatanPojos.get(SpKecamatan.getSelectedItemPosition()).getId() : null;
        final String SpKelurahans = (kelurahanPojos.size()!=0) ? kelurahanPojos.get(SpKelurahan.getSelectedItemPosition()).getId() : null;
        final String SpAgamas = SpAgama.getSelectedItem().toString();
        final String SpGoldars = SpGolonganDarah.getSelectedItem().toString();
        if (TextUtils.isEmpty(EtEmails)) {
            EtEmail.setError("Email masih kosong");
            EtEmail.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }
        if (!TextUtils.isEmpty(EtEmails) && !Patterns.EMAIL_ADDRESS.matcher(EtEmails).matches()) {
            EtEmail.setError("Format email tidak sesuai");
            EtEmail.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }
        if (TextUtils.isEmpty(EtNamaNasabahs)) {
            EtNamaNasabah.setError("Nama lengkap masih kosong");
            EtNamaNasabah.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }
        if (TextUtils.isEmpty(EtNamaIbuKandungs)) {
            EtNamaIbuKandung.setError("Nama ibu kandung masih kosong");
            EtNamaIbuKandung.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }
        if (TextUtils.isEmpty(EtNoHps)) {
            EtNoHp.setError("Nomor HP masih kosong");
            EtNoHp.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }
        if (TextUtils.isEmpty(EtTempatLahirs)) {
            EtTempatLahir.setError("Tempat lahir masih kosong");
            EtTempatLahir.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }
        if (TextUtils.isEmpty(EtTglLahirs)) {
            EtTglLahir.setError("Tanggal lahir masih kosong");
            EtTglLahir.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }
        if (TextUtils.isEmpty(EtAlamatRumahs)) {
            EtAlamatRumah.setError("Alamat rumah masih kosong");
            EtAlamatRumah.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }
        if (TextUtils.isEmpty(EtWargaNegaras)) {
            EtWargaNegara.setError("Kewarga negaraan masih kosong");
            EtWargaNegara.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }if (TextUtils.isEmpty(EtPekerjaans)) {
            EtPekerjaan.setError("Pekerjaan masih kosong");
            EtPekerjaan.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }if (TextUtils.isEmpty(EtNamaPerusahaans)) {
            EtNamaPerusahaan.setError("Nama usaha / perusahaan masih kosong");
            EtNamaPerusahaan.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }if (TextUtils.isEmpty(EtAlamatTempatKerjas)) {
            EtAlamatTempatKerja.setError("Alamat usaha / perusahaan masih kosong");
            EtAlamatTempatKerja.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }
        button.setEnabled(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<ResponsePojo> responsePojoCall = servicePojo.editNasabah(
                        id_nasabah,EtEmails,EtNamaNasabahs,EtNamaIbuKandungs,EtNoHps,SpJenisKelamins,EtTempatLahirs,EtTglLahirs,EtAlamatRumahs
                        ,SpProvinsis,SpKabupatens,SpKecamatans,SpKelurahans,EtWargaNegaras, EtPekerjaans,EtAlamatTempatKerjas,EtNamaPerusahaans,SpAgamas,SpGoldars,EtHobis,EtMakananKesukaans);
                responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                    @Override
                    public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                        if (response.body()!= null){
                            if (response.body().getStatus()==200){
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, R.string.progressRight);
                                Toast.makeText(EditNasabahActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
                                Toast.makeText(EditNasabahActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsePojo> call, Throwable t) {
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, "Gagal, KLIK lagi");
                        Toast.makeText(EditNasabahActivity.this, "Terjadi gangguan jaringan", Toast.LENGTH_LONG).show();
                    }
                });

            }
        }, 3000);
    }
    private void initDataProfil(){
        Call<NasabahPojo> nasabahPojoCall = servicePojo.getDetailNasabah(id_nasabah);
        nasabahPojoCall.enqueue(new Callback<NasabahPojo>() {
            @Override
            public void onResponse(Call<NasabahPojo> call, Response<NasabahPojo> response) {
                if (response.body()!=null){
                    if (!response.body().getId_nasabah().equals("")){
                        nasabahPojo = response.body();
                        int positionAgama = adapterAgama.getPosition(nasabahPojo.getAgama());
                        SpAgama.setSelection(positionAgama);
                        int positionGoldar = adapterGoldar.getPosition(nasabahPojo.getGolongan_darah());
                        SpGolonganDarah.setSelection(positionGoldar);
                        int positionJk = adapterJenisKelamin.getPosition(nasabahPojo.getJenis_kelamin());
                        SpJenisKelamin.setSelection(positionJk);
                        EtUsername.setText(nasabahPojo.getUsername());
                        EtEmail.setText(nasabahPojo.getEmail());
                        EtNamaNasabah.setText(nasabahPojo.getNama_nasabah());
                        EtNoHp.setText(nasabahPojo.getNo_hp());
                        EtTempatLahir.setText(nasabahPojo.getTempat_lahir());
                        EtNamaIbuKandung.setText(nasabahPojo.getNama_ibu_kandung());
                        EtTempatLahir.setText(nasabahPojo.getTempat_lahir());
                        EtAlamatRumah.setText(nasabahPojo.getAlamat_rumah());
                        EtWargaNegara.setText(nasabahPojo.getWarga_negara());
                        EtPekerjaan.setText(nasabahPojo.getPekerjaan());
                        EtAlamatTempatKerja.setText(nasabahPojo.getAlamat_tempat_kerja());
                        EtNamaPerusahaan.setText(nasabahPojo.getNama_usaha());
                        EtHobi.setText(nasabahPojo.getHobi());
                        EtMakananKesukaan.setText(nasabahPojo.getMakanan_kesukaan());
                        id_provinsi = nasabahPojo.getId_provinsi();
                        id_kabupaten = nasabahPojo.getId_kabupaten();
                        id_kecamatan = nasabahPojo.getId_kecamatan();
                        id_kelurahan = nasabahPojo.getId_kelurahan();
                        tgl_lahir = nasabahPojo.getTanggal_lahir();
                        EtTglLahir.setText(tgl_lahir);
                        initDataProvinsi();
                        BtnSimpan.setEnabled(true);
                        if (nasabahPojo.getFoto_nasabah() != null && nasabahPojo.getFoto_nasabah().length() > 0) {
                            Picasso.with(EditNasabahActivity.this).load(nasabahPojo.getFoto_nasabah()).placeholder(R.color.greycustom2).into(IvFotoNasabah);
                        } else {
                            Picasso.with(EditNasabahActivity.this).load(R.color.greycustom2).into(IvFotoNasabah);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<NasabahPojo> call, Throwable t) {

            }
        });
    }
    private void initDataProvinsi(){
        provinsiPojoCall = servicePojo.getProvinsi();
        provinsiPojoCall.enqueue(new Callback<ProvinsiPojoResponse>() {
            @Override
            public void onResponse(Call<ProvinsiPojoResponse> call, Response<ProvinsiPojoResponse> response) {
                if (response.body() != null){
                    if (response.body().getStatus()==200){
                        provinsiPojos = response.body().getData();
                        List<String> listSpinner = new ArrayList<String>();
                        int selected = 0;
                        for (int i = 0; i < provinsiPojos.size(); i++){
                            listSpinner.add(provinsiPojos.get(i).getNama().toUpperCase());
                            if (nasabahPojo!=null && nasabahPojo.getId_provinsi().equals(provinsiPojos.get(i).getId())){
                                selected = i;
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditNasabahActivity.this,
                                android.R.layout.simple_spinner_item, listSpinner);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SpProvinsi.setAdapter(adapter);
                        SpProvinsi.setSelection(selected);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProvinsiPojoResponse> call, Throwable t) {
            }
        });
    }
    private void initDataKabupaten(){
        kabupatenPojoResponseCall = servicePojo.getKabupaten(id_provinsi);
        kabupatenPojoResponseCall.enqueue(new Callback<KabupatenPojoResponse>() {
            @Override
            public void onResponse(Call<KabupatenPojoResponse> call, Response<KabupatenPojoResponse> response) {
                if (response.body()!=null){
                    if (response.body().getStatus()==200){
                        kabupatenPojos = response.body().getData();
                        List<String> listSpinner = new ArrayList<String>();
                        int selected = 0;
                        for (int i = 0; i < kabupatenPojos.size(); i++){
                            listSpinner.add(kabupatenPojos.get(i).getNama().toUpperCase());
                            if (nasabahPojo!=null && nasabahPojo.getId_kabupaten().equals(kabupatenPojos.get(i).getId())){
                                selected = i;
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditNasabahActivity.this,
                                android.R.layout.simple_spinner_item, listSpinner);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SpKabupaten.setAdapter(adapter);
                        SpKabupaten.setSelection(selected);
                    }
                }
            }
            @Override
            public void onFailure(Call<KabupatenPojoResponse> call, Throwable t) {
            }
        });
    }
    private void initDataKecamatan(){
        kecamatanPojoCall = servicePojo.getKecamatan(id_kabupaten);
        kecamatanPojoCall.enqueue(new Callback<KecamatanPojoResponse>() {
            @Override
            public void onResponse(Call<KecamatanPojoResponse> call, Response<KecamatanPojoResponse> response) {
                if(response.body()!=null){
                    if (response.body().getStatus()==200){
                        kecamatanPojos = response.body().getData();
                        List<String> listSpinner = new ArrayList<String>();
                        int selected = 0;
                        for (int i = 0; i < kecamatanPojos.size(); i++){
                            listSpinner.add(kecamatanPojos.get(i).getNama().toUpperCase());
                            if (nasabahPojo!=null && nasabahPojo.getId_kecamatan().equals(kecamatanPojos.get(i).getId())){
                                selected = i;
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditNasabahActivity.this,
                                android.R.layout.simple_spinner_item, listSpinner);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SpKecamatan.setAdapter(adapter);
                        SpKecamatan.setSelection(selected);
                    }
                }
            }

            @Override
            public void onFailure(Call<KecamatanPojoResponse> call, Throwable t) {

            }
        });
    }
    private void initDataKelurahan(){
        kelurahanPojoResponseCall = servicePojo.getKelurahan(id_kecamatan);
        kelurahanPojoResponseCall.enqueue(new Callback<KelurahanPojoResponse>() {
            @Override
            public void onResponse(Call<KelurahanPojoResponse> call, Response<KelurahanPojoResponse> response) {
                if (response.body()!=null){
                    if (response.body().getStatus()==200){
                        kelurahanPojos = response.body().getData();
                        List<String> listSpinner = new ArrayList<String>();
                        int selected = 0;
                        for (int i = 0; i < kelurahanPojos.size(); i++){
                            listSpinner.add(kelurahanPojos.get(i).getNama().toUpperCase());
                            if (nasabahPojo!=null && nasabahPojo.getId_kelurahan().equals(kelurahanPojos.get(i).getId())){
                                selected = i;
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditNasabahActivity.this,
                                android.R.layout.simple_spinner_item, listSpinner);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SpKelurahan.setAdapter(adapter);
                        SpKelurahan.setSelection(selected);
                    }
                }
            }

            @Override
            public void onFailure(Call<KelurahanPojoResponse> call, Throwable t) {
            }
        });
    }
    private void showDateStartDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(Objects.requireNonNull(EditNasabahActivity.this), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Locale localeID = new Locale("en", "EN");
                SimpleDateFormat format = new SimpleDateFormat("d MMMM yyyy ", localeID);
                EtTglLahir.setText(format.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    class ProvinsiPojoResponse{
        @SerializedName("status")
        Integer status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        List<ProvinsiPojo> data;

        public ProvinsiPojoResponse(Integer status, String msg, List<ProvinsiPojo> data) {
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

        public List<ProvinsiPojo> getData() {
            return data;
        }

        public void setData(List<ProvinsiPojo> data) {
            this.data = data;
        }
    }
    class KabupatenPojoResponse{
        @SerializedName("status")
        Integer status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        List<KabupatenPojo> data;

        public KabupatenPojoResponse(Integer status, String msg, List<KabupatenPojo> data) {
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

        public List<KabupatenPojo> getData() {
            return data;
        }

        public void setData(List<KabupatenPojo> data) {
            this.data = data;
        }
    }
    class KecamatanPojoResponse{
        @SerializedName("status")
        Integer status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        List<KecamatanPojo> data;

        public KecamatanPojoResponse(Integer status, String msg, List<KecamatanPojo> data) {
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

        public List<KecamatanPojo> getData() {
            return data;
        }

        public void setData(List<KecamatanPojo> data) {
            this.data = data;
        }
    }
    class KelurahanPojoResponse{
        @SerializedName("status")
        Integer status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        List<KelurahanPojo> data;

        public KelurahanPojoResponse(Integer status, String msg, List<KelurahanPojo> data) {
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

        public List<KelurahanPojo> getData() {
            return data;
        }

        public void setData(List<KelurahanPojo> data) {
            this.data = data;
        }
    }
}
