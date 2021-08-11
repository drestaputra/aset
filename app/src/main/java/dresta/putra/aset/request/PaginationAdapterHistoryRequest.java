package dresta.putra.aset.request;


import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dresta.putra.aset.R;

public class PaginationAdapterHistoryRequest extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<RequestPojo> RequestPojos;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapterHistoryRequest(Context context) {
        this.context = context;
        RequestPojos = new ArrayList<>();
    }


    public void setRequestPojos(List<RequestPojo> RequestPojos) {
        this.RequestPojos = RequestPojos;
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
        View v1 = inflater.inflate(R.layout.adapter_riwayat_request, parent, false);
        viewHolder = new RequestPojoVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final RequestPojo result = RequestPojos.get(position); // RequestPojo
        switch (getItemViewType(position)) {
            case ITEM:
                final RequestPojoVH Vh = (RequestPojoVH) holder;
//                private TextView TxvStatus,TxvNoInvoice,TxvNamaPaket,TxvTotalTagihanInvoice,TxvTglRequest;
                Vh.TxvStatus.setText("Status : "+result.getStatus());
                Vh.TxvNoInvoice.setText("Invoice : #"+result.getNo_invoice());
                Vh.TxvNoInvoice.setPaintFlags(Vh.TxvNoInvoice.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                Locale localeID = new Locale("in", "ID");
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                Vh.TxvTotalTagihanInvoice.setText(formatRupiah.format(Float.parseFloat(result.getTotal_tagihan_invoice())));
                Vh.TxvTglRequest.setText(result.getTgl_request());
                Vh.TxvNamaPaket.setText("Paket : "+result.getNama_paket());

                Vh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Idetail = new Intent(context.getApplicationContext(), DetailInvoiceActivity.class);
                        Idetail.putExtra("id_request",result.getId_request());
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
        return RequestPojos == null ? 0 : RequestPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == RequestPojos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(RequestPojo r) {
        RequestPojos.add(r);
        notifyItemInserted(RequestPojos.size() - 1);
    }

    public void addAll(@NonNull List<RequestPojo> moveResults) {
        for (RequestPojo result : moveResults) {
            add(result);
        }
    }

    public void remove(RequestPojo r) {
        int position = RequestPojos.indexOf(r);
        if (position > -1) {
            RequestPojos.remove(position);
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
        add(new RequestPojo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = RequestPojos.size() - 1;
        RequestPojo result = getItem(position);

        if (result != null) {
            RequestPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public RequestPojo getItem(int position) {
        return RequestPojos.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class RequestPojoVH extends RecyclerView.ViewHolder {
        private TextView TxvStatus,TxvNoInvoice,TxvNamaPaket,TxvTotalTagihanInvoice,TxvTglRequest;
        public RequestPojoVH(@NonNull View itemView) {
            super(itemView);
            TxvStatus =  itemView.findViewById(R.id.TxvStatus);
            TxvNoInvoice =  itemView.findViewById(R.id.TxvNoInvoice);
            TxvNamaPaket =  itemView.findViewById(R.id.TxvNamaPaket);
            TxvTotalTagihanInvoice =  itemView.findViewById(R.id.TxvTotalTagihanInvoice);
            TxvTglRequest =  itemView.findViewById(R.id.TxvTglRequest);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }


}

