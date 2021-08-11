package dresta.putra.aset.transaksi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;

import dresta.putra.aset.R;
import dresta.putra.aset.pinjaman.PinjamanActivity;
import dresta.putra.aset.simpanan.SimpananActivity;

public class TransaksiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CardView CvSimpanan = findViewById(R.id.CvSimpanan);
        CvSimpanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ISimpanan = new Intent(TransaksiActivity.this, SimpananActivity.class);
                startActivity(ISimpanan);
                overridePendingTransition(0, 0);
            }
        });
        CardView CvPinjaman = findViewById(R.id.CvPinjaman);
        CvPinjaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IPinjaman = new Intent(TransaksiActivity.this, PinjamanActivity.class);
                startActivity(IPinjaman);
                overridePendingTransition(0, 0);
            }
        });
    }
}
