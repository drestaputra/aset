package dresta.putra.aset.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.github.razir.progressbutton.ProgressParams;
import com.google.gson.annotations.SerializedName;

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
import dresta.putra.aset.login.LoginActivity;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class FragmentDaftarNasabah extends Fragment {
    private EditText EtUsername,EtEmail,EtNamaNasabah,EtNamaIbuKandung,EtNoHp,EtTempatLahir,EtTglLahir,EtAlamatRumah,EtWargaNegara
            ,EtPekerjaan,EtNamaPerusahaan,EtAlamatTempatKerja,EtHobi,EtMakananKesukaan;
    private Button BtnDaftar;
    private CardView CvNoAkses,CvFormulir;

    private Spinner  SpAgama,SpJenisKelamin,SpProvinsi,SpKabupaten,SpKecamatan,SpKelurahan,SpGolonganDarah;
    private String id_provinsi="",id_kabupaten="",id_kecamatan = "";
    private DatePickerDialog datePickerDialog;
    private List<ProvinsiPojo> provinsiPojos;
    private List<KabupatenPojo> kabupatenPojos;
    private List<KecamatanPojo> kecamatanPojos;
    private List<KelurahanPojo> kelurahanPojos;
    private PrefManager prefManager;

    public FragmentDaftarNasabah() {
        // Required empty public constructor
    }
    interface APIDaftarNasabah{
        @FormUrlEncoded
        @POST("api/nasabah/daftar_nasabah")
        Call<ResponsePojo> daftarNasabah(
                @Field("username") String username,
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
    }
    private APIDaftarNasabah apiDaftarNasabah;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_daftar_nasabah, container, false);
        prefManager = new PrefManager(getContext());
        EtUsername = view.findViewById(R.id.EtUsername);
        EtEmail = view.findViewById(R.id.EtEmail);
        EtNamaNasabah = view.findViewById(R.id.EtNamaNasabah);
        EtNamaIbuKandung = view.findViewById(R.id.EtNamaIbuKandung);
        EtTglLahir = view.findViewById(R.id.EtTglLahir);
        EtNoHp = view.findViewById(R.id.EtNoHp);
        EtTempatLahir = view.findViewById(R.id.EtTempatLahir);
        EtTglLahir = view.findViewById(R.id.EtTglLahir);
        EtTglLahir = view.findViewById(R.id.EtTglLahir);
        SpProvinsi = view.findViewById(R.id.SpProvinsi);
        SpKabupaten = view.findViewById(R.id.SpKabupaten);
        SpKecamatan = view.findViewById(R.id.SpKecamatan);
        SpKelurahan = view.findViewById(R.id.SpKelurahan);
        EtAlamatRumah = view.findViewById(R.id.EtAlamatRumah);
        EtWargaNegara = view.findViewById(R.id.EtWargaNegara);
        EtPekerjaan = view.findViewById(R.id.EtPekerjaan);

        EtNamaPerusahaan = view.findViewById(R.id.EtNamaPerusahaan);
        EtAlamatTempatKerja = view.findViewById(R.id.EtAlamatTempatKerja);
        EtHobi = view.findViewById(R.id.EtHobi);
        EtMakananKesukaan = view.findViewById(R.id.EtMakananKesukaan);
        CvFormulir = view.findViewById(R.id.CvFormulir);
        CvNoAkses = view.findViewById(R.id.CvNoAkses);
        if (!(prefManager.isKolektorLoggedIn())){
            CvFormulir.setVisibility(View.GONE);
            CvNoAkses.setVisibility(View.VISIBLE);
            Button BtnLogin = view.findViewById(R.id.BtnLogin);
            BtnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Ilogin = new Intent(getContext(), LoginActivity.class);
                    startActivity(Ilogin);
                }
            });

        }else{
            CvNoAkses.setVisibility(View.GONE);
            CvFormulir.setVisibility(View.VISIBLE);
        }
        BtnDaftar = view.findViewById(R.id.BtnDaftar);
        apiDaftarNasabah = RetrofitClientInstance.getRetrofitInstance(getContext()).create(APIDaftarNasabah.class);
        Call<ProvinsiPojoResponse> provinsiPojoCall = apiDaftarNasabah.getProvinsi();
        provinsiPojoCall.enqueue(new Callback<ProvinsiPojoResponse>() {
            @Override
            public void onResponse(Call<ProvinsiPojoResponse> call, Response<ProvinsiPojoResponse> response) {
                if (response.body() != null){
                    if (response.body().getStatus()==200){
                        provinsiPojos = response.body().getData();
                        List<String> listSpinner = new ArrayList<String>();
                        for (int i = 0; i < provinsiPojos.size(); i++){
                            listSpinner.add(provinsiPojos.get(i).getNama().toUpperCase());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, listSpinner);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SpProvinsi.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProvinsiPojoResponse> call, Throwable t) {
            }
        });

        SpProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_provinsi = provinsiPojos.get(position).getId();
                Call<KabupatenPojoResponse> kabupatenPojoResponseCall = apiDaftarNasabah.getKabupaten(id_provinsi);
                kabupatenPojoResponseCall.enqueue(new Callback<KabupatenPojoResponse>() {
                    @Override
                    public void onResponse(Call<KabupatenPojoResponse> call, Response<KabupatenPojoResponse> response) {
                        if (response.body()!=null){
                            if (response.body().getStatus()==200){
                                kabupatenPojos = response.body().getData();
                                List<String> listSpinner = new ArrayList<String>();
                                for (int i = 0; i < kabupatenPojos.size(); i++){
                                    listSpinner.add(kabupatenPojos.get(i).getNama().toUpperCase());
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                        android.R.layout.simple_spinner_item, listSpinner);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                SpKabupaten.setAdapter(adapter);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<KabupatenPojoResponse> call, Throwable t) {
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SpKabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_kabupaten = kabupatenPojos.get(position).getId();
                Call<KecamatanPojoResponse> kecamatanPojoCall = apiDaftarNasabah.getKecamatan(id_kabupaten);
                kecamatanPojoCall.enqueue(new Callback<KecamatanPojoResponse>() {
                    @Override
                    public void onResponse(Call<KecamatanPojoResponse> call, Response<KecamatanPojoResponse> response) {
                        if(response.body()!=null){
                            if (response.body().getStatus()==200){
                                kecamatanPojos = response.body().getData();
                                List<String> listSpinner = new ArrayList<String>();
                                for (int i = 0; i < kecamatanPojos.size(); i++){
                                    listSpinner.add(kecamatanPojos.get(i).getNama().toUpperCase());
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                        android.R.layout.simple_spinner_item, listSpinner);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                SpKecamatan.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<KecamatanPojoResponse> call, Throwable t) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SpKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_kecamatan = kecamatanPojos.get(position).getId();
                Call<KelurahanPojoResponse> kelurahanPojoResponseCall = apiDaftarNasabah.getKelurahan(id_kecamatan);
                kelurahanPojoResponseCall.enqueue(new Callback<KelurahanPojoResponse>() {
                    @Override
                    public void onResponse(Call<KelurahanPojoResponse> call, Response<KelurahanPojoResponse> response) {
                        if (response.body()!=null){
                            if (response.body().getStatus()==200){
                                kelurahanPojos = response.body().getData();
                                List<String> listSpinner = new ArrayList<String>();

                                for (int i = 0; i < kelurahanPojos.size(); i++){
                                    listSpinner.add(kelurahanPojos.get(i).getNama().toUpperCase());
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                        android.R.layout.simple_spinner_item, listSpinner);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                SpKelurahan.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<KelurahanPojoResponse> call, Throwable t) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EtTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateStartDialog();
            }
        });
        SpAgama = view.findViewById(R.id.SpAgama);
        String[] arraySpinnerAgama = new String[] {
                "Buddha", "Hindu","Islam","Katolik","Khonghucu","Protestan"
        };
        ArrayAdapter<String> adapterAgama = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_black, arraySpinnerAgama);
        adapterAgama.setDropDownViewResource(R.layout.spinner_black);
        SpAgama.setAdapter(adapterAgama);
        SpJenisKelamin = view.findViewById(R.id.SpJenisKelamin);
        String[] arraySpinnerJenisKelamin = new String[] {
                "laki-laki", "perempuan"
        };
        ArrayAdapter<String> adapterJenisKelamin = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_black, arraySpinnerJenisKelamin);
        adapterJenisKelamin.setDropDownViewResource(R.layout.spinner_black);
        SpJenisKelamin.setAdapter(adapterJenisKelamin);
        SpGolonganDarah = view.findViewById(R.id.SpGolonganDarah);
        String[] arraySpinnerGoldar = new String[] {
                "A", "B","AB","O"
        };
        ArrayAdapter<String> adapterGoldar = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_black, arraySpinnerGoldar);
        adapterGoldar.setDropDownViewResource(R.layout.spinner_black);
        SpGolonganDarah.setAdapter(adapterGoldar);
        BtnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daftar(BtnDaftar);
            }
        });
        return view;
    }
    private void daftar(final Button button){
        DrawableButtonExtensionsKt.showProgress(button, new Function1<ProgressParams, Unit>() {
            @Override
            public Unit invoke(ProgressParams progressParams) {
                progressParams.setButtonTextRes(R.string.loading);
                progressParams.setProgressColor(Color.WHITE);
                return Unit.INSTANCE;
            }
        });
        button.setEnabled(false);
        final String EtUsernames = EtUsername.getText().toString();
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
        if (TextUtils.isEmpty(EtUsernames)) {
            EtUsername.setError("Username masih kosong");
            EtUsername.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Daftar");
            return;
        }
        if (TextUtils.isEmpty(EtEmails)) {
            EtEmail.setError("Email masih kosong");
            EtEmail.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Daftar");
            return;
        }
        if (!TextUtils.isEmpty(EtEmails) && !Patterns.EMAIL_ADDRESS.matcher(EtEmails).matches()) {
            EtEmail.setError("Format email tidak sesuai");
            EtEmail.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Daftar");
            return;
        }
        if (TextUtils.isEmpty(EtNamaNasabahs)) {
            EtNamaNasabah.setError("Nama lengkap masih kosong");
            EtNamaNasabah.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Daftar");
            return;
        }
        if (TextUtils.isEmpty(EtNamaIbuKandungs)) {
            EtNamaIbuKandung.setError("Nama ibu kandung masih kosong");
            EtNamaIbuKandung.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Daftar");
            return;
        }
        if (TextUtils.isEmpty(EtNoHps)) {
            EtNoHp.setError("Nomor HP masih kosong");
            EtNoHp.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Daftar");
            return;
        }
        if (TextUtils.isEmpty(EtTempatLahirs)) {
            EtTempatLahir.setError("Tempat lahir masih kosong");
            EtTempatLahir.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Daftar");
            return;
        }
        if (TextUtils.isEmpty(EtTglLahirs)) {
            EtTglLahir.setError("Tanggal lahir masih kosong");
            EtTglLahir.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Daftar");
            return;
        }
        if (TextUtils.isEmpty(EtAlamatRumahs)) {
            EtAlamatRumah.setError("Alamat rumah masih kosong");
            EtAlamatRumah.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Daftar");
            return;
        }
        if (TextUtils.isEmpty(EtWargaNegaras)) {
            EtWargaNegara.setError("Kewarga_negaraan masih kosong");
            EtWargaNegara.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Daftar");
            return;
        }if (TextUtils.isEmpty(EtPekerjaans)) {
            EtPekerjaan.setError("Pekerjaan masih kosong");
            EtPekerjaan.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Daftar");
            return;
        }if (TextUtils.isEmpty(EtNamaPerusahaans)) {
            EtNamaPerusahaan.setError("Nama usaha / perusahaan masih kosong");
            EtNamaPerusahaan.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Daftar");
            return;
        }if (TextUtils.isEmpty(EtAlamatTempatKerjas)) {
            EtAlamatTempatKerja.setError("Alamat usaha / perusahaan masih kosong");
            EtAlamatTempatKerja.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Daftar");
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<ResponsePojo> responsePojoCall = apiDaftarNasabah.daftarNasabah(
                        EtUsernames,EtEmails,EtNamaNasabahs,EtNamaIbuKandungs,EtNoHps,SpJenisKelamins,EtTempatLahirs,EtTglLahirs,EtAlamatRumahs
                        ,SpProvinsis,SpKabupatens,SpKecamatans,SpKelurahans,EtWargaNegaras,EtPekerjaans, EtAlamatTempatKerjas,EtNamaPerusahaans,SpAgamas,SpGoldars,EtHobis,EtMakananKesukaans);
                responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                    @Override
                    public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                        if (response.body()!= null){
                            if (response.body().getStatus()==200){
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, R.string.progressRight);
                                EtUsername.setText(null);
                                EtEmail.setText(null);
                                EtNamaNasabah.setText(null);
                                EtNamaIbuKandung.setText(null);
                                EtNoHp.setText(null);
                                EtTempatLahir.setText(null);
                                EtTglLahir.setText(null);
                                EtAlamatRumah.setText(null);
                                EtWargaNegara.setText(null);
                                EtPekerjaan.setText(null);
                                EtNamaPerusahaan.setText(null);
                                EtHobi.setText(null);
                                EtMakananKesukaan.setText(null);
                                Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }else{
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, R.string.progressRight);
                                Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsePojo> call, Throwable t) {
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, "Gagal, KLIK lagi");
                        Toast.makeText(getContext(), "Terjadi gangguan jaringan", Toast.LENGTH_LONG).show();
                    }
                });

            }
        }, 3000);
    }
    private void showDateStartDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Locale localeID = new Locale("in", "ID");
                SimpleDateFormat format = new SimpleDateFormat("EEEE,  d MMMM yyyy ", localeID);
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
