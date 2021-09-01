package dresta.putra.aset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dresta.putra.aset.peta.AsetActivity;
import dresta.putra.aset.user.EditProfilUserActivity;

public class CariActivity extends AppCompatActivity {
    private Spinner SpJenisHak, SpLuastTanah;
    EditText EtNamaAset, EtKodeBarang, EtTahunPerolehan, EtAlamat, EtNomorSertifikat;
    Button BtnCari;
    Bundle bundleIntent;
    private String nama_aset,kode_barang, tahun_perolehan, alamat, nomor_sertifikat, jenis_hak, SpJenisHaks,luas_tanah, SpLuasTanahs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari);
        EtNamaAset = findViewById(R.id.EtNamaAset);
        EtKodeBarang = findViewById(R.id.EtKodeBarang);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());
        EtTahunPerolehan = findViewById(R.id.EtTahunPerolehan);
        EtAlamat = findViewById(R.id.EtAlamat);
        EtNomorSertifikat = findViewById(R.id.EtNomorSertifikat);
        SpJenisHak = findViewById(R.id.SpJenisHak);
        List<String> listSpinner = new ArrayList<String>();
        listSpinner.add("Pilih");
        listSpinner.add("Hak Pakai");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CariActivity.this,
                android.R.layout.simple_spinner_item, listSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpJenisHak.setAdapter(adapter);

        SpLuastTanah = findViewById(R.id.SpLuasTanah);
        List<String> listSpinnerLuasTanah = new ArrayList<String>();
        listSpinnerLuasTanah.add("Pilih");
        listSpinnerLuasTanah.add("0-100");
        listSpinnerLuasTanah.add("101-500");
        listSpinnerLuasTanah.add("501-1000");
        listSpinnerLuasTanah.add("1001-2000");
        listSpinnerLuasTanah.add("2001-4000");
        listSpinnerLuasTanah.add("4001-6000");
        listSpinnerLuasTanah.add("6001-8000");
        listSpinnerLuasTanah.add("8001-10000");
        listSpinnerLuasTanah.add(">10000");
        ArrayAdapter<String> adapterSpinnerLuasTanah = new ArrayAdapter<String>(CariActivity.this, android.R.layout.simple_spinner_item, listSpinnerLuasTanah);
        adapterSpinnerLuasTanah.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpLuastTanah.setAdapter(adapterSpinnerLuasTanah);



        int selectedJenisHak = 0;
        int selectedLuasTanah = 0;
        bundleIntent = getIntent().getExtras();
        if (bundleIntent != null){
            EtNamaAset.setText(String.valueOf(bundleIntent.getString("nama_aset")));
            EtKodeBarang.setText(String.valueOf(bundleIntent.getString("kode_barang")));
            EtTahunPerolehan.setText(String.valueOf(bundleIntent.getString("tahun_perolehan")));
            EtAlamat.setText(String.valueOf(bundleIntent.getString("alamat")));

            jenis_hak = String.valueOf(bundleIntent.getString("jenis_hak"));
            for (int i = 0; i < listSpinner.size() ; i++) {
                if (listSpinner.get(i).equals(jenis_hak)){
                    selectedJenisHak = i;
                }
            }
            SpJenisHak.setSelection(selectedJenisHak);

            luas_tanah = String.valueOf(bundleIntent.getString("luas_tanah"));
            for (int i = 0; i < listSpinnerLuasTanah.size() ; i++) {
                if (listSpinnerLuasTanah.get(i).equals(luas_tanah)){
                    selectedLuasTanah = i;
                }
            }
            SpLuastTanah.setSelection(selectedLuasTanah);
        }







        BtnCari = findViewById(R.id.BtnCari);
        BtnCari.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("nama_aset",String.valueOf(EtNamaAset.getText()));
            bundle.putString("kode_barang",String.valueOf(EtKodeBarang.getText()));
            bundle.putString("tahun_perolehan",String.valueOf(EtTahunPerolehan.getText()));
            bundle.putString("alamat",String.valueOf(EtAlamat.getText()));
            bundle.putString("nomor_sertifikat",String.valueOf(EtNomorSertifikat.getText()));
            SpJenisHaks = String.valueOf(listSpinner.get(SpJenisHak.getSelectedItemPosition()));
            bundle.putString("jenis_hak",String.valueOf(SpJenisHaks));
            SpLuasTanahs = String.valueOf(listSpinnerLuasTanah.get(SpLuastTanah.getSelectedItemPosition()));
            bundle.putString("luas_tanah",String.valueOf(SpLuasTanahs));
            Intent intent = new Intent(CariActivity.this, AsetActivity.class);
            intent.putExtras(bundle);
            finish();
            startActivity(intent);
        });
    }
}