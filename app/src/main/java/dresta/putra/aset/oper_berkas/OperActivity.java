package dresta.putra.aset.oper_berkas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import dresta.putra.aset.R;
import dresta.putra.aset.ResponsePojo;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.kolektor.KolektorPojo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class OperActivity extends AppCompatActivity {

    private Button BtnOper;
    private String id_nasabah;
    private Spinner SpUsername;
    private EditText EtPassword;
    private TextView TxvTitleUsername;
    List<KolektorPojo> kolektorPojos = null;
    interface APIOper{
        @GET("api/kolektor/get_all_kolektor_username")
        Call<KolektorUsernameResponsePojo> getAllKolektorUsername();
        @FormUrlEncoded
        @POST("api/kolektor/request_oper_berkas")
        Call<ResponsePojo> requestOperBerkas(@Field("id_nasabah") String id_nasabah,@Field("username") String username, @Field("password") String password);
    }
    private APIOper apiOper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oper);
        EtPassword = findViewById(R.id.EtPassword);
        SpUsername = findViewById(R.id.SpUsername);
        id_nasabah = getIntent().getStringExtra("id_nasabah");
        apiOper = RetrofitClientInstance.getRetrofitInstance(OperActivity.this).create(APIOper.class);
        Button BtBack = findViewById(R.id.BtBack);
        TxvTitleUsername = findViewById(R.id.TxvTitleUsername);
        BtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        BtnOper = findViewById(R.id.BtnOper);
        BtnOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestOper();
            }
        });

        Call<KolektorUsernameResponsePojo> kolektorResponsePojoCall = apiOper.getAllKolektorUsername();
        kolektorResponsePojoCall.enqueue(new Callback<KolektorUsernameResponsePojo>() {
            @Override
            public void onResponse(Call<KolektorUsernameResponsePojo> call, Response<KolektorUsernameResponsePojo> response) {
                if (response.body()!=null){
                    if (response.body().getStatus() == 200){
                        kolektorPojos = response.body().getData();
                        List<String> kolektorUn = new ArrayList<String>();
                        for (int i = 0; i < kolektorPojos.size(); i++){
                            kolektorUn.add(kolektorPojos.get(i).getUsername().toUpperCase());
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(OperActivity.this,
                                android.R.layout.simple_spinner_item, kolektorUn);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SpUsername.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<KolektorUsernameResponsePojo> call, Throwable t) {

            }
        });

    }
    class KolektorUsernameResponsePojo{
        @SerializedName("status")
        Integer status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        List<KolektorPojo> data;

        public KolektorUsernameResponsePojo() {
        }

        public KolektorUsernameResponsePojo(Integer status, String msg, List<KolektorPojo> data) {
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

        public List<KolektorPojo> getData() {
            return data;
        }

        public void setData(List<KolektorPojo> data) {
            this.data = data;
        }
    }
    private void requestOper(){
        Log.d("tesrequest", "requestOper: ");
        final String passwordS = EtPassword.getText().toString();
        final String usernameS = (kolektorPojos.size()!=0) ? kolektorPojos.get(SpUsername.getSelectedItemPosition()).getUsername() : null;
        //checking if username is empty
        if (TextUtils.isEmpty(usernameS)) {
            Toast.makeText(this, "Username kolektor penerima masih kosong", Toast.LENGTH_SHORT).show();
            TxvTitleUsername.requestFocus();
            BtnOper.setEnabled(true);
            return;
        }
        //checking if password is empty
        if (TextUtils.isEmpty(passwordS)) {
            EtPassword.setError("Password masih kosong");
            EtPassword.requestFocus();
            BtnOper.setEnabled(true);
            return;
        }
        Call<ResponsePojo> responsePojoCall = apiOper.requestOperBerkas(id_nasabah,usernameS,passwordS);
        responsePojoCall.enqueue(new Callback<ResponsePojo>() {
            @Override
            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                if (response.body()!=null){
                    Log.d("tesrequest2", "requestOper: ");
                    Toast.makeText(OperActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    if (response.body().getStatus()==200){
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePojo> call, Throwable t) {
                Toast.makeText(OperActivity.this, "Terjadi gangguan jaringan, coba lagi", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

