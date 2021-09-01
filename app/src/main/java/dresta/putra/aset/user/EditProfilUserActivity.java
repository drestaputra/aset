package dresta.putra.aset.user;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.github.razir.progressbutton.ProgressParams;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dresta.putra.aset.R;
import dresta.putra.aset.ResponsePojo;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.alamat.KabupatenPojo;
import dresta.putra.aset.alamat.KecamatanPojo;
import dresta.putra.aset.alamat.ProvinsiPojo;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class EditProfilUserActivity extends AppCompatActivity {
    private Spinner SpProvinsi,SpKabupaten,SpKecamatan;
    private String id_provinsi="",id_kabupaten="",id_kecamatan = "";
    private List<ProvinsiPojo> provinsiPojos = null;
    private List<KabupatenPojo> kabupatenPojos = null;
    private List<KecamatanPojo> kecamatanPojos = null;
    private UserPojo userPojo = null;
    private EditText EtNama,EtEmail,EtNoHp,EtInstansi,EtAlamatRumah,EtWargaNegara;
    private TextView TxvUsername;
    private Button BtnSimpan;
    private Integer ready = 0;
    private Bundle bundle;
    private String nama_aset,kode_barang, tahun_perolehan, alamat, nomor_sertifikat;

    interface APIEditUser{
        @FormUrlEncoded
        @POST("api/user/edit_profil")
        Call<ResponsePojo> editProfil(
                @Field("nama_lengkap") String nama_lengkap,
                @Field("email") String email,
                @Field("no_hp") String no_hp,
                @Field("instansi") String instansi,
                @Field("provinsi") String provinsi,
                @Field("kabupaten") String kabupaten,
                @Field("kecamatan") String kecamatan,
                @Field("alamat") String alamat
        );
        @GET("api/user/profil")
        Call<EditUserPojoResponse> getProfil();
        @GET("api/alamat/provinsi")
        Call<ProvinsiPojoResponse> getProvinsi();
        @FormUrlEncoded
        @POST("api/alamat/kabupaten")
        Call<KabupatenPojoResponse> getKabupaten(@Field("id_provinsi") String id_provinsi);
        @FormUrlEncoded
        @POST("api/alamat/kecamatan")
        Call<KecamatanPojoResponse> getKecamatan(@Field("id_kabupaten") String id_kabupaten);
    }
    private APIEditUser apiEditProfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());
        SpProvinsi = findViewById(R.id.SpProvinsi);
        SpKabupaten =findViewById(R.id.SpKabupaten);
        SpKecamatan = findViewById(R.id.SpKecamatan);
        EtNama = findViewById(R.id.EtNama);
        EtEmail = findViewById(R.id.EtEmail);
        EtNoHp = findViewById(R.id.EtNoHp);
        EtInstansi = findViewById(R.id.EtInstansi);
        EtAlamatRumah = findViewById(R.id.EtAlamatRumah);
        TxvUsername = findViewById(R.id.TxvUsername);
        BtnSimpan = findViewById(R.id.BtnSimpan);
        apiEditProfil = RetrofitClientInstance.getRetrofitInstance(EditProfilUserActivity.this).create(APIEditUser.class);
        initDataProfil();

        SpProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_provinsi = provinsiPojos.get(position).getId();
                initKabupaten();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        SpKabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_kabupaten = kabupatenPojos.get(position).getId();
                initKecamatan();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SpKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_kecamatan = kecamatanPojos.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void initProvinsi(){
        Call<ProvinsiPojoResponse> provinsiPojoCall = apiEditProfil.getProvinsi();
        provinsiPojoCall.enqueue(new Callback<ProvinsiPojoResponse>() {
            @Override
            public void onResponse(Call<ProvinsiPojoResponse> call, Response<ProvinsiPojoResponse> response) {
                if (response.body() != null){
                    if (response.body().getStatus()==200){
                        ready = ready+1;
                        provinsiPojos = response.body().getData();
                        List<String> listSpinner = new ArrayList<String>();
                        int selected = 0;
                        for (int i = 0; i < provinsiPojos.size(); i++){
                            listSpinner.add(provinsiPojos.get(i).getNama().toUpperCase());
                            if (userPojo!=null && userPojo.getProvinsi().equals(provinsiPojos.get(i).getId())){
                                selected = i;
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfilUserActivity.this,
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
    private void initKabupaten(){
        Call<KabupatenPojoResponse> kabupatenPojoResponseCall = apiEditProfil.getKabupaten(id_provinsi);
        kabupatenPojoResponseCall.enqueue(new Callback<KabupatenPojoResponse>() {
            @Override
            public void onResponse(Call<KabupatenPojoResponse> call, Response<KabupatenPojoResponse> response) {
                if (response.body()!=null){
                    if (response.body().getStatus()==200){
                        ready = ready+1;
                        int selected = 0;
                        kabupatenPojos = response.body().getData();
                        List<String> listSpinner = new ArrayList<String>();
                        for (int i = 0; i < kabupatenPojos.size(); i++){
                            listSpinner.add(kabupatenPojos.get(i).getNama().toUpperCase());
                            if (userPojo!=null && userPojo.getKabupaten().toString().equals(kabupatenPojos.get(i).getId())){
                                selected = i;
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfilUserActivity.this,
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
    private void initKecamatan(){
        Call<KecamatanPojoResponse> kecamatanPojoCall = apiEditProfil.getKecamatan(id_kabupaten);
        kecamatanPojoCall.enqueue(new Callback<KecamatanPojoResponse>() {
            @Override
            public void onResponse(Call<KecamatanPojoResponse> call, Response<KecamatanPojoResponse> response) {
                if(response.body()!=null){
                    if (response.body().getStatus()==200){
                        ready = ready+1;
                        kecamatanPojos = response.body().getData();
                        List<String> listSpinner = new ArrayList<String>();
                        int selected = 0;
                        for (int i = 0; i < kecamatanPojos.size(); i++){
                            listSpinner.add(kecamatanPojos.get(i).getNama().toUpperCase());
                            if (userPojo!=null && userPojo.getKecamatan().toString().equals(kecamatanPojos.get(i).getId())){
                                selected = i;
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfilUserActivity.this,
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
    private void initDataProfil(){
        final Call<EditUserPojoResponse> userPojoCall = apiEditProfil.getProfil();
        userPojoCall.enqueue(new Callback<EditUserPojoResponse>() {
            @Override
            public void onResponse(Call<EditUserPojoResponse> call, Response<EditUserPojoResponse> response) {
                if (response.body()!=null){
                    if (response.body().getStatus() == 200){
                        ready = ready+1;
                        userPojo = response.body().getData();
                        TxvUsername.setText(userPojo.getUsername());
                        EtEmail.setText(userPojo.getEmail());
                        EtNama.setText(userPojo.getNamaLengkap());
                        EtNoHp.setText(userPojo.getNo_hp());
                        EtAlamatRumah.setText(userPojo.getAlamat());
                        id_provinsi = userPojo.getProvinsi();
                        id_kabupaten = userPojo.getKabupaten();
                        id_kecamatan = userPojo.getKecamatan();
                        initProvinsi();
                        initKabupaten();
                        initKecamatan();

                    }
                }
            }

            @Override
            public void onFailure(Call<EditUserPojoResponse> call, Throwable t) {
                Toast.makeText(EditProfilUserActivity.this, "Terjadi gangguan jaringan", Toast.LENGTH_SHORT).show();
            }
        });
        BtnSimpan = findViewById(R.id.BtnSimpan);
        BtnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ready<4){
                    Toast.makeText(EditProfilUserActivity.this, "Coba lagi", Toast.LENGTH_SHORT).show();
                }else{
                    simpan(BtnSimpan);
                }
            }
        });
    }
    private void simpan(final Button button){
        DrawableButtonExtensionsKt.showProgress(button, new Function1<ProgressParams, Unit>() {
            @Override
            public Unit invoke(ProgressParams progressParams) {
                progressParams.setButtonTextRes(R.string.loading);
                progressParams.setProgressColor(Color.BLACK);
                return Unit.INSTANCE;
            }
        });
        button.setEnabled(false);
        final String EtEmails = EtEmail.getText().toString();
        final String EtNamas = EtNama.getText().toString();
        final String EtNoHps = EtNoHp.getText().toString();
        final String EtInstansis = EtInstansi.getText().toString();
        final String EtAlamatRumahs = EtAlamatRumah.getText().toString();
        final String EtWargaNegaras = EtWargaNegara.getText().toString();
        final String SpProvinsis = (provinsiPojos!=null) ? provinsiPojos.get(SpProvinsi.getSelectedItemPosition()).getId() : id_provinsi;
        final String SpKabupatens = (kabupatenPojos!=null) ? kabupatenPojos.get(SpKabupaten.getSelectedItemPosition()).getId() : id_kabupaten;
        final String SpKecamatans = (kecamatanPojos!=null) ? kecamatanPojos.get(SpKecamatan.getSelectedItemPosition()).getId() : id_kecamatan;
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
        if (TextUtils.isEmpty(EtNamas)) {
            EtNama.setError("Nama lengkap masih kosong");
            EtNama.requestFocus();
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
//        if (TextUtils.isEmpty(EtNoHps)) {
//            EtNoHp.setError("Nomor KTP masih kosong");
//            EtNoHp.requestFocus();
//            button.setEnabled(true);
//            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
//            return;
//        }
        if (TextUtils.isEmpty(EtAlamatRumahs)) {
            EtAlamatRumah.setError("Alamat rumah masih kosong");
            EtAlamatRumah.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<ResponsePojo> responsePojoCall = apiEditProfil.editProfil(EtNamas,EtEmails,EtNoHps,EtInstansis,SpProvinsis,SpKabupatens,SpKecamatans,EtAlamatRumahs);
                responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                    @Override
                    public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                        if (response.body()!= null){
                            if (response.body().getStatus()==200){
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
                                Toast.makeText(EditProfilUserActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
                                Toast.makeText(EditProfilUserActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsePojo> call, Throwable t) {
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
                        Toast.makeText(EditProfilUserActivity.this, "Terjadi gangguan jaringan", Toast.LENGTH_LONG).show();
                    }
                });

            }
        }, 3000);
    }
    class EditUserPojoResponse{
        @SerializedName("status")
        int status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        UserPojo data;

        public EditUserPojoResponse() {
        }

        public EditUserPojoResponse(int status, String msg, UserPojo data) {
            this.status = status;
            this.msg = msg;
            this.data = data;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public UserPojo getData() {
            return data;
        }

        public void setData(UserPojo data) {
            this.data = data;
        }
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
}
