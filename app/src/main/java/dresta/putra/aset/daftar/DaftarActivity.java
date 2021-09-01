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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import dresta.putra.aset.MainActivity;
import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.login.LoginActivity;
import dresta.putra.aset.user.UserPojo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class DaftarActivity extends AppCompatActivity {
    EditText username_input,EtPassword,EtNamaLengkap,EtInstansi,EtConfPassword;
    TextView TxvLogin;
    Button BtnDaftar;
    Vibrator v;
    private PrefManager prefManager;


    interface MyAPIService {
        @FormUrlEncoded
        @POST("api/daftar/user")
        Call<DaftarResponsePojo> DaftarUser(
                @Field("nama_lengkap") String nama_lengkap,
                @Field("instansi") String instansi,
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
        EtInstansi = findViewById(R.id.EtInstansi);
        EtConfPassword = findViewById(R.id.EtConfPassword);
        BtnDaftar = findViewById(R.id.BtnDaftar);
        TxvLogin = findViewById(R.id.TxvLogin);
        ImageView IvBack = findViewById(R.id.IvBack);
        IvBack.setOnClickListener(v1 -> finish());

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        prefManager = new PrefManager(this);
        boolean userLoggedIn= prefManager.isUserLoggedIn();
        if (userLoggedIn) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        TxvLogin.setOnClickListener(view -> finish());


        //when someone clicks on login
        BtnDaftar.setOnClickListener(view -> {
//                ke halaman regist
            validateUserData();
        });

    }
    private void validateUserData() {
        //fflagirst getting the values
        final String nama_lengkaps = EtNamaLengkap.getText().toString();
        final String instansis = EtInstansi.getText().toString();
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

        if (TextUtils.isEmpty(nama_lengkaps)) {
            EtNamaLengkap.setError("Nama masih kosong");
            EtNamaLengkap.requestFocus();
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
        loginUser(username,passwords,nama_lengkaps,instansis);


    }
    
    private void loginUser(final String username, String password, String nama_lengkap, String instansi) {
        //making api call
        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance(DaftarActivity.this).create(MyAPIService.class);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(DaftarActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setTitle("Daftar");
        progressDoalog.setMessage("Daftar....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Objects.requireNonNull(progressDoalog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        progressDoalog.show();

        Call<DaftarResponsePojo> call = myAPIService.DaftarUser(nama_lengkap,instansi, username, password);

        call.enqueue(new Callback<DaftarResponsePojo>() {

            @Override
            public void onResponse(Call<DaftarResponsePojo> call, Response<DaftarResponsePojo> response) {

                if (response.body() != null) {
                    progressDoalog.dismiss();
                    if(response.body().getStatus()==200){
                        UserPojo userPojo = response.body().getData();
//                        set_sess(userPojo.getId_user()userPojo.getUsername());
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
    public void set_sess(String id_user,String username, String password){
        prefManager.storeDataUser(id_user,username,password);
    }


    private static class DaftarResponsePojo{
        @SerializedName("status")
        Integer status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        UserPojo data=null;

        public DaftarResponsePojo(Integer status, String msg, UserPojo data) {
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

        public UserPojo getData() {
            return data;
        }

        public void setData(UserPojo data) {
            this.data = data;
        }
    }

}

