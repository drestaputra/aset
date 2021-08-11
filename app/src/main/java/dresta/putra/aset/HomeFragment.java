package dresta.putra.aset;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import dresta.putra.aset.angsuran.BayarAngsuranActivity;
import dresta.putra.aset.daftar.DaftarActivity;
import dresta.putra.aset.informasi_program.InformasiProgramActivity;
import dresta.putra.aset.jadwal.JadwalActivity;
import dresta.putra.aset.login.LoginActivity;
import dresta.putra.aset.nasabah.NasabahActivity;
import dresta.putra.aset.oper_berkas.OperBerkasNasabahActivity;
import dresta.putra.aset.paket.AdapterPaket;
import dresta.putra.aset.paket.PaketPojoResponse;
import dresta.putra.aset.pengaduan.PengaduanActivity;
import dresta.putra.aset.setoran.LihatSetoranActivity;
import dresta.putra.aset.simulasi.SimulasiActivity;
import dresta.putra.aset.slider.AdapterSlider;
import dresta.putra.aset.slider.SliderPojoResponse;
import dresta.putra.aset.transaksi.TransaksiActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class HomeFragment extends Fragment {
    private ViewPager viewPager;
    private RecyclerView viewPagerPaket;
    private AdapterSlider adapter;
    private AdapterPaket adapterPaket;
    private PrefManager prefManager;
    private ShimmerFrameLayout mShimmerViewContainerPaket,mShimmerViewContainerSlider;
    private GridLayoutManager linearLayoutManager;
    private LinearLayout LlNasabah,LlOperBerkas,LlInformasiProgram,LlSimulasi,LlPengaduan,LlJadwal,LlAngsur,LlTransaksi,LlLihatSetoran;
    private CardView CvRatioModal;
    private TextView TxvRatioModal, TxvRatioModalMsg;
    private ProgressBar PbRatioModal;

    public HomeFragment() {
        // Required empty public constructor
    }
    interface APIHomeFragment{

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);



        return view;
    }


}
