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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.peta.PaginationAdapterAset;
import dresta.putra.aset.peta.PetaPojo;
import dresta.putra.aset.peta.PetaResponsePojo;
import dresta.putra.aset.utils.PaginationScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class FragmentAset extends Fragment {
    private ProgressBar progressBar;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int TOTAL_RECORDS = 0;
    private int currentPage = PAGE_START;
    private SearchView mSearchView;
    private String query_pencarian="";
    private PaginationAdapterAset adapter;
    private GridLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private List<PetaPojo> results;
    private PrefManager prefManager;

    public FragmentAset() {
        // Required empty public constructor
    }
    interface APIAset{
        @FormUrlEncoded
        @POST("api/aset/data_aset")
        Call<PetaResponsePojo> getDataAset(
                @Field("pencarian") String pencarian,
                @Field("page") int page,
                @Field("perPage") int perPage
        );
    }
    private APIAset ServicePojo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_aset, container, false);
        prefManager = new PrefManager(getContext());

        results = new ArrayList<>();
        linearLayoutManager = new GridLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext(),1);
        ServicePojo = RetrofitClientInstance.getRetrofitInstance(getActivity().getApplicationContext()).create(APIAset.class);
        progressBar = view.findViewById(R.id.main_progress);
        mSearchView = view.findViewById(R.id.mSearchView);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_card_view);
        mShimmerViewContainer.startShimmerAnimation();
        RecyclerView rv = view.findViewById(R.id.RvData);
        adapter = new PaginationAdapterAset(getActivity().getApplicationContext());
        adapter.clear();
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);
        loadFirstPage();

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;
                currentPage += 1;
                new Handler().postDelayed(() -> loadNextPage(), 1000);
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

        return view;
    }
    private  void searchListener(){
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
//                bug ketika msearch diisi kemudian kosong, data menjadi kosong, sebenernya script dibawah untuk mengantisipasi double request ketika oncreate
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
        PetaResponsePojoCall().enqueue(new Callback<PetaResponsePojo>() {
            @Override
            public void onResponse(@NonNull Call<PetaResponsePojo> call, @NonNull Response<PetaResponsePojo> response) {

                if (response.body() != null) {
                    if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        adapter.clear();
                        results = fetchResults(response);
                        if (results != null) {
                            adapter.addAll(results);
                        }
                        TOTAL_PAGES=response.body().getTotalPage();
                        TOTAL_RECORDS=response.body().getTotalRecords();
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
               adapter.clear();
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadFirstPage() {
        adapter.clear();
        PetaResponsePojoCall().enqueue(new Callback<PetaResponsePojo>() {
            @Override
            public void onResponse(@NonNull Call<PetaResponsePojo> call, @NonNull Response<PetaResponsePojo> response) {
                // Got data. Send it to adapter
                assert response.body() != null;
                if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);
                    TOTAL_PAGES=response.body().getTotalPage();
                    TOTAL_RECORDS=response.body().getTotalRecords();
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
                    Toast.makeText(getContext(), "Belum ada Data untuk saat ini", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
                searchListener();
            }

            @Override
            public void onFailure(@NonNull Call<PetaResponsePojo> call, @NonNull Throwable t) {
                adapter.clear();
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Belum ada data", Toast.LENGTH_SHORT).show();
                Log.d("tesdebug", t.toString());
            }
        });

    }
    private void loadNextPage() {
        PetaResponsePojoCall().enqueue(new Callback<PetaResponsePojo>() {
            @Override
            public void onResponse(@NonNull Call<PetaResponsePojo> call, @NonNull Response<PetaResponsePojo> response) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                adapter.removeLoadingFooter();
                isLoading = false;

                results = fetchResults(response);
                adapter.addAll(Objects.requireNonNull(results));
                TOTAL_RECORDS = response.body() != null ? response.body().getTotalRecords() : 0;
                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(@NonNull Call<PetaResponsePojo> call, @NonNull Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * @param response extracts List<{@link PetaPojo>} from response
     * @return null
     */
    private List<PetaPojo> fetchResults(Response<PetaResponsePojo> response) {
        PetaResponsePojo topRatedMovies = response.body();
        return topRatedMovies != null ? topRatedMovies.getData() : null;
    }
    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<PetaResponsePojo> PetaResponsePojoCall() {
        Call<PetaResponsePojo> petaResponsePojoCall ;
        if (prefManager.getKontakPojo() != null && prefManager.getKontakPojo().getApp_is_aset_show()!=null && prefManager.getKontakPojo().getApp_is_aset_show().equals("0")){
            Toast.makeText(getContext(), "Maaf saat ini halaman data aset sedang dibatasi aksesnya.", Toast.LENGTH_SHORT).show();
            petaResponsePojoCall =  ServicePojo.getDataAset(
                    "",
                    0,
                    0
            );
        }else{
            int PER_PAGE = 20;
            petaResponsePojoCall = ServicePojo.getDataAset(
                    query_pencarian,
                    currentPage,
                    PER_PAGE
            );
        }
        return petaResponsePojoCall;

    }








}
