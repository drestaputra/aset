package dresta.putra.aset.berita;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

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

public class BeritaActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int TOTAL_RECORDS = 0;
    private int PER_PAGE=20;
    private int currentPage = PAGE_START;
    private SearchView mSearchView;
    private String query_pencarian="",id_kategori_buku;
    private PaginationAdapterBerita adapter;
    private GridLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    private PrefManager prefManager;

    interface APIBerita{
        @FormUrlEncoded
        @POST("api/berita/data_berita")
        Call<BeritaResponsePojo> getDataBerita(
                @Field("pencarian") String pencarian,
                @Field("page") int page,
                @Field("perPage") int perPage
        );
    }
    private APIBerita ServicePojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita);
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
        prefManager = new PrefManager(this);

//        filter tambahan
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        ImageButton ImBFilter = findViewById(R.id.ImBFilter);
        Button BtnClose = findViewById(R.id.BtnClose);
        BtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        
        ServicePojo = RetrofitClientInstance.getRetrofitInstance(BeritaActivity.this).create(APIBerita.class);
        loadFirstPage();
        adapter = new PaginationAdapterBerita(BeritaActivity.this);
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
            public int getTotalRecords() {
                return TOTAL_RECORDS;
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
                init_pencarian();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {

                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                query_pencarian=query;
                init_pencarian();
                return false;
            }
        });
    }
    private void init_pencarian(){
        currentPage=0;
        isLastPage = false;
        BeritaResponsePojoCall().enqueue(new Callback<BeritaResponsePojo>() {
            @Override
            public void onResponse(Call<BeritaResponsePojo> call, Response<BeritaResponsePojo> response) {

                if (response.body() != null) {
                    if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        adapter.clear();
                        List<BeritaPojo> results = fetchResults(response);
                        adapter.addAll(results);
                        TOTAL_PAGES=response.body().getTotalPage();
                        TOTAL_RECORDS = response.body().getTotalRecords();
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
            public void onFailure(@NonNull Call<BeritaResponsePojo> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    adapter.clear();
                    Toast.makeText(BeritaActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadFirstPage() {
        BeritaResponsePojoCall().enqueue(new Callback<BeritaResponsePojo>() {
            @Override
            public void onResponse(Call<BeritaResponsePojo> call, Response<BeritaResponsePojo> response) {
                // Got data. Send it to adapter
                assert response.body() != null;
                if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    List<BeritaPojo> results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);
                    TOTAL_PAGES=response.body().getTotalPage();
                    TOTAL_RECORDS = response.body().getTotalRecords();
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
                    Toast.makeText(BeritaActivity.this, "Belum ada Data untuk saat ini", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<BeritaResponsePojo> call, Throwable t) {
                t.printStackTrace();
                adapter.clear();
                Toast.makeText(BeritaActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadNextPage() {
        BeritaResponsePojoCall().enqueue(new Callback<BeritaResponsePojo>() {
            @Override
            public void onResponse(Call<BeritaResponsePojo> call, Response<BeritaResponsePojo> response) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                adapter.removeLoadingFooter();
                isLoading = false;

                List<BeritaPojo> results = fetchResults(response);
                adapter.addAll(results);
                TOTAL_RECORDS = response.body().getTotalRecords();
                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<BeritaResponsePojo> call, Throwable t) {
                t.printStackTrace();
                adapter.clear();
                Toast.makeText(BeritaActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @param response extracts List<{@link BeritaPojo >} from response
     * @return
     */
    private List<BeritaPojo> fetchResults(Response<BeritaResponsePojo> response) {
        BeritaResponsePojo topRatedMovies = response.body();
        return topRatedMovies.getData();
    }
    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<BeritaResponsePojo> BeritaResponsePojoCall() {

        return ServicePojo.getDataBerita(
                query_pencarian,
                currentPage,
                PER_PAGE
        );
    }
}
