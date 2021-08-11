package dresta.putra.aset.daftar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.annotations.SerializedName;

import dresta.putra.aset.MainActivity;
import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.login.KolektorPojo;
import dresta.putra.aset.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class DaftarActivity extends AppCompatActivity {
    EditText username_input,EtPassword,EtNamaLengkap,EtEmail,EtAlamat,EtConfPassword;
    TextView link_daftar,TxvLogin;
    Button BtnDaftar;
    Vibrator v;
    private PrefManager prefManager;


    interface MyAPIService {
        @FormUrlEncoded
        @POST("api/daftar/kolektor")
        Call<DaftarResponsePojo> DaftarKolektor(
                @Field("nama_lengkap") String nama_lengkap,
                @Field("email") String email,
                @Field("alamat") String alamat,
                @Field("username") String username,
                @Field("password") String password);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        username_input = findViewById(R.id.userName);
        EtPassword = findViewById(R.id.EtPassword);
        EtNamaLengkap = findViewById(R.id.EtNamaLengkap);
        EtEmail = findViewById(R.id.EtEmail);
        EtAlamat = findViewById(R.id.EtAlamat);
        EtConfPassword = findViewById(R.id.EtConfPassword);
        BtnDaftar = findViewById(R.id.BtnDaftar);
        TxvLogin = findViewById(R.id.TxvLogin);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        prefManager = new PrefManager(this);
        boolean kolektorLoggedIn= prefManager.isKolektorLoggedIn();
        if (kolektorLoggedIn) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        TxvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                validateUserData();
                Intent Ihome = new Intent(DaftarActivity.this,LoginActivity.class);
                startActivity(Ihome);
            }
        });


        //when someone clicks on login
        BtnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ke halaman regist
                validateUserData();
            }
        });

    }
    private void validateUserData() {
        //fflagirst getting the values
        final String nama_lengkaps = EtNamaLengkap.getText().toString();
        final String emails = EtEmail.getText().toString();
        final String alamats = EtAlamat.getText().toString();
        final String conf_passwords = EtConfPassword.getText().toString();
        final String username= username_input.getText().toString();
        final String passwords = EtPassword.getText().toString();

        //checking if username is empty
        if (TextUtils.isEmpty(username)) {
            username_input.setError("Username masih kosong");
            username_input.requestFocus();
            // Vibrate for 100 milliseconds
            v.vibrate(100);
            BtnDaftar.setEnabled(true);
            return;
        }
        //checking if username is empty
        if (TextUtils.isEmpty(alamats)) {
            EtAlamat.setError("Alamat masih kosong");
            EtAlamat.requestFocus();
            // Vibrate for 100 milliseconds
            v.vibrate(100);
            BtnDaftar.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(nama_lengkaps)) {
            EtNamaLengkap.setError("Nama masih kosong");
            EtNamaLengkap.requestFocus();
            // Vibrate for 100 milliseconds
            v.vibrate(100);
            BtnDaftar.setEnabled(true);
            return;
        }
        //checking if username is empty
        if (TextUtils.isEmpty(emails)) {
            EtEmail.setError("Email masih kosong");
            EtEmail.requestFocus();
            // Vibrate for 100 milliseconds
            v.vibrate(100);
            BtnDaftar.setEnabled(true);
            return;
        }
        //checking if password is empty
        if (TextUtils.isEmpty(passwords)) {
            EtPassword.setError("Password masih kosong");
            EtPassword.requestFocus();
            v.vibrate(100);
            BtnDaftar.setEnabled(true);
            return;
        }
        if (!conf_passwords.equals(passwords)){
            EtConfPassword.setError("Password tidak sama");
            EtConfPassword.requestFocus();
            v.vibrate(100);
            BtnDaftar.setEnabled(true);
            return;
        }

        
        //Login User if everything is fine
        loginUser(username,passwords,nama_lengkaps,emails,alamats);


    }
    
    private void loginUser(final String username, String password, String nama_lengkap, String email, String alamat) {
        //making api call
        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance(DaftarActivity.this).create(MyAPIService.class);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(DaftarActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setTitle("Daftar");
        progressDoalog.setMessage("Daftar....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        progressDoalog.show();

        Call<DaftarResponsePojo> call = myAPIService.DaftarKolektor(nama_lengkap,email,alamat,username,password);

        call.enqueue(new Callback<DaftarResponsePojo>() {

            @Override
            public void onResponse(Call<DaftarResponsePojo> call, Response<DaftarResponsePojo> response) {

                if (response.body() != null) {
                    progressDoalog.dismiss();
                    if(response.body().getStatus()==200){
                        KolektorPojo kolektorPojo = response.body().getData();
//                        set_sess(kolektorPojo.getId_kolektor()kolektorPojo.getUsername());
                        startActivity(new Intent(DaftarActivity.this,LoginActivity.class));
                        Toast.makeText(DaftarActivity.this, "Berhasil Mendaftar, silahkan login", Toast.LENGTH_SHORT).show();
                    }else{
                        CoordinatorLayout ClParent = findViewById(R.id.ClDaftar);
                        Snackbar snack = Snackbar.make(ClParent, response.body().getMsg(), Snackbar.LENGTH_LONG);
                        View view = snack.getView();
                        CoordinatorLayout.LayoutParams params=(CoordinatorLayout.LayoutParams)view.getLayoutParams();
                        params.gravity = Gravity.TOP;
                        view.setLayoutParams(params);
                        snack.show();
                    }
                }
            }
            @Override
            public void onFailure(Call<DaftarResponsePojo> call, Throwable throwable) {
                progressDoalog.dismiss();
                CoordinatorLayout ClParent = findViewById(R.id.ClDaftar);
                Snackbar snack = Snackbar.make(ClParent, "Gagal Mendaftar, Coba Lagi", Snackbar.LENGTH_LONG);
                View view = snack.getView();
                CoordinatorLayout.LayoutParams params=(CoordinatorLayout.LayoutParams)view.getLayoutParams();
                params.gravity = Gravity.TOP;
                view.setLayoutParams(params);
                snack.show();
            }
        });

    }
    public void set_sess(String id_kolektor,String username, String password){
        prefManager.storeDataKolektor(id_kolektor,username,password);
    }


    private class DaftarResponsePojo{
        @SerializedName("status")
        Integer status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        KolektorPojo data=null;

        public DaftarResponsePojo(Integer status, String msg, KolektorPojo data) {
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

        public KolektorPojo getData() {
            return data;
        }

        public void setData(KolektorPojo data) {
            this.data = data;
        }
    }

}

