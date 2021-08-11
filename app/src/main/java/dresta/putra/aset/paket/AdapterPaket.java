package dresta.putra.aset.paket;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import dresta.putra.aset.R;

public class AdapterPaket extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<PaketPojo> PaketPojos;
    private Context context;

    private boolean isLoadingAdded = false;

    public AdapterPaket(Context context, List<PaketPojo> paketPojos) {
        this.context = context;
        PaketPojos = paketPojos;
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
        return Objects.requireNonNull(viewHolder);
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, @NonNull LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.adapter_jenis_pembayaran, parent, false);
        viewHolder = new PaketPojoVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final PaketPojo models = PaketPojos.get(position); // PaketPojo
        switch (getItemViewType(position)) {
            case ITEM:
                final PaketPojoVH Vh = (PaketPojoVH) holder;
                Locale localeID = new Locale("in", "ID");
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                Vh.TxvNamaPaket.setText(models.getNama_paket());
                Vh.TxvDeskripsiPaket.setText(HtmlCompat.fromHtml(models.getDeskripsi_paket(),HtmlCompat.FROM_HTML_MODE_LEGACY));
                Vh.TxvHargaPaket.setText(formatRupiah.format(Float.parseFloat(models.getHarga_paket())));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentl= new Intent(context.getApplicationContext(), RequestActivty.class);
                        intentl.putExtra("id_paket",models.getId_paket());
                        intentl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentl);
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
        return PaketPojos == null ? 0 : PaketPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == PaketPojos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(PaketPojo r) {
        PaketPojos.add(r);
        notifyItemInserted(PaketPojos.size() - 1);
    }

    public void addAll(@NonNull List<PaketPojo> moveResults) {
        for (PaketPojo result : moveResults) {
            add(result);
        }
    }

    public void remove(PaketPojo r) {
        int position = PaketPojos.indexOf(r);
        if (position > -1) {
            PaketPojos.remove(position);
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
        add(new PaketPojo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = PaketPojos.size() - 1;
        PaketPojo result = getItem(position);

        if (result != null) {
            PaketPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public PaketPojo getItem(int position) {
        return PaketPojos.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class PaketPojoVH extends RecyclerView.ViewHolder {
        TextView TxvNamaPaket, TxvDeskripsiPaket, TxvHargaPaket;
        public PaketPojoVH(@NonNull View itemView) {
            super(itemView);
            TxvNamaPaket = itemView.findViewById(R.id.TxvNamaPaket);
            TxvDeskripsiPaket = itemView.findViewById(R.id.TxvDeskripsiPaket);
            TxvHargaPaket = itemView.findViewById(R.id.TxvHargaPaket);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }


}

