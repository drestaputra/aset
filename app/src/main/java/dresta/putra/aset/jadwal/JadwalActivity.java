package dresta.putra.aset.jadwal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.oper_berkas.OperBerkasAdapter;
import dresta.putra.aset.pinjaman.PinjamanPojo;
import dresta.putra.aset.pinjaman.PinjamanResponsePojo;
import dresta.putra.aset.utils.PaginationScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import java.text.DateFormatSymbols;

public class JadwalActivity extends AppCompatActivity {
    private TextView TxvTanggal;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int PER_PAGE=20;
    private int currentPage = PAGE_START;
    private String query_pencarian="";
    private OperBerkasAdapter adapter;
    private GridLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private String tgl;
    private PrefManager prefManager;
    private CalendarView CalvJadwal;
    private Locale localeID;
    interface APIJadwal{
        @FormUrlEncoded
        @POST("api/jadwal/data_jadwal_tagihan")
        Call<PinjamanResponsePojo> getDataPinjaman(
                @Field("tgl") String tgl,
                @Field("page") int page,
                @Field("perPage") int perPage
        );
    }
    private APIJadwal ServicePojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);
        TxvTanggal = findViewById(R.id.TxvTanggal);

        mShimmerViewContainer = findViewById(R.id.shimmer_card_view);
        localeID = new Locale("in", "ID");
        CalvJadwal = findViewById(R.id.CalvJadwal);
        Configuration config = new Configuration();
        config.locale = localeID;
        getApplicationContext().getResources().updateConfiguration(config, null);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",localeID);
        SimpleDateFormat tgl_sekarang = new SimpleDateFormat("dd MMMM yyyy",localeID);
        SimpleDateFormat tgl_sekarang_ymd = new SimpleDateFormat("yyyy-MM-dd",localeID);
        final String selectedDate = tgl_sekarang.format(new Date(CalvJadwal.getDate()));
        final String selectedDate_ymd = tgl_sekarang_ymd.format(new Date(CalvJadwal.getDate()));
        tgl = selectedDate_ymd;
        TxvTanggal.setText("Jadwal tagihan : "+tgl);
        CalvJadwal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                adapter.clear();
                mShimmerViewContainer.startShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                TxvTanggal.setText("Jadwal tagihan : "+dayOfMonth+" "+getMonth(month)+" "+ year);
                tgl = year+"-"+(month+1)+"-"+dayOfMonth;
                PinjamanResponsePojoCall().enqueue(new Callback<PinjamanResponsePojo>() {
                    @Override
                    public void onResponse(Call<PinjamanResponsePojo> call, Response<PinjamanResponsePojo> response) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 200 && response.body().getTotalRecords() != 0) {
                                mShimmerViewContainer.stopShimmerAnimation();
                                mShimmerViewContainer.setVisibility(View.GONE);
                                List<PinjamanPojo> results = fetchResults(response);
                                adapter.addAll(results);
                                TOTAL_PAGES = response.body().getTotalPage();
//
//
                                if (currentPage <= TOTAL_PAGES - 1) {
                                    adapter.addLoadingFooter();
                                } else {
                                    isLastPage = true;
                                }
                            } else {
                                mShimmerViewContainer.stopShimmerAnimation();
                                mShimmerViewContainer.setVisibility(View.GONE);
                                Toast.makeText(JadwalActivity.this, "Belum ada Data untuk saat ini", Toast.LENGTH_SHORT).show();
                                adapter.clear();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PinjamanResponsePojo> call, Throwable t) {

                    }
                });
            }
        });

        RecyclerView rv = findViewById(R.id.RvData);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Jadwal tagihan");
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        prefManager = new PrefManager(this);
        ServicePojo = RetrofitClientInstance.getRetrofitInstance(JadwalActivity.this).create(APIJadwal.class);
        loadFirstPage();
        adapter = new OperBerkasAdapter(JadwalActivity.this);
        linearLayoutManager = new GridLayoutManager(this,1);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    public String getMonth(int month) { return DateFormatSymbols.getInstance(localeID).getMonths()[month]; }
    private void loadFirstPage() {
        PinjamanResponsePojoCall().enqueue(new Callback<PinjamanResponsePojo>() {
            @Override
            public void onResponse(Call<PinjamanResponsePojo> call, Response<PinjamanResponsePojo> response) {
                // Got data. Send it to adapter
                if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
//                    Toast.makeText(JadwalActivity.this, "test", Toast.LENGTH_SHORT).show();
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    List<PinjamanPojo> results = fetchResults(response);
                    adapter.addAll(results);
                    TOTAL_PAGES=response.body().getTotalPage();
//
//
                    if (currentPage <= TOTAL_PAGES-1) {
                        adapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                }else{
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    adapter.clear();
                    Toast.makeText(JadwalActivity.this, "Belum ada Data untuk saat ini", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PinjamanResponsePojo> call, Throwable t) {
                    t.printStackTrace();
                    adapter.clear();
            }
        });

    }
    private void loadNextPage() {
        PinjamanResponsePojoCall().enqueue(new Callback<PinjamanResponsePojo>() {
            @Override
            public void onResponse(Call<PinjamanResponsePojo> call, Response<PinjamanResponsePojo> response) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                adapter.removeLoadingFooter();
                isLoading = false;

                List<PinjamanPojo> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<PinjamanResponsePojo> call, Throwable t) {
                    t.printStackTrace();
                    adapter.clear();
            }
        });
    }
    /**
     * @param response extracts List<{@link PinjamanPojo>} from response
     * @return
     */
    private List<PinjamanPojo> fetchResults(Response<PinjamanResponsePojo> response) {
        PinjamanResponsePojo topRatedMovies = response.body();
        return topRatedMovies.getData();
    }
    private Call<PinjamanResponsePojo> PinjamanResponsePojoCall() {
        return ServicePojo.getDataPinjaman(
                tgl,
                currentPage,
                PER_PAGE
        );
    }

}
