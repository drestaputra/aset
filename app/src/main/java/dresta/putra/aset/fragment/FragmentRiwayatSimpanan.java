package dresta.putra.aset.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import java.util.List;

import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.riwayat_simpanan.PaginationAdapterRiwayatSimpanan;
import dresta.putra.aset.riwayat_simpanan.RiwayatSimpananPojo;
import dresta.putra.aset.riwayat_simpanan.RiwayatSimpananPojoResponse;
import dresta.putra.aset.utils.PaginationScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class FragmentRiwayatSimpanan extends Fragment {
    private ProgressBar progressBar;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int PER_PAGE=20;
    private int currentPage = PAGE_START;
    private SearchView mSearchView;
    private String query_pencarian="";
    private PaginationAdapterRiwayatSimpanan adapter;
    private GridLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private PrefManager prefManager;

    public FragmentRiwayatSimpanan() {
        // Required empty public constructor
    }
    interface APIAngsuran{
        @FormUrlEncoded
        @POST("api/riwayat_simpanan/data_riwayat_simpanan")
        Call<RiwayatSimpananPojoResponse> getDataRiwayatAngsuran(
                @Field("pencarian") String pencarian,
                @Field("page") int page,
                @Field("perPage") int perPage
        );
    }
    private APIAngsuran ServicePojo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_riwayat_simpanan, container, false);
        ServicePojo = RetrofitClientInstance.getRetrofitInstance(getActivity().getApplicationContext()).create(APIAngsuran.class);
        progressBar = view.findViewById(R.id.main_progress);
        mSearchView = view.findViewById(R.id.mSearchView);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_card_view);
        mShimmerViewContainer.startShimmerAnimation();
        RecyclerView rv = view.findViewById(R.id.RvData);
        adapter = new PaginationAdapterRiwayatSimpanan(getActivity().getApplicationContext());
        adapter.clear();
        linearLayoutManager = new GridLayoutManager(getContext(),1);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        loadFirstPage();
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
        return view;
    }
    private void init_pencarian(){
        currentPage=0;
        isLastPage = false;
        RiwayatSimpananPojoResponseCall().enqueue(new Callback<RiwayatSimpananPojoResponse>() {
            @Override
            public void onResponse(Call<RiwayatSimpananPojoResponse> call, Response<RiwayatSimpananPojoResponse> response) {

                if (response.body() != null) {
                    Log.d("testotal", String.valueOf(response.body().getTotalRecords()));
                    if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        adapter.clear();
                        List<RiwayatSimpananPojo> results = fetchResults(response);
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
            public void onFailure(@NonNull Call<RiwayatSimpananPojoResponse> call, @NonNull Throwable t) {
                adapter.clear();
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadFirstPage() {
        adapter.clear();
        RiwayatSimpananPojoResponseCall().enqueue(new Callback<RiwayatSimpananPojoResponse>() {
            @Override
            public void onResponse(Call<RiwayatSimpananPojoResponse> call, Response<RiwayatSimpananPojoResponse> response) {
                // Got data. Send it to adapter
                assert response.body() != null;
                if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    List<RiwayatSimpananPojo> results = fetchResults(response);
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
                    Toast.makeText(getActivity().getApplicationContext(), "Belum ada Data untuk saat ini", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<RiwayatSimpananPojoResponse> call, Throwable t) {
                adapter.clear();
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadNextPage() {
        RiwayatSimpananPojoResponseCall().enqueue(new Callback<RiwayatSimpananPojoResponse>() {
            @Override
            public void onResponse(Call<RiwayatSimpananPojoResponse> call, Response<RiwayatSimpananPojoResponse> response) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                adapter.removeLoadingFooter();
                isLoading = false;

                List<RiwayatSimpananPojo> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<RiwayatSimpananPojoResponse> call, Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * @param response extracts List<{@link RiwayatSimpananPojo>} from response
     * @return
     */
    private List<RiwayatSimpananPojo> fetchResults(Response<RiwayatSimpananPojoResponse> response) {
        RiwayatSimpananPojoResponse topRatedMovies = response.body();
        return topRatedMovies.getData();
    }
    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<RiwayatSimpananPojoResponse> RiwayatSimpananPojoResponseCall() {
        return ServicePojo.getDataRiwayatAngsuran(
                query_pencarian,
                currentPage,
                PER_PAGE
        );
    }







}
