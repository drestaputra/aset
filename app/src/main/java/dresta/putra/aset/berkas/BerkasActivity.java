package dresta.putra.aset.berkas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.utils.PaginationScrollListener;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static android.Manifest.permission_group.STORAGE;

public class BerkasActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private ProgressBar progressBar;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int PER_PAGE = 20;
    private int currentPage = PAGE_START;
    private SearchView mSearchView;
    private String query_pencarian = "", id_kategori_buku;
    private PaginationAdapterBerkas adapter;
    private GridLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    private PrefManager prefManager;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final int REQUEST_CODE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    interface APIBerkas {
        @FormUrlEncoded
        @POST("api/berkas/data_berkas")
        Call<BerkasPojoResponse> getDataBerkas(
                @Field("pencarian") String pencarian,
                @Field("page") int page,
                @Field("perPage") int perPage
        );
    }

    private APIBerkas ServicePojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berkas);
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

        ServicePojo = RetrofitClientInstance.getRetrofitInstance(BerkasActivity.this).create(APIBerkas.class);
        loadFirstPage();
        adapter = new PaginationAdapterBerkas(BerkasActivity.this);
        linearLayoutManager = new GridLayoutManager(this, 1);
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
                query_pencarian = query;
                if (query != null) {
                    init_pencarian();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                query_pencarian = query;
                init_pencarian();
                return false;
            }
        });
        if (EasyPermissions.hasPermissions(BerkasActivity.this, PERMISSIONS_STORAGE)) {

        } else {
            //If permission has not been granted, request
            EasyPermissions.requestPermissions(BerkasActivity.this, "This app needs access to your file storage", REQUEST_CODE, PERMISSIONS_STORAGE);
        }
    }
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        Log.d("tesdebug", "onRequestPermissionsResult: ");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
            } else {
//                execute(REQUEST_CODE_PERMISSIONS);

//                finish();
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(requestCode == REQUEST_CODE){
            Toast.makeText(BerkasActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(requestCode == REQUEST_CODE){
            Toast.makeText(BerkasActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void init_pencarian() {
        currentPage = 0;
        isLastPage = false;
        BerkasPojoResponseCall().enqueue(new Callback<BerkasPojoResponse>() {
            @Override
            public void onResponse(Call<BerkasPojoResponse> call, Response<BerkasPojoResponse> response) {

                if (response.body() != null) {
                    if (response.body().getStatus() == 200 && response.body().getTotalRecords() != 0) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        adapter.clear();
                        List<BerkasPojo> results = fetchResults(response);
                        adapter.addAll(results);
                        TOTAL_PAGES = response.body().getTotalPage();
                        if (currentPage <= TOTAL_PAGES - 1) {
                            adapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                        }
                    } else {
                        adapter.clear();
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BerkasPojoResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                adapter.clear();
                Toast.makeText(BerkasActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadFirstPage() {
        BerkasPojoResponseCall().enqueue(new Callback<BerkasPojoResponse>() {
            @Override
            public void onResponse(Call<BerkasPojoResponse> call, Response<BerkasPojoResponse> response) {
                // Got data. Send it to adapter
                assert response.body() != null;
                if (response.body().getStatus() == 200 && response.body().getTotalRecords() != 0) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    List<BerkasPojo> results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
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
                    Toast.makeText(BerkasActivity.this, "Belum ada Data untuk saat ini", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<BerkasPojoResponse> call, Throwable t) {
                t.printStackTrace();
                adapter.clear();
                Toast.makeText(BerkasActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadNextPage() {
        BerkasPojoResponseCall().enqueue(new Callback<BerkasPojoResponse>() {
            @Override
            public void onResponse(Call<BerkasPojoResponse> call, Response<BerkasPojoResponse> response) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                adapter.removeLoadingFooter();
                isLoading = false;

                List<BerkasPojo> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<BerkasPojoResponse> call, Throwable t) {
                t.printStackTrace();
                adapter.clear();
                Toast.makeText(BerkasActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @param response extracts List<{@link BerkasPojo >} from response
     * @return
     */
    private List<BerkasPojo> fetchResults(Response<BerkasPojoResponse> response) {
        BerkasPojoResponse topRatedMovies = response.body();
        return topRatedMovies.getData();
    }

    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<BerkasPojoResponse> BerkasPojoResponseCall() {
        return ServicePojo.getDataBerkas(
                query_pencarian,
                currentPage,
                PER_PAGE
        );
    }


}
