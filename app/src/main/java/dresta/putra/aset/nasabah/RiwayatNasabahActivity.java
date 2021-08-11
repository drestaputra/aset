package dresta.putra.aset.nasabah;

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

import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;

import dresta.putra.aset.utils.PaginationScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class RiwayatNasabahActivity extends AppCompatActivity {
    private  String id_nasabah;
    private ProgressBar progressBar;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int PER_PAGE=20;
    private int currentPage = PAGE_START;
    private SearchView mSearchView;
    private String query_pencarian="";
    private PaginationAdapterRiwayatNasabah adapter;
    private GridLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;

    interface APIRiwayatNasabah{
        @FormUrlEncoded
        @POST("api/nasabah/riwayat_nasabah")
        Call<RiwayatNasabahResponsePojo> getRiwayat(
                @Field("id_nasabah") String id_nasabah,
                @Field("pencarian") String pencarian,
                @Field("page") int page,
                @Field("perPage") int perPage
        );
    }
    private  APIRiwayatNasabah apiRiwayatNasabah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_nasabah);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id_nasabah = getIntent().getStringExtra("id_nasabah");
        apiRiwayatNasabah  = RetrofitClientInstance.getRetrofitInstance(RiwayatNasabahActivity.this).create(APIRiwayatNasabah.class);
        progressBar = findViewById(R.id.main_progress);
        mSearchView = findViewById(R.id.mSearchView);
        mShimmerViewContainer = findViewById(R.id.shimmer_card_view);
        mShimmerViewContainer.startShimmerAnimation();
        RecyclerView rv = findViewById(R.id.RvData);
        adapter = new PaginationAdapterRiwayatNasabah(RiwayatNasabahActivity.this);
        adapter.clear();
        linearLayoutManager = new GridLayoutManager(RiwayatNasabahActivity.this,1);
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
        riwayatNasabahResponsePojoCall().enqueue(new Callback<RiwayatNasabahResponsePojo>() {
            @Override
            public void onResponse(Call<RiwayatNasabahResponsePojo> call, Response<RiwayatNasabahResponsePojo> response) {

                if (response.body() != null) {
                    if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        adapter.clear();
                        List<RiwayatNasabahPojo> results = fetchResults(response);
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
            public void onFailure(@NonNull Call<RiwayatNasabahResponsePojo> call, @NonNull Throwable t) {
                adapter.clear();
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RiwayatNasabahActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadFirstPage() {
        adapter.clear();
        riwayatNasabahResponsePojoCall().enqueue(new Callback<RiwayatNasabahResponsePojo>() {
            @Override
            public void onResponse(Call<RiwayatNasabahResponsePojo> call, Response<RiwayatNasabahResponsePojo> response) {
                // Got data. Send it to adapter
                assert response.body() != null;
                if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    List<RiwayatNasabahPojo> results = fetchResults(response);
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
                    Toast.makeText(RiwayatNasabahActivity.this, "Belum ada Data untuk saat ini", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<RiwayatNasabahResponsePojo> call, Throwable t) {
                adapter.clear();
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RiwayatNasabahActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadNextPage() {
        riwayatNasabahResponsePojoCall().enqueue(new Callback<RiwayatNasabahResponsePojo>() {
            @Override
            public void onResponse(Call<RiwayatNasabahResponsePojo> call, Response<RiwayatNasabahResponsePojo> response) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                adapter.removeLoadingFooter();
                isLoading = false;

                List<RiwayatNasabahPojo> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<RiwayatNasabahResponsePojo> call, Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RiwayatNasabahActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * @param response extracts List<{@link RiwayatNasabahPojo >} from response
     * @return
     */
    private List<RiwayatNasabahPojo> fetchResults(Response<RiwayatNasabahResponsePojo> response) {
        RiwayatNasabahResponsePojo responsePojo = response.body();
        return responsePojo != null ? responsePojo.getData() : null;
    }
    /**
     * Performs a Retrofit call
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<RiwayatNasabahResponsePojo> riwayatNasabahResponsePojoCall() {
        return apiRiwayatNasabah.getRiwayat(
                id_nasabah,
                query_pencarian,
                currentPage,
                PER_PAGE
        );
    }
}