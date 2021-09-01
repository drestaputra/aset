package dresta.putra.aset.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.github.razir.progressbutton.ProgressParams;

import java.util.Objects;

import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.ResponsePojo;
import dresta.putra.aset.RetrofitClientInstance;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class EditPasswordUserActivity extends AppCompatActivity {
    private EditText EtPasswordLama, EtPasswordBaru, EtPasswordKonfirmasi;
    private Button BtnSimpan;
    private PrefManager prefManager;
    interface APIGantiPassword{
        @FormUrlEncoded
        @POST("api/user/ganti_password")
        Call<ResponsePojo> ganti_password(@Field("password_lama") String password_lama, @Field("password_baru") String password_baru, @Field("password_konfirmasi") String password_konfirmasi);
    }
    private APIGantiPassword servicePojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password_user);
        prefManager = new PrefManager(EditPasswordUserActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        EtPasswordLama = findViewById(R.id.EtPasswordLama);
        EtPasswordBaru = findViewById(R.id.EtPasswordBaru);
        EtPasswordKonfirmasi = findViewById(R.id.EtPasswordKonfirmasi);
        BtnSimpan = findViewById(R.id.BtnSimpan);
        servicePojo = RetrofitClientInstance.getRetrofitInstance(EditPasswordUserActivity.this).create(APIGantiPassword.class);
        BtnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan(BtnSimpan);
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
        final String EtPasswordBarus = EtPasswordBaru.getText().toString();
        final String EtPasswordLamas = EtPasswordLama.getText().toString();
        final String EtPasswordKonfirmasis = EtPasswordKonfirmasi.getText().toString();
        if (TextUtils.isEmpty(EtPasswordBarus)) {
            EtPasswordBaru.setError("Password baru kosong");
            EtPasswordBaru.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }
        if (TextUtils.isEmpty(EtPasswordLamas)) {
            EtPasswordLama.setError("Password lama kosong");
            EtPasswordLama.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }
        if (!EtPasswordKonfirmasis.equals(EtPasswordBarus)) {
            EtPasswordKonfirmasi.setError("Konfirmasi password tidak sama");
            EtPasswordKonfirmasi.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<ResponsePojo> responsePojoCall = servicePojo.ganti_password(EtPasswordLamas,EtPasswordBarus,EtPasswordKonfirmasis);
                responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                    @Override
                    public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                        if (response.body()!=null){
                            if (response.body().getStatus() == 200){
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
                                Toast.makeText(EditPasswordUserActivity.this, "Ubah password berhasil, mohon login ulang", Toast.LENGTH_LONG).show();
                                prefManager.logout();
                            }else{
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, "Simpan");
                                Toast.makeText(EditPasswordUserActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsePojo> call, Throwable t) {
                        button.setEnabled(true);
                        Toast.makeText(EditPasswordUserActivity.this, "Gagal, coba lagi", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        },3000);
    }
}
