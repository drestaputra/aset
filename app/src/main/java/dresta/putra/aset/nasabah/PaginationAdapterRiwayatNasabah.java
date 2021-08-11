package dresta.putra.aset.nasabah;


import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dresta.putra.aset.R;
import dresta.putra.aset.pinjaman.DetailPinjamanActivity;
import dresta.putra.aset.simpanan.RiwayatSimpananActivity;

public class PaginationAdapterRiwayatNasabah extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<RiwayatNasabahPojo> RiwayatNasabahPojos;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapterRiwayatNasabah(Context context) {
        this.context = context;
        RiwayatNasabahPojos = new ArrayList<>();
    }


    public void setRiwayatNasabahPojos(List<RiwayatNasabahPojo> RiwayatNasabahPojos) {
        this.RiwayatNasabahPojos = RiwayatNasabahPojos;
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
        View v1 = inflater.inflate(R.layout.adapter_riwayat_nasabah, parent, false);
        viewHolder = new RiwayatNasabahPojoVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final RiwayatNasabahPojo result = RiwayatNasabahPojos.get(position); // RiwayatNasabahPojo
        switch (getItemViewType(position)) {
            case ITEM:
                final RiwayatNasabahPojoVH Vh = (RiwayatNasabahPojoVH) holder;
//                private TextView  TxvKeteranganRiwayat, TxvTglRiwayat;
                Vh.TxvIdTransaksi.setText("#"+result.getId_transaksi());
                Vh.TxvIdTransaksi.setPaintFlags(Vh.TxvIdTransaksi.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                Vh.TxvJumlahRiwayat.setText(result.getJumlah_riwayat());
                Vh.TxvTipeTransaksi.setText(result.getTipe_transaksi().toUpperCase());
                Vh.TxvTglRiwayat.setText(result.getTgl_riwayat());
                Vh.TxvKeteranganRiwayat.setText(result.getKeterangan_riwayat());
                Vh.TxvIdTransaksi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (result.getTipe_transaksi().equals("simpanan")){
                            Intent Idetail = new Intent(context.getApplicationContext(), RiwayatSimpananActivity.class);
                            Idetail.putExtra("id_simpanan",result.getId_transaksi());
                            Idetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(Idetail);
                        }else if(result.getTipe_transaksi().equals("pinjaman")){
                            Intent Idetail = new Intent(context.getApplicationContext(), DetailPinjamanActivity.class);
                            Idetail.putExtra("id_pinjaman",result.getId_transaksi());
                            Idetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(Idetail);
                        }
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
        return RiwayatNasabahPojos == null ? 0 : RiwayatNasabahPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == RiwayatNasabahPojos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(RiwayatNasabahPojo r) {
        RiwayatNasabahPojos.add(r);
        notifyItemInserted(RiwayatNasabahPojos.size() - 1);
    }

    public void addAll(@NonNull List<RiwayatNasabahPojo> moveResults) {
        for (RiwayatNasabahPojo result : moveResults) {
            add(result);
        }
    }

    public void remove(RiwayatNasabahPojo r) {
        int position = RiwayatNasabahPojos.indexOf(r);
        if (position > -1) {
            RiwayatNasabahPojos.remove(position);
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
        add(new RiwayatNasabahPojo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = RiwayatNasabahPojos.size() - 1;
        RiwayatNasabahPojo result = getItem(position);

        if (result != null) {
            RiwayatNasabahPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public RiwayatNasabahPojo getItem(int position) {
        return RiwayatNasabahPojos.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class RiwayatNasabahPojoVH extends RecyclerView.ViewHolder {
        private TextView TxvIdTransaksi, TxvTipeTransaksi, TxvJumlahRiwayat, TxvKeteranganRiwayat, TxvTglRiwayat;
        public RiwayatNasabahPojoVH(@NonNull View itemView) {
            super(itemView);
            TxvIdTransaksi =  itemView.findViewById(R.id.TxvIdTransaksi);
            TxvTipeTransaksi =  itemView.findViewById(R.id.TxvTipeTransaksi);
            TxvJumlahRiwayat =  itemView.findViewById(R.id.TxvJumlahRiwayat);
            TxvKeteranganRiwayat =  itemView.findViewById(R.id.TxvKeteranganRiwayat);
            TxvTglRiwayat =  itemView.findViewById(R.id.TxvTglRiwayat);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }


}

