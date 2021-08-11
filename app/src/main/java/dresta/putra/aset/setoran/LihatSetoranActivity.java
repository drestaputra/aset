package dresta.putra.aset.setoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.angsuran.PaginationAdapterRiwayatAngsuran;
import dresta.putra.aset.angsuran.RiwayatPinjamanPojo;
import dresta.putra.aset.angsuran.RiwayatPinjamanPojoResponse;
import dresta.putra.aset.utils.PaginationScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class LihatSetoranActivity extends AppCompatActivity {
    private String pencarian = "";
    private ProgressBar progressBar;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int PER_PAGE=20;
    private int currentPage = PAGE_START;
    private SearchView mSearchView;
    private String query_pencarian="";
    private PaginationAdapterRiwayatAngsuran adapter;
    private GridLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private PrefManager prefManager;
    private TextView TxvTotalPengangsur,TxvTotalAngsuran,TxvTerbesar, TxvTerkecil;
    interface APILihatSetoranActivity{
        @FormUrlEncoded
        @POST("api/riwayat_pinjaman/pinjaman_hari_ini")
        Call<RiwayatPinjamanPojoResponse> getPinjamanHariIni(
                @Field("pencarian") String pencarian,
                @Field("page") int page,
                @Field("perPage") int perPage
        );
        @FormUrlEncoded
        @POST("api/riwayat_pinjaman/summary_pinjaman_hari_ini")
        Call<SummaryPinjamanPojoResponse> getSummaryPinjamanHariIni(
                @Field("pencarian") String pencarian
        );
    }
    private APILihatSetoranActivity ServicePojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_setoran);
        ServicePojo = RetrofitClientInstance.getRetrofitInstance(LihatSetoranActivity.this).create(APILihatSetoranActivity.class);
        progressBar = findViewById(R.id.main_progress);
        mSearchView = findViewById(R.id.mSearchView);
        mShimmerViewContainer = findViewById(R.id.shimmer_card_view);
        mShimmerViewContainer.startShimmerAnimation();
        RecyclerView rv = findViewById(R.id.RvData);
        TxvTotalPengangsur = findViewById(R.id.TxvTotalPengangsur);
        TxvTotalAngsuran = findViewById(R.id.TxvTotalAngsuran);
        TxvTerbesar = findViewById(R.id.TxvTerbesar);
        TxvTerkecil = findViewById(R.id.TxvTerkecil);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new PaginationAdapterRiwayatAngsuran(LihatSetoranActivity.this);
        adapter.clear();
        linearLayoutManager = new GridLayoutManager(LihatSetoranActivity.this,1);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);
        loadFirstPage();

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
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                query_pencarian=query;
//                if (!query.equals("")){
                init_pencarian();
//                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {

                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                query_pencarian=query;
//                if (!query.equals("")){
                init_pencarian();
//                }
                return false;
            }
        });
    }
    private void init_pencarian(){
        currentPage=0;
        isLastPage = false;
        RiwayatPinjamanPojoResponseCall().enqueue(new Callback<RiwayatPinjamanPojoResponse>() {
            @Override
            public void onResponse(Call<RiwayatPinjamanPojoResponse> call, Response<RiwayatPinjamanPojoResponse> response) {

                if (response.body() != null) {
                    if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        adapter.clear();
                        List<RiwayatPinjamanPojo> results = fetchResults(response);
                        adapter.addAll(results);
                        TOTAL_PAGES=response.body().getTotalPage();
                        if (currentPage <= TOTAL_PAGES-1) {
                            adapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                        }
                    }else{
                        adapter.clear();
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RiwayatPinjamanPojoResponse> call, @NonNull Throwable t) {
                adapter.clear();
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LihatSetoranActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadFirstPage() {
        adapter.clear();
        RiwayatPinjamanPojoResponseCall().enqueue(new Callback<RiwayatPinjamanPojoResponse>() {
            @Override
            public void onResponse(Call<RiwayatPinjamanPojoResponse> call, Response<RiwayatPinjamanPojoResponse> response) {
                // Got data. Send it to adapter
                assert response.body() != null;
                if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    List<RiwayatPinjamanPojo> results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
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
                    Toast.makeText(LihatSetoranActivity.this, "Belum ada Data untuk saat ini", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<RiwayatPinjamanPojoResponse> call, Throwable t) {
                adapter.clear();
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LihatSetoranActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadNextPage() {
        RiwayatPinjamanPojoResponseCall().enqueue(new Callback<RiwayatPinjamanPojoResponse>() {
            @Override
            public void onResponse(Call<RiwayatPinjamanPojoResponse> call, Response<RiwayatPinjamanPojoResponse> response) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                adapter.removeLoadingFooter();
                isLoading = false;

                List<RiwayatPinjamanPojo> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<RiwayatPinjamanPojoResponse> call, Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LihatSetoranActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * @param response extracts List<{@link RiwayatPinjamanPojo>} from response
     * @return
     */
    private List<RiwayatPinjamanPojo> fetchResults(Response<RiwayatPinjamanPojoResponse> response) {
        RiwayatPinjamanPojoResponse topRatedMovies = response.body();
        return topRatedMovies != null ? topRatedMovies.getData() : null;
    }
    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<RiwayatPinjamanPojoResponse> RiwayatPinjamanPojoResponseCall() {
        initSummary();
        return ServicePojo.getPinjamanHariIni(
                query_pencarian,
                currentPage,
                PER_PAGE
        );
    }
    private void initSummary(){
        Call<SummaryPinjamanPojoResponse> summaryPinjamanPojoResponseCall = ServicePojo.getSummaryPinjamanHariIni(query_pencarian);
        summaryPinjamanPojoResponseCall.enqueue(new Callback<SummaryPinjamanPojoResponse>() {
            @Override
            public void onResponse(Call<SummaryPinjamanPojoResponse> call, Response<SummaryPinjamanPojoResponse> response) {
                if (response.body()!=null){
                    if (response.body().getStatus()==200){
                        SummaryPinjamanPojo result = response.body().getData();
                        TxvTotalAngsuran.setText(result.getTotal_angsuran());
                        TxvTotalPengangsur.setText(result.getJumlah_pengangsur());
                        TxvTerbesar.setText(result.getTerbesar());
                        TxvTerkecil.setText(result.getTerkecil());
                    }
                }
            }

            @Override
            public void onFailure(Call<SummaryPinjamanPojoResponse> call, Throwable t) {

            }
        });

    }
    class SummaryPinjamanPojoResponse{
        @SerializedName("status")
        Integer status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        SummaryPinjamanPojo data;

        public SummaryPinjamanPojoResponse(Integer status, String msg, SummaryPinjamanPojo data) {
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

        public SummaryPinjamanPojo getData() {
            return data;
        }

        public void setData(SummaryPinjamanPojo data) {
            this.data = data;
        }
    }
    class SummaryPinjamanPojo{
        @SerializedName("jumlah_pengangsur")
        String jumlah_pengangsur;
        @SerializedName("total_angsuran")
        String total_angsuran;
        @SerializedName("terbesar")
        String terbesar;
        @SerializedName("terkecil")
        String terkecil;

        public SummaryPinjamanPojo(String jumlah_pengangsur, String total_angsuran, String terbesar, String terkecil) {
            this.jumlah_pengangsur = jumlah_pengangsur;
            this.total_angsuran = total_angsuran;
            this.terbesar = terbesar;
            this.terkecil = terkecil;
        }

        public SummaryPinjamanPojo() {
        }

        public String getJumlah_pengangsur() {
            return jumlah_pengangsur;
        }

        public void setJumlah_pengangsur(String jumlah_pengangsur) {
            this.jumlah_pengangsur = jumlah_pengangsur;
        }

        public String getTotal_angsuran() {
            return total_angsuran;
        }

        public void setTotal_angsuran(String total_angsuran) {
            this.total_angsuran = total_angsuran;
        }

        public String getTerbesar() {
            return terbesar;
        }

        public void setTerbesar(String terbesar) {
            this.terbesar = terbesar;
        }

        public String getTerkecil() {
            return terkecil;
        }

        public void setTerkecil(String terkecil) {
            this.terkecil = terkecil;
        }
    }
}
