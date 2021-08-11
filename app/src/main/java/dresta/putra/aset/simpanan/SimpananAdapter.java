package dresta.putra.aset.simpanan;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dresta.putra.aset.R;

public class SimpananAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<SimpananPojo> SimpananPojos;
    private Context context;

    private boolean isLoadingAdded = false;

    public SimpananAdapter(Context context) {
        this.context = context;
        SimpananPojos = new ArrayList<>();
    }


    public void setSimpananPojos(List<SimpananPojo> SimpananPojos) {
        this.SimpananPojos = SimpananPojos;
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
        View v1 = inflater.inflate(R.layout.adapter_simpanan, parent, false);
        viewHolder = new SimpananPojoVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Locale currentLocale = new Locale("id", "id");
        final SimpananPojo result = SimpananPojos.get(position); // SimpananPojo
        switch (getItemViewType(position)) {
            case ITEM:
                final SimpananPojoVH VH = (SimpananPojoVH) holder;
                Locale localeID = new Locale("in", "ID");
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                VH.TxvNamaNasabah.setText(result.getNasabah().getNama_nasabah());
                VH.TxvIdSimpanan.setText("ID : #"+result.getId_simpanan());
                VH.TxvJumlahSimpanan.setText("Total Simpanan : "+formatRupiah.format(Float.parseFloat(result.getJumlah_simpanan())));
                VH.TxvAlamat.setText("Alamat : "+ result.getNasabah().getAlamat_rumah());
                VH.TxvTglSimpanan.setText("Tgl. buka simpanan : "+ result.getTgl_simpanan());
                VH.TxvLastUpdate.setText("Tgl. terakhir simpanan : "+ result.getLast_update());

                if (result.getNasabah().getFoto_nasabah() != null && result.getNasabah().getFoto_nasabah().length() > 0) {
                    Picasso.with(context).load(result.getNasabah().getFoto_nasabah()).placeholder(R.color.greycustom2).into(VH.IvNasabah);
                } else {
                    Picasso.with(context).load(R.color.greycustom2).into(VH.IvNasabah);
                    Picasso.with(context).load(R.drawable.ic_user).placeholder(R.color.greycustom2).into(VH.IvNasabah);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent Idetail = new Intent(context.getApplicationContext(), DetailPinjamanActivity.class);
//                        Idetail.putExtra("id_pinjaman",result.getId_pinjaman());
//                        context.startActivity(Idetail);
                    }
                });
                VH.BtnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Idetail = new Intent(context.getApplicationContext(), RiwayatSimpananActivity.class);
                        Idetail.putExtra("id_simpanan",result.getId_simpanan());
                        context.startActivity(Idetail);
                    }
                });
                VH.BtnTambah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Idetail = new Intent(context.getApplicationContext(), SimpanUangActivity.class);
                        Idetail.putExtra("id_simpanan",result.getId_simpanan());
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
        return SimpananPojos == null ? 0 : SimpananPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == SimpananPojos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(SimpananPojo r) {
        SimpananPojos.add(r);
        notifyItemInserted(SimpananPojos.size() - 1);
    }

    public void addAll(@NonNull List<SimpananPojo> moveResults) {
        for (SimpananPojo result : moveResults) {
            add(result);
        }
    }

    public void remove(SimpananPojo r) {
        int position = SimpananPojos.indexOf(r);
        if (position > -1) {
            SimpananPojos.remove(position);
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
        add(new SimpananPojo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = SimpananPojos.size() - 1;
        SimpananPojo result = getItem(position);

        if (result != null) {
            SimpananPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public SimpananPojo getItem(int position) {
        return SimpananPojos.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class SimpananPojoVH extends RecyclerView.ViewHolder {
        private TextView TxvLastUpdate,TxvTglSimpanan,TxvAlamat, TxvIdSimpanan,TxvNamaNasabah, TxvJumlahSimpanan;
        private ImageView IvNasabah;
        private Button BtnDetail,BtnTambah;
        public SimpananPojoVH(@NonNull View itemView) {
            super(itemView);
            TxvTglSimpanan =  itemView.findViewById(R.id.TxvTglSimpanan);
            TxvLastUpdate =  itemView.findViewById(R.id.TxvLastUpdate);
            TxvAlamat =  itemView.findViewById(R.id.TxvAlamat);
            TxvIdSimpanan =  itemView.findViewById(R.id.TxvIdSimpanan);
            TxvNamaNasabah =  itemView.findViewById(R.id.TxvNamaNasabah);
            TxvJumlahSimpanan =  itemView.findViewById(R.id.TxvJumlahSimpanan);
            TxvIdSimpanan =  itemView.findViewById(R.id.TxvIdSimpanan);
            IvNasabah =  itemView.findViewById(R.id.IvNasabah);
            BtnDetail = itemView.findViewById(R.id.BtnDetail);
            BtnTambah = itemView.findViewById(R.id.BtnTambah);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }


}

