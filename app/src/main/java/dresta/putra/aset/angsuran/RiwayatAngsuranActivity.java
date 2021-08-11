package dresta.putra.aset.angsuran;

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
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;
import java.util.Objects;

import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.utils.PaginationScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class RiwayatAngsuranActivity extends AppCompatActivity {
    private String pencarian = "";
    private ProgressBar progressBar;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int PER_PAGE=20;
    private int currentPage = PAGE_START;
    private SearchView mSearchView;
    private String query_pencarian="",id_pinjaman;
    private PaginationAdapterRiwayatAngsuran adapter;
    private GridLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private PrefManager prefManager;
    interface APIRiwayatAngsuranActivity{
        @FormUrlEncoded
        @POST("api/riwayat_pinjaman/data_riwayat_pinjaman")
        Call<RiwayatPinjamanPojoResponse> getPinjamanHariIni(
                @Field("pencarian") String pencarian,
                @Field("page") int page,
                @Field("perPage") int perPage
        );
    }
    private APIRiwayatAngsuranActivity ServicePojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_angsuran);
        ServicePojo = RetrofitClientInstance.getRetrofitInstance(RiwayatAngsuranActivity.this).create(APIRiwayatAngsuranActivity.class);
        progressBar = findViewById(R.id.main_progress);
        mSearchView = findViewById(R.id.mSearchView);
        mShimmerViewContainer = findViewById(R.id.shimmer_card_view);
        mShimmerViewContainer.startShimmerAnimation();
        RecyclerView rv = findViewById(R.id.RvData);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent().getExtras() != null){
            id_pinjaman = getIntent().getStringExtra("id_pinjaman");
            if (id_pinjaman!=null){
                query_pencarian = id_pinjaman;
                mSearchView.setQuery(id_pinjaman,true);
                mSearchView.setIconifiedByDefault(false);
            }
        }

        adapter = new PaginationAdapterRiwayatAngsuran(RiwayatAngsuranActivity.this);
        adapter.clear();
        linearLayoutManager = new GridLayoutManager(RiwayatAngsuranActivity.this,1);
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
                Toast.makeText(RiwayatAngsuranActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RiwayatAngsuranActivity.this, "Belum ada Data untuk saat ini", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<RiwayatPinjamanPojoResponse> call, Throwable t) {
                adapter.clear();
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RiwayatAngsuranActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RiwayatAngsuranActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
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
        return ServicePojo.getPinjamanHariIni(
                query_pencarian,
                currentPage,
                PER_PAGE
        );
    }
}
