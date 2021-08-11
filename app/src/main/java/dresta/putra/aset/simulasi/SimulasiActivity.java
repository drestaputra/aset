package dresta.putra.aset.simulasi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.text.NumberFormat;
import java.util.Locale;

import dresta.putra.aset.R;
import dresta.putra.aset.ResponsePojo;
import dresta.putra.aset.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class SimulasiActivity extends AppCompatActivity {
    private Button IvBack;
    private Button BtnSimulasi;
    private EditText EtJumlahPinjaman,EtPeriodePinjaman;
    private LinearLayout LlHasil;
    private Button[] btn = new Button[4];
    private Button btn_unfocus,btn_focus;
    private IndicatorSeekBar SbBunga;
    private NumberFormat formatRupiah;
    String terpilih = "Hari";
    private int[] btn_id = {R.id.BtnHarian, R.id.BtnMingguan, R.id.BtnBulanan, R.id.BtnTahunan};

    private interface APISimulasi{
        @GET("api/about/bunga_default")
        Call<ResponsePojo> getBungaDefault();
    }
    private APISimulasi apiSimulasi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulasi);
        IvBack = findViewById(R.id.IvBack);
        EtJumlahPinjaman = findViewById(R.id.EtJumlahPinjaman);
        EtPeriodePinjaman = findViewById(R.id.EtPeriodePinjaman);
        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        BtnSimulasi = findViewById(R.id.BtnSimulasi);
        LlHasil = findViewById(R.id.LlHasil);
        SbBunga = findViewById(R.id.SbBunga);
        apiSimulasi = RetrofitClientInstance.getRetrofitInstance(SimulasiActivity.this).create(APISimulasi.class);
        Locale localeID = new Locale("in", "ID");
        formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        SbBunga.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                hitungPinjaman();
            }
            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
                hitungPinjaman();
            }
            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                hitungPinjaman();
            }
        });
        SbBunga.setProgress(5);
        setBunga();
        BtnSimulasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungPinjaman();
            }
        });
        // membuat transparan notifikasi
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();

        Button tes = findViewById(R.id.BtnMingguan);

        terpilih = "Hari";
        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button) findViewById(btn_id[i]);
            setFocus(tes, btn[0]);
//            btn[i].setBackgroundColor(Color.rgb(207, 207, 207));
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.BtnHarian :
                            terpilih = "Hari";
                            setFocus(btn_unfocus, btn[0]);
                            break;

                        case R.id.BtnMingguan :
                            terpilih = "Minggu";
                            setFocus(btn_unfocus, btn[1]);
                            break;

                        case R.id.BtnBulanan :
                            terpilih = "Bulan";
                            setFocus(btn_unfocus, btn[2]);
                            break;

                        case R.id.BtnTahunan :
                            terpilih = "Tahun";
                            setFocus(btn_unfocus, btn[3]);
                            break;
                    }
                }
            });
        }
        btn_unfocus = btn[0];


    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    private void setBunga(){
        Call<ResponsePojo> responsePojoCall = apiSimulasi.getBungaDefault();
        responsePojoCall.enqueue(new Callback<ResponsePojo>() {
            @Override
            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                if (response.body()!=null){
                    if (response.body().getStatus()==200){
                        Float bunga = Float.parseFloat(response.body().getMsg());
                        SbBunga.setProgress(bunga);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePojo> call, Throwable t) {

            }
        });
    }
    private void setFocus(Button btn_unfocus, Button btn_focus){
        hitungPinjaman();
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.rgb(3, 106, 150));
        this.btn_unfocus = btn_focus;

    }
    private void hitungPinjaman(){
        final String EtJumlahPinjamans = EtJumlahPinjaman.getText().toString();
        final String EtPeriodePinjamans = EtPeriodePinjaman.getText().toString();
        if (TextUtils.isEmpty(EtJumlahPinjamans)) {
            EtJumlahPinjaman.setError("Mau pinjam berapa?");
            EtJumlahPinjaman.requestFocus();
            BtnSimulasi.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(EtPeriodePinjamans)) {
            EtPeriodePinjaman.setError("Berapa lama nih mau pinjam");
            EtPeriodePinjaman.requestFocus();
            BtnSimulasi.setEnabled(true);
            return;
        }
        Float jumlah_pinjaman =  Float.parseFloat(EtJumlahPinjaman.getText().toString());
        int periode_pinjaman =  Integer.parseInt(EtPeriodePinjaman.getText().toString());
        int bunga =  SbBunga.getProgress();
        Float total_pinjaman = jumlah_pinjaman+(jumlah_pinjaman*bunga/100);
        Float angsuran = total_pinjaman/periode_pinjaman;
        BtnSimulasi.setText(formatRupiah.format(angsuran)+" / "+terpilih+" x "+periode_pinjaman+" "+terpilih);
    }

}
