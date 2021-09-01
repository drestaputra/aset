package dresta.putra.aset.berita;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dresta.putra.aset.R;

public class PaginationAdapterBerita extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<BeritaPojo> BeritaPojos;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapterBerita(Context context) {
        this.context = context;
        BeritaPojos = new ArrayList<>();
    }


    public void setBeritaPojos(List<BeritaPojo> BeritaPojos) {
        this.BeritaPojos = BeritaPojos;
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
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, @NonNull LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.adapter_berita, parent, false);
        viewHolder = new BeritaPojoVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final BeritaPojo result = BeritaPojos.get(position); // BeritaPojo
        switch (getItemViewType(position)) {
            case ITEM:
                final BeritaPojoVH Vh = (BeritaPojoVH) holder;
//                private TextView TxvJudulBerita,TxvDeskripsiBerita,TxvTglBerita;
//                private ImageView IvBerita;
                Vh.TxvJudulBerita.setText(result.getJudul_berita());
                Vh.TxvTglBerita.setText(result.getTgl_berita());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Vh.TxvDeskripsiBerita.setText(HtmlCompat.fromHtml(result.getDeskripsi_berita(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    Vh.TxvDeskripsiBerita.setText(Html.fromHtml(result.getDeskripsi_berita()));
                }
                if (result.getFoto_berita() != null && result.getFoto_berita().length() > 0) {
                    Picasso.with(context).load(result.getFoto_berita()).placeholder(R.color.greycustom2).into(Vh.IvBerita);
                } else {
                    Picasso.with(context).load(R.color.greycustom2).into(Vh.IvBerita);
                    Picasso.with(context).load(R.drawable.ic_user).placeholder(R.color.greycustom2).into(Vh.IvBerita);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Idetail = new Intent(context.getApplicationContext(), DetailBeritaActivity.class);
                        Idetail.putExtra("id_berita",result.getId_berita());
                        Idetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(Idetail);
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
        return BeritaPojos == null ? 0 : BeritaPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == BeritaPojos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(BeritaPojo r) {
        BeritaPojos.add(r);
        notifyItemInserted(BeritaPojos.size() - 1);
    }

    public void addAll(@NonNull List<BeritaPojo> moveResults) {
        for (BeritaPojo result : moveResults) {
            add(result);
        }
    }

    public void remove(BeritaPojo r) {
        int position = BeritaPojos.indexOf(r);
        if (position > -1) {
            BeritaPojos.remove(position);
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
        add(new BeritaPojo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = BeritaPojos.size() - 1;
        BeritaPojo result = getItem(position);

        if (result != null) {
            BeritaPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public BeritaPojo getItem(int position) {
        return BeritaPojos.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class BeritaPojoVH extends RecyclerView.ViewHolder {
        private TextView TxvJudulBerita,TxvDeskripsiBerita,TxvTglBerita;
        private ImageView IvBerita;
        public BeritaPojoVH(@NonNull View itemView) {
            super(itemView);
            TxvJudulBerita =  itemView.findViewById(R.id.TxvJudulBerita);
            TxvDeskripsiBerita =  itemView.findViewById(R.id.TxvDeskripsiBerita);
            TxvTglBerita =  itemView.findViewById(R.id.TxvTglBerita);
            IvBerita =  itemView.findViewById(R.id.IvBerita);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }


}

