package dresta.putra.aset.nasabah;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dresta.putra.aset.R;

public class PaginationAdapterNasabah extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<NasabahPojo> NasabahPojos;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapterNasabah(Context context) {
        this.context = context;
        NasabahPojos = new ArrayList<>();
    }


    public void setNasabahPojos(List<NasabahPojo> NasabahPojos) {
        this.NasabahPojos = NasabahPojos;
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
        View v1 = inflater.inflate(R.layout.adapter_nasabah, parent, false);
        viewHolder = new NasabahPojoVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final NasabahPojo result = NasabahPojos.get(position); // NasabahPojo
        switch (getItemViewType(position)) {
            case ITEM:
                final NasabahPojoVH movieVH = (NasabahPojoVH) holder;
//                movieVH.TxvNamaBlok.setText(result.getJudul_buku());
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    movieVH.TxvDeskripsi.setText(HtmlCompat.fromHtml(result.getDeskripsi_buku(), HtmlCompat.FROM_HTML_MODE_LEGACY));
//                } else {
//                    movieVH.TxvDeskripsi.setText(Html.fromHtml(result.getDeskripsi_buku()));
//                }
                movieVH.TxvNamaNasabah.setText(result.getNama_nasabah());
                movieVH.TxvNamaUsaha.setText(result.getNama_usaha());
                if (result.getFoto_nasabah() != null && result.getFoto_nasabah().length() > 0) {
                    Picasso.with(context).load(result.getFoto_nasabah()).placeholder(R.color.greycustom2).into(movieVH.IvNasabah);
                } else {
                    Picasso.with(context).load(R.color.greycustom2).into(movieVH.IvNasabah);
                    Picasso.with(context).load(R.drawable.ic_user).placeholder(R.color.greycustom2).into(movieVH.IvNasabah);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Idetail = new Intent(context.getApplicationContext(), DetailNasabahActivity.class);
                        Idetail.putExtra("id_nasabah",result.getId_nasabah());
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
        return NasabahPojos == null ? 0 : NasabahPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == NasabahPojos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(NasabahPojo r) {
        NasabahPojos.add(r);
        notifyItemInserted(NasabahPojos.size() - 1);
    }

    public void addAll(@NonNull List<NasabahPojo> moveResults) {
        for (NasabahPojo result : moveResults) {
            add(result);
        }
    }

    public void remove(NasabahPojo r) {
        int position = NasabahPojos.indexOf(r);
        if (position > -1) {
            NasabahPojos.remove(position);
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
        add(new NasabahPojo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = NasabahPojos.size() - 1;
        NasabahPojo result = getItem(position);

        if (result != null) {
            NasabahPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public NasabahPojo getItem(int position) {
        return NasabahPojos.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class NasabahPojoVH extends RecyclerView.ViewHolder {
        private TextView TxvNamaNasabah,TxvNamaUsaha;
        private ImageView IvNasabah;
        public NasabahPojoVH(@NonNull View itemView) {
            super(itemView);
            TxvNamaNasabah =  itemView.findViewById(R.id.TxvNamaNasabah);
            IvNasabah =  itemView.findViewById(R.id.IvNasabah);
            TxvNamaUsaha =  itemView.findViewById(R.id.TxvNamaUsaha);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }


}

