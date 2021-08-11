package dresta.putra.aset.riwayat_simpanan;


import android.content.Context;
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

public class PaginationAdapterRiwayatSimpanan extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<RiwayatSimpananPojo> RiwayatSimpananPojos;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapterRiwayatSimpanan(Context context) {
        this.context = context;
        RiwayatSimpananPojos = new ArrayList<>();
    }


    public void setRiwayatSimpananPojos(List<RiwayatSimpananPojo> RiwayatSimpananPojos) {
        this.RiwayatSimpananPojos = RiwayatSimpananPojos;
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
        View v1 = inflater.inflate(R.layout.adapter_riwayat_simpanan, parent, false);
        viewHolder = new RiwayatSimpananPojoVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final RiwayatSimpananPojo result = RiwayatSimpananPojos.get(position); // RiwayatSimpananPojo
        switch (getItemViewType(position)) {
            case ITEM:
                final RiwayatSimpananPojoVH Vh = (RiwayatSimpananPojoVH) holder;
                Vh.TxvJumlahRiwayat.setText(result.getJumlah_riwayat_simpanan());
                Vh.TxvIdSimpanan.setText("ID Simpanan : #"+result.getId_simpanan());
                Vh.TxvIdSimpanan.setPaintFlags(Vh.TxvIdSimpanan.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                Vh.TxvTotalSimpanan.setText("Total : "+result.getJumlah_simpanan_sebelumnya());
                Vh.TxvKeteranganRiwayat.setText("Keterangan : "+result.getKeterangan_riwayat());
                Vh.TxvTglRiwayat.setText(result.getTgl_riwayat_simpanan());
                Vh.TxvIdSimpanan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent Idetail = new Intent(context.getApplicationContext(), DetailPinjamanActivity.class);
//                        Idetail.putExtra("id_pinjaman",result.getId_pinjaman());
//                        Idetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(Idetail);
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
        return RiwayatSimpananPojos == null ? 0 : RiwayatSimpananPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == RiwayatSimpananPojos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(RiwayatSimpananPojo r) {
        RiwayatSimpananPojos.add(r);
        notifyItemInserted(RiwayatSimpananPojos.size() - 1);
    }

    public void addAll(@NonNull List<RiwayatSimpananPojo> moveResults) {
        for (RiwayatSimpananPojo result : moveResults) {
            add(result);
        }
    }

    public void remove(RiwayatSimpananPojo r) {
        int position = RiwayatSimpananPojos.indexOf(r);
        if (position > -1) {
            RiwayatSimpananPojos.remove(position);
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
        add(new RiwayatSimpananPojo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = RiwayatSimpananPojos.size() - 1;
        RiwayatSimpananPojo result = getItem(position);

        if (result != null) {
            RiwayatSimpananPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public RiwayatSimpananPojo getItem(int position) {
        return RiwayatSimpananPojos.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class RiwayatSimpananPojoVH extends RecyclerView.ViewHolder {
        private TextView TxvIdSimpanan,TxvTotalSimpanan,TxvJumlahRiwayat,TxvKeteranganRiwayat,TxvTglRiwayat;
        public RiwayatSimpananPojoVH(@NonNull View itemView) {
            super(itemView);
            TxvIdSimpanan =  itemView.findViewById(R.id.TxvIdSimpanan);
            TxvTotalSimpanan =  itemView.findViewById(R.id.TxvTotalSimpanan);
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

