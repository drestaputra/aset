package dresta.putra.aset.informasi_program;


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

public class PaginationAdapterInformasiProgram extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<InformasiProgramPojo> InformasiProgramPojos;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapterInformasiProgram(Context context) {
        this.context = context;
        InformasiProgramPojos = new ArrayList<>();
    }


    public void setInformasiProgramPojos(List<InformasiProgramPojo> InformasiProgramPojos) {
        this.InformasiProgramPojos = InformasiProgramPojos;
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
        View v1 = inflater.inflate(R.layout.adapter_informasi_program, parent, false);
        viewHolder = new InformasiProgramPojoVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final InformasiProgramPojo result = InformasiProgramPojos.get(position); // InformasiProgramPojo
        switch (getItemViewType(position)) {
            case ITEM:
                final InformasiProgramPojoVH Vh = (InformasiProgramPojoVH) holder;
//                private TextView TxvJudulInformasiProgram,TxvDeskripsiInformasiProgram,TxvTglInformasiProgram;
//                private ImageView IvInformasiProgram;
                Vh.TxvJudulInformasiProgram.setText(result.getJudul_informasi_program());
                Vh.TxvTglInformasiProgram.setText(result.getTgl_informasi_program());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Vh.TxvDeskripsiInformasiProgram.setText(HtmlCompat.fromHtml(result.getDeskripsi_informasi_program(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    Vh.TxvDeskripsiInformasiProgram.setText(Html.fromHtml(result.getDeskripsi_informasi_program()));
                }
                if (result.getFoto_informasi_program() != null && result.getFoto_informasi_program().length() > 0) {
                    Picasso.with(context).load(result.getFoto_informasi_program()).placeholder(R.color.greycustom2).into(Vh.IvInformasiProgram);
                } else {
                    Picasso.with(context).load(R.color.greycustom2).into(Vh.IvInformasiProgram);
                    Picasso.with(context).load(R.drawable.ic_user).placeholder(R.color.greycustom2).into(Vh.IvInformasiProgram);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Idetail = new Intent(context.getApplicationContext(), DetailInformasiProgramActivity.class);
                        Idetail.putExtra("id_informasi_program",result.getId_informasi_program());
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
        return InformasiProgramPojos == null ? 0 : InformasiProgramPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == InformasiProgramPojos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(InformasiProgramPojo r) {
        InformasiProgramPojos.add(r);
        notifyItemInserted(InformasiProgramPojos.size() - 1);
    }

    public void addAll(@NonNull List<InformasiProgramPojo> moveResults) {
        for (InformasiProgramPojo result : moveResults) {
            add(result);
        }
    }

    public void remove(InformasiProgramPojo r) {
        int position = InformasiProgramPojos.indexOf(r);
        if (position > -1) {
            InformasiProgramPojos.remove(position);
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
        add(new InformasiProgramPojo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = InformasiProgramPojos.size() - 1;
        InformasiProgramPojo result = getItem(position);

        if (result != null) {
            InformasiProgramPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public InformasiProgramPojo getItem(int position) {
        return InformasiProgramPojos.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class InformasiProgramPojoVH extends RecyclerView.ViewHolder {
        private TextView TxvJudulInformasiProgram,TxvDeskripsiInformasiProgram,TxvTglInformasiProgram;
        private ImageView IvInformasiProgram;
        public InformasiProgramPojoVH(@NonNull View itemView) {
            super(itemView);
            TxvJudulInformasiProgram =  itemView.findViewById(R.id.TxvJudulInformasiProgram);
            TxvDeskripsiInformasiProgram =  itemView.findViewById(R.id.TxvDeskripsiInformasiProgram);
            TxvTglInformasiProgram =  itemView.findViewById(R.id.TxvTglInformasiProgram);
            IvInformasiProgram =  itemView.findViewById(R.id.IvInformasiProgram);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }


}

