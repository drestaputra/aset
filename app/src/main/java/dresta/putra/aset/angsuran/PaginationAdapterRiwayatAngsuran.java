package dresta.putra.aset.angsuran;


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

public class PaginationAdapterRiwayatAngsuran extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<RiwayatPinjamanPojo> RiwayatPinjamanPojos;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapterRiwayatAngsuran(Context context) {
        this.context = context;
        RiwayatPinjamanPojos = new ArrayList<>();
    }


    public void setRiwayatPinjamanPojos(List<RiwayatPinjamanPojo> RiwayatPinjamanPojos) {
        this.RiwayatPinjamanPojos = RiwayatPinjamanPojos;
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
        View v1 = inflater.inflate(R.layout.adapter_riwayat_angsuran, parent, false);
        viewHolder = new RiwayatPinjamanPojoVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final RiwayatPinjamanPojo result = RiwayatPinjamanPojos.get(position); // RiwayatPinjamanPojo
        switch (getItemViewType(position)) {
            case ITEM:
                final RiwayatPinjamanPojoVH Vh = (RiwayatPinjamanPojoVH) holder;
                Vh.TxvAngsuranKe.setText("Angsuran ke : "+result.getAngsuran_ke());
                Vh.TxvIdPinjaman.setText("ID Pinjaman : #"+result.getId_pinjaman());
                Vh.TxvIdPinjaman.setPaintFlags(Vh.TxvIdPinjaman.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                Vh.TxvJumlahAngsuran.setText(result.getJumlah_riwayat_pembayaran());
                Vh.TxvKeteranganRiwayat.setText("Keterangan : "+result.getKeterangan_riwayat());
                Vh.TxvTglAngsuran.setText(result.getTgl_riwayat_pinjaman());
                Vh.TxvIdPinjaman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Idetail = new Intent(context.getApplicationContext(), DetailPinjamanActivity.class);
                        Idetail.putExtra("id_pinjaman",result.getId_pinjaman());
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
        return RiwayatPinjamanPojos == null ? 0 : RiwayatPinjamanPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == RiwayatPinjamanPojos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(RiwayatPinjamanPojo r) {
        RiwayatPinjamanPojos.add(r);
        notifyItemInserted(RiwayatPinjamanPojos.size() - 1);
    }

    public void addAll(@NonNull List<RiwayatPinjamanPojo> moveResults) {
        for (RiwayatPinjamanPojo result : moveResults) {
            add(result);
        }
    }

    public void remove(RiwayatPinjamanPojo r) {
        int position = RiwayatPinjamanPojos.indexOf(r);
        if (position > -1) {
            RiwayatPinjamanPojos.remove(position);
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
        add(new RiwayatPinjamanPojo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = RiwayatPinjamanPojos.size() - 1;
        RiwayatPinjamanPojo result = getItem(position);

        if (result != null) {
            RiwayatPinjamanPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public RiwayatPinjamanPojo getItem(int position) {
        return RiwayatPinjamanPojos.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class RiwayatPinjamanPojoVH extends RecyclerView.ViewHolder {
        private TextView TxvIdPinjaman,TxvAngsuranKe,TxvJumlahAngsuran,TxvKeteranganRiwayat,TxvTglAngsuran;
        public RiwayatPinjamanPojoVH(@NonNull View itemView) {
            super(itemView);
            TxvIdPinjaman =  itemView.findViewById(R.id.TxvIdPinjaman);
            TxvAngsuranKe =  itemView.findViewById(R.id.TxvAngsuranKe);
            TxvJumlahAngsuran =  itemView.findViewById(R.id.TxvJumlahAngsuran);
            TxvKeteranganRiwayat =  itemView.findViewById(R.id.TxvKeteranganRiwayat);
            TxvTglAngsuran =  itemView.findViewById(R.id.TxvTglAngsuran);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }


}

