package dresta.putra.aset.oper_berkas;


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
import dresta.putra.aset.pinjaman.DetailPinjamanActivity;
import dresta.putra.aset.pinjaman.PinjamanPojo;

public class OperBerkasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<PinjamanPojo> PinjamanPojos;
    private Context context;

    private boolean isLoadingAdded = false;

    public OperBerkasAdapter(Context context) {
        this.context = context;
        PinjamanPojos = new ArrayList<>();
    }


    public void setPinjamanPojos(List<PinjamanPojo> PinjamanPojos) {
        this.PinjamanPojos = PinjamanPojos;
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
        View v1 = inflater.inflate(R.layout.adapter_oper_berkas, parent, false);
        viewHolder = new PinjamanPojoVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Locale currentLocale = new Locale("id", "id");
        final PinjamanPojo result = PinjamanPojos.get(position); // PinjamanPojo
        switch (getItemViewType(position)) {
            case ITEM:
                final PinjamanPojoVH VH = (PinjamanPojoVH) holder;
                Locale localeID = new Locale("in", "ID");
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

                VH.TxvNamaNasabah.setText(result.getNasabah().getNama_nasabah());
                VH.TxvIdPinjaman.setText("#"+result.getId_pinjaman());
                VH.TxvJumlahPinjaman.setText(formatRupiah.format(Float.parseFloat(result.getJumlah_pinjaman_setelah_bunga())));
                VH.TxvTerbayar.setText("Terbayar : "+ formatRupiah.format(Float.parseFloat(result.getJumlah_terbayar())));
                float kurang = Float.parseFloat(result.getJumlah_pinjaman_setelah_bunga()) - Float.parseFloat(result.getJumlah_terbayar());
                VH.TxvKekurangan.setText("Kekurangan : "+ formatRupiah.format(kurang));
                VH.TxvAngsuran.setText("Angsuran : @ "+ formatRupiah.format(Float.parseFloat(result.getJumlah_perangsuran())));
                VH.TxvPeriode.setText("Periode : "+result.getPeriode_angsuran());
                VH.TxvLamaAngsuran.setText("Lama Angsuran : "+ result.getLama_angsuran());
                VH.TxvTglPinjaman.setText("Tgl. Pinjaman : "+ result.getTgl_pinjaman());
                VH.TxvAlamat.setText("Alamat : "+ result.getNasabah().getAlamat_rumah());

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    movieVH.TxvDeskripsi.setText(HtmlCompat.fromHtml(result.getDeskripsi_buku(), HtmlCompat.FROM_HTML_MODE_LEGACY));
//                } else {
//                    movieVH.TxvDeskripsi.setText(Html.fromHtml(result.getDeskripsi_buku()));
//                }
                if (result.getNasabah().getFoto_nasabah() != null && result.getNasabah().getFoto_nasabah().length() > 0) {
                    Picasso.with(context).load(result.getNasabah().getFoto_nasabah()).placeholder(R.color.greycustom2).into(VH.IvNasabah);
                } else {
                    Picasso.with(context).load(R.color.greycustom2).into(VH.IvNasabah);
                    Picasso.with(context).load(R.drawable.ic_user).placeholder(R.color.greycustom2).into(VH.IvNasabah);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Idetail = new Intent(context.getApplicationContext(), DetailPinjamanActivity.class);
                        Idetail.putExtra("id_pinjaman",result.getId_pinjaman());
                        context.startActivity(Idetail);
                    }
                });
                VH.BtnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Idetail = new Intent(context.getApplicationContext(), DetailPinjamanActivity.class);
                        Idetail.putExtra("id_pinjaman",result.getId_pinjaman());
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
        return PinjamanPojos == null ? 0 : PinjamanPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == PinjamanPojos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(PinjamanPojo r) {
        PinjamanPojos.add(r);
        notifyItemInserted(PinjamanPojos.size() - 1);
    }

    public void addAll(@NonNull List<PinjamanPojo> moveResults) {
        for (PinjamanPojo result : moveResults) {
            add(result);
        }
    }

    public void remove(PinjamanPojo r) {
        int position = PinjamanPojos.indexOf(r);
        if (position > -1) {
            PinjamanPojos.remove(position);
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
        add(new PinjamanPojo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = PinjamanPojos.size() - 1;
        PinjamanPojo result = getItem(position);

        if (result != null) {
            PinjamanPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public PinjamanPojo getItem(int position) {
        return PinjamanPojos.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class PinjamanPojoVH extends RecyclerView.ViewHolder {
        private TextView TxvAlamat,TxvTerbayar, TxvKekurangan, TxvAngsuran, TxvPeriode, TxvLamaAngsuran, TxvTglPinjaman,TxvNamaNasabah, TxvJumlahPinjaman, TxvIdPinjaman;
        private ImageView IvNasabah;
        private Button BtnDetail;
        public PinjamanPojoVH(@NonNull View itemView) {
            super(itemView);
            TxvAlamat =  itemView.findViewById(R.id.TxvAlamat);
            TxvTerbayar =  itemView.findViewById(R.id.TxvTerbayar);
            TxvKekurangan =  itemView.findViewById(R.id.TxvKekurangan);
            TxvAngsuran =  itemView.findViewById(R.id.TxvAngsuran);
            TxvPeriode =  itemView.findViewById(R.id.TxvPeriode);
            TxvLamaAngsuran =  itemView.findViewById(R.id.TxvLamaAngsuran);
            TxvTglPinjaman =  itemView.findViewById(R.id.TxvTglPinjaman);
            TxvNamaNasabah =  itemView.findViewById(R.id.TxvNamaNasabah);
            TxvJumlahPinjaman =  itemView.findViewById(R.id.TxvJumlahPinjaman);
            TxvIdPinjaman =  itemView.findViewById(R.id.TxvIdPinjaman);
            IvNasabah =  itemView.findViewById(R.id.IvNasabah);
            BtnDetail = itemView.findViewById(R.id.BtnDetail);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }


}

