package dresta.putra.aset.peta;


import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dresta.putra.aset.R;

public class PaginationAdapterAset extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<PetaPojo> PetaPojos;
    private Context context;

    private boolean isLoadingAdded = false;
    private AdapterFotoMarker adapterFotoMarker;
    public PaginationAdapterAset(Context context) {
        this.context = context;
        PetaPojos = new ArrayList<>();
    }


    public void setPetaPojos(List<PetaPojo> PetaPojos) {
        this.PetaPojos = PetaPojos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new PaginationAdapterAset.LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, @NonNull LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.adapter_marker, parent, false);
        viewHolder = new PaginationAdapterAset.PetaPojoVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final PetaPojo result = PetaPojos.get(position); // PetaPojo
        switch (getItemViewType(position)) {
            case ITEM:
                final PaginationAdapterAset.PetaPojoVH Vh = (PaginationAdapterAset.PetaPojoVH) holder;
                if (result.getFoto_aset() != null) {
                    adapterFotoMarker = new AdapterFotoMarker(result.getFoto_aset(), Objects.requireNonNull(context));
                    Vh.viewPager.setAdapter(adapterFotoMarker);
                }
                if (result.getLatitude() != null && !result.getLatitude().equals("0") && result.getLongitude() != null && !result.getLongitude().equals("0")){
                    Vh.BtnRute.setVisibility(View.VISIBLE);
                    Vh.BtnStreetView.setVisibility(View.VISIBLE);
                    Vh.BtnMap.setVisibility(View.VISIBLE);
                }else{
                    Vh.BtnRute.setVisibility(View.GONE);
                    Vh.BtnStreetView.setVisibility(View.GONE);
                    Vh.BtnMap.setVisibility(View.GONE);
                }
                Vh.title.setText(result.getNama_aset());
                Vh.desc.setText(Html.fromHtml(result.getKeterangan()));
                Vh.BtnDetail.setOnClickListener(v -> {
                    Intent Idetail = new Intent(context.getApplicationContext(), DetailAsetActivity.class);
                    Idetail.putExtra("id_aset",result.getId_aset());
                    Idetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(Idetail);
                });
                Vh.BtnMap.setOnClickListener(v -> {
                    Intent Idetail = new Intent(context.getApplicationContext(), DetailPetaActivity.class);
                    Idetail.putExtra("id_aset",result.getId_aset());
                    Idetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(Idetail);
                });
                Vh.BtnStreetView.setOnClickListener( v -> {
                    Intent Idetail = new Intent(context.getApplicationContext(), StreetViewActivity.class);
                    Idetail.putExtra("id_aset",result.getId_aset());
                    Idetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(Idetail);
                });
                Vh.BtnRute.setOnClickListener( v -> {
                    Intent Idetail = new Intent(context.getApplicationContext(), RuteActivity.class);
                    Idetail.putExtra("id_aset",result.getId_aset());
                    Idetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(Idetail);
                });

                //        slider
                Vh.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(final int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });


                break;
            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return PetaPojos == null ? 0 : PetaPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == PetaPojos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(PetaPojo r) {
        PetaPojos.add(r);
        notifyItemInserted(PetaPojos.size() - 1);
    }

    public void addAll(@NonNull List<PetaPojo> moveResults) {
        for (PetaPojo result : moveResults) {
            add(result);
        }
    }

    public void remove(PetaPojo r) {
        int position = PetaPojos.indexOf(r);
        if (position > -1) {
            PetaPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new PetaPojo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = PetaPojos.size() - 1;
        PetaPojo result = getItem(position);

        if (result != null) {
            PetaPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public PetaPojo getItem(int position) {
        return PetaPojos.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class PetaPojoVH extends RecyclerView.ViewHolder {
        private ViewPager viewPager;
        private Button BtnDetail;
        private ImageView imageView,BtnMap, BtnStreetView, BtnRute;
        private TextView title,desc;

        public PetaPojoVH(@NonNull View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.viewPager);
            BtnDetail = itemView.findViewById(R.id.BtnDetail);
            BtnMap = itemView.findViewById(R.id.BtnMap);
            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            BtnStreetView = itemView.findViewById(R.id.BtnStreetView);
            BtnRute = itemView.findViewById(R.id.BtnRute);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }


}


