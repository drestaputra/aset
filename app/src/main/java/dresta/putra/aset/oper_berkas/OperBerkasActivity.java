package dresta.putra.aset.oper_berkas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import dresta.putra.aset.pinjaman.PinjamanPojo;
import dresta.putra.aset.pinjaman.PinjamanResponsePojo;
import dresta.putra.aset.utils.PaginationScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class OperBerkasActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int PER_PAGE=20;
    private int currentPage = PAGE_START;
    private SearchView mSearchView;
    private String query_pencarian="",id_kategori_buku;
    private OperBerkasAdapter adapter;
    private GridLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    private String nama_nasabah,no_nasabah,username;
    private PrefManager prefManager;

    interface APINasabah{
        @FormUrlEncoded
        @POST("api/pinjaman/data_pinjaman")
        Call<PinjamanResponsePojo> getDataNasabah(
                @Field("nama_nasabah") String nama_nasabah,
                @Field("no_nasabah") String no_nasabah,
                @Field("username") String username,
                @Field("pencarian") String pencarian,
                @Field("page") int page,
                @Field("perPage") int perPage
        );
    }
    private APINasabah ServicePojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oper_berkas);
        progressBar = findViewById(R.id.main_progress);
        mSearchView = findViewById(R.id.mSearchView);
        mShimmerViewContainer = findViewById(R.id.shimmer_card_view);
        mShimmerViewContainer.startShimmerAnimation();
        RecyclerView rv = findViewById(R.id.RvBuku);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        id_kategori_nasabah = getIntent().getStringExtra("id_kategori_nasabah");
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
        ImageView IvRiwayat = findViewById(R.id.IvRiwayat);
        IvRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Iriwayat = new Intent(OperBerkasActivity.this,RiwayatOperBerkasActivity.class);
                startActivity(Iriwayat);
                overridePendingTransition(0, 0);
            }
        });

//        ImBFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Call<PinjamanResponsePojo> getBlok = ServicePojo.getDataNasabah(0,99999);
//                getBlok.enqueue(new Callback<PinjamanResponsePojo>() {
//                    @Override
//                    public void onResponse(Call<PinjamanResponsePojo> call, Response<PinjamanResponsePojo> response) {
//                        if(response.body().getStatus()==200){
//                            List<SurveiPojo> semuaBlok = response.body().getData();
//                            List<String> listSpinner = new ArrayList<String>();
//                            listSpinner.add("Semua Survei");
//                            for (int i = 0; i < semuaBlok.size(); i++){
//                                listSpinner.add(semuaBlok.get(i).getNama_survei().toUpperCase());
//                            }
//                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
//                                    android.R.layout.simple_spinner_item, listSpinner);
//                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            spinnerSurvei.setAdapter(adapter);
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<PinjamanResponsePojo> call, Throwable t) {
//                        List<SurveiPojo> surveiPojos = prefManager.getSurveiPojos("",0,9999);
//                        if (surveiPojos!=null){
//                            List<String> listSpinner = new ArrayList<String>();
//                            listSpinner.add("Semua Survei");
//                            for (int i = 0; i < surveiPojos.size(); i++){
//                                listSpinner.add(surveiPojos.get(i).getNama_survei().toUpperCase());
//                            }
//                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
//                                    android.R.layout.simple_spinner_item, listSpinner);
//                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            spinnerSurvei.setAdapter(adapter);
//                        }
//                    }
//                });
//                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            }
//        });
//        spinnerSurvei.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                nama_survei = spinnerSurvei.getSelectedItem().toString();
//                if (spinnerSurvei.getSelectedItemPosition()==0){
//                    nama_survei="";
//                }
//                getKategori();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });
//        Button BtFilter = findViewById(R.id.BtFilter);
//        BtFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nama_survei = spinnerSurvei.getSelectedItem().toString();
//                nama_kategori_nasabah = spinnerKategori.getSelectedItem().toString();
//                if (spinnerKategori.getSelectedItemPosition()==0){
//                    nama_kategori_nasabah="";
//                }
//                if (spinnerSurvei.getSelectedItemPosition()==0){
//                    nama_survei="";
//                }
//                init_pencarian();
//            }
//        });

        //init service and load data
        ServicePojo = RetrofitClientInstance.getRetrofitInstance(OperBerkasActivity.this).create(APINasabah.class);
        loadFirstPage();
        adapter = new OperBerkasAdapter(OperBerkasActivity.this);
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
        PinjamanResponsePojoCall().enqueue(new Callback<PinjamanResponsePojo>() {
            @Override
            public void onResponse(Call<PinjamanResponsePojo> call, Response<PinjamanResponsePojo> response) {

                if (response.body() != null) {
                    if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        adapter.clear();
                        List<PinjamanPojo> results = fetchResults(response);
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
            public void onFailure(@NonNull Call<PinjamanResponsePojo> call, @NonNull Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                adapter.clear();
                Toast.makeText(OperBerkasActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();


            }
        });

    }
    private void loadFirstPage() {
        PinjamanResponsePojoCall().enqueue(new Callback<PinjamanResponsePojo>() {
            @Override
            public void onResponse(Call<PinjamanResponsePojo> call, Response<PinjamanResponsePojo> response) {
                // Got data. Send it to adapter
                if(Objects.requireNonNull(response.body()).getStatus()==200 && response.body().getTotalRecords()!=0) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    List<PinjamanPojo> results = fetchResults(response);
                    Log.d("teslogoperba", response.body().getTotalRecords().toString());
                    Log.d("teslogoperba2", response.body().getData().get(0).getId_pinjaman());
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
                    Toast.makeText(OperBerkasActivity.this, "Belum ada Data untuk saat ini", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<PinjamanResponsePojo> call, Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                adapter.clear();
                Toast.makeText(OperBerkasActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();

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
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                adapter.clear();
                Toast.makeText(OperBerkasActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
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
    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<PinjamanResponsePojo> PinjamanResponsePojoCall() {
        return ServicePojo.getDataNasabah(
                nama_nasabah,
                no_nasabah,
                username,
                query_pencarian,
                currentPage,
                PER_PAGE
        );
    }
}
