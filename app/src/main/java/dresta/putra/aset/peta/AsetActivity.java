package dresta.putra.aset.peta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import dresta.putra.aset.CariActivity;
import dresta.putra.aset.R;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class AsetActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int PER_PAGE=20;
    private int currentPage = PAGE_START;
    private SearchView mSearchView;
    private String query_pencarian="",id_kategori_buku;
    private PaginationAdapterAset adapter;
    private GridLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    private String nama_nasabah,no_nasabah,username;
    private PrefManager prefManager;
    private ImageView IvTambahPinjaman;
    private ImageButton ImBFilter;
    private SwipeRefreshLayout swipe;
    private String nama_aset = "",kode_barang = "", tahun_perolehan = "", alamat = "", nomor_sertifikat = "", jenis_hak = "", luas_tanah = "";
    private Bundle bundle;
    interface APIAset{
        @FormUrlEncoded
        @POST("api/aset/data_aset")
        Call<PetaResponsePojo> getDataPinjaman(
                @Field("nama_aset") String nama_aset,
                @Field("kode_barang") String kode_barang,
                @Field("tahun_perolehan") String tahun_perolehan,
                @Field("alamat") String alamat,
                @Field("nomor_sertifikat") String nomor_sertifikat,
                @Field("jenis_hak") String jenis_hak,
                @Field("luas_tanah") String luas_tanah,
                @Field("pencarian") String pencarian,
                @Field("page") int page,
                @Field("perPage") int perPage
        );
    }
    private APIAset ServicePojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aset);
        prefManager = new PrefManager(this);
        if (prefManager.getKontakPojo() != null && prefManager.getKontakPojo().getApp_is_aset_show()!=null && prefManager.getKontakPojo().getApp_is_aset_show().equals("0")){
            Toast.makeText(this, "Maaf saat ini halaman data aset sedang dibatasi aksesnya.", Toast.LENGTH_SHORT).show();
            finish();
        }
        progressBar = findViewById(R.id.main_progress);
        mSearchView = findViewById(R.id.mSearchView);
        swipe = findViewById(R.id.swipe);
        swipe.setOnRefreshListener(() -> {
            adapter.clear();init_pencarian();
            swipe.setRefreshing(false);
        });


        bundle = getIntent().getExtras();
//        private String nama_aset,kode_barang, tahun_perolehan, alamat, nomor_sertifikat;
        if (bundle != null){
            nama_aset = String.valueOf(bundle.getString("nama_aset"));
            kode_barang = String.valueOf(bundle.getString("kode_barang"));
            tahun_perolehan = String.valueOf(bundle.getString("tahun_perolehan"));
            alamat = String.valueOf(bundle.getString("alamat"));
            nomor_sertifikat = String.valueOf(bundle.getString("nomor_sertifikat"));
            jenis_hak = String.valueOf(bundle.getString("jenis_hak"));
            luas_tanah = String.valueOf(bundle.getString("luas_tanah"));
        }
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
        ImBFilter = findViewById(R.id.ImBFilter);
        ImBFilter.setOnClickListener(v -> {
            Bundle bundleCari = new Bundle();
            bundleCari.putString("nama_aset", (nama_aset != null) ? nama_aset : "");
            bundleCari.putString("kode_barang", (kode_barang != null) ? kode_barang : "");
            bundleCari.putString("tahun_perolehan", (tahun_perolehan != null) ? tahun_perolehan : "");
            bundleCari.putString("alamat", (alamat != null) ? alamat : "");
            bundleCari.putString("nomor_sertifikat", (nomor_sertifikat != null) ? nomor_sertifikat : "");
            bundleCari.putString("jenis_hak", (jenis_hak != null) ? jenis_hak : "");
            bundleCari.putString("luas_tanah", (luas_tanah != null) ? luas_tanah : "");
            Intent intent = new Intent(AsetActivity.this, CariActivity.class);
            intent.putExtras(bundleCari);
            finish();
            startActivity(intent);
        });

        ServicePojo = RetrofitClientInstance.getRetrofitInstance(AsetActivity.this).create(AsetActivity.APIAset.class);
        loadFirstPage();
        adapter = new PaginationAdapterAset(AsetActivity.this);
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
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                query_pencarian=query;
                if (!query.equals("")){
                    init_pencarian();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {

                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                query_pencarian=query;
                if (!query.equals("")){
                     init_pencarian();
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init_pencarian(){
        currentPage=0;
        isLastPage = false;
        PetaResponsePojoCall().enqueue(new Callback<PetaResponsePojo>() {
            @Override
            public void onResponse(Call<PetaResponsePojo> call, Response<PetaResponsePojo> response) {

                if (response.body() != null) {
                    if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        adapter.clear();
                        List<PetaPojo> results = fetchResults(response);
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
            public void onFailure(@NonNull Call<PetaResponsePojo> call, @NonNull Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                adapter.clear();
                Toast.makeText(AsetActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();


            }
        });

    }
    private void loadFirstPage() {
        PetaResponsePojoCall().enqueue(new Callback<PetaResponsePojo>() {
            @Override
            public void onResponse(Call<PetaResponsePojo> call, Response<PetaResponsePojo> response) {
                // Got data. Send it to adapter
                if(Objects.requireNonNull(response.body()).getStatus()==200 && response.body().getTotalRecords()!=0) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    List<PetaPojo> results = fetchResults(response);
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
                    Toast.makeText(AsetActivity.this, "Belum ada Data untuk saat ini", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<PetaResponsePojo> call, Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                adapter.clear();
                Toast.makeText(AsetActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void loadNextPage() {
        PetaResponsePojoCall().enqueue(new Callback<PetaResponsePojo>() {
            @Override
            public void onResponse(Call<PetaResponsePojo> call, Response<PetaResponsePojo> response) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                adapter.removeLoadingFooter();
                isLoading = false;

                List<PetaPojo> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<PetaResponsePojo> call, Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                adapter.clear();
                Toast.makeText(AsetActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @param response extracts List<{@link PetaPojo>} from response
     * @return
     */
    private List<PetaPojo> fetchResults(Response<PetaResponsePojo> response) {
        PetaResponsePojo topRatedMovies = response.body();
        return topRatedMovies.getData();
    }
    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<PetaResponsePojo> PetaResponsePojoCall() {
        return ServicePojo.getDataPinjaman(
                nama_aset,kode_barang,tahun_perolehan,alamat,nomor_sertifikat,jenis_hak, luas_tanah,
                query_pencarian,
                currentPage,
                PER_PAGE
        );
    }
}
