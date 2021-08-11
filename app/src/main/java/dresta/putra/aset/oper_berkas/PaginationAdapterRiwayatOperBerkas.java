package dresta.putra.aset.oper_berkas;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.ResponsePojo;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.nasabah.DetailNasabahActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class PaginationAdapterRiwayatOperBerkas extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<OperBerkasPojo> OperBerkasPojos;
    private Context context;
    private PrefManager prefManager;

    private boolean isLoadingAdded = false;

    public PaginationAdapterRiwayatOperBerkas(Context context) {
        this.context = context;
        OperBerkasPojos = new ArrayList<>();
    }


    public void setOperBerkasPojos(List<OperBerkasPojo> OperBerkasPojos) {
        this.OperBerkasPojos = OperBerkasPojos;
    }
    interface APIAdapterOperBerkas{
        @FormUrlEncoded
        @POST("api/oper_berkas/proses_oper_berkas")
        Call<ResponsePojo> terimaOperBerkas(@Field("id_oper_berkas") String id_oper_berkas,@Field("status") String status);
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
        View v1 = inflater.inflate(R.layout.adapter_riwayat_oper_berkas, parent, false);
        viewHolder = new OperBerkasPojoVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        prefManager = new PrefManager(context);
        String id_kolektor_loged_in = prefManager.LoggedInIdKolektor();
        final OperBerkasPojo result = OperBerkasPojos.get(position); // OperBerkasPojo
        APIAdapterOperBerkas apiAdapterOperBerkas = RetrofitClientInstance.getRetrofitInstance(context).create(APIAdapterOperBerkas.class);
        final Call<ResponsePojo> responsePojoCallTerima = apiAdapterOperBerkas.terimaOperBerkas(result.id_oper_berkas,"done");
        final Call<ResponsePojo> responsePojoCallTolak = apiAdapterOperBerkas.terimaOperBerkas(result.id_oper_berkas,"tolak");
        switch (getItemViewType(position)) {
            case ITEM:
//                private TextView TxvIdPinjaman,TxvTglOperBerkas,TxvStatus,TxvKolektorDari,TxvKolektorKe;
//                private CardView CvItem;
                final OperBerkasPojoVH Vh = (OperBerkasPojoVH) holder;
                Vh.TxvIdPinjaman.setText("Nasabah : #"+result.getNama_nasabah());
                Vh.TxvIdPinjaman.setPaintFlags(Vh.TxvIdPinjaman.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                Vh.TxvIdPinjaman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent IPinjaman = new Intent(context, DetailNasabahActivity.class);
                        IPinjaman.putExtra("id_nasabah",result.getId_nasabah());
                        IPinjaman.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(IPinjaman);
                    }
                });
                Vh.TxvTglOperBerkas.setText("Tgl : "+result.getTgl_oper_berkas());
                Vh.TxvStatus.setText("Status : "+result.getStatus());
                Vh.TxvKolektorDari.setText(result.getUsername_kolektor_dari());
                Vh.TxvKolektorKe.setText(result.getUsername_kolektor_ke());
                if (result.getStatus()!=null && result.getStatus().equals("proses") && result.getId_kolektor_ke().equals(id_kolektor_loged_in))
                {
                    Vh.CvItem.setCardBackgroundColor(Color.argb(100,30,136,229));
                    Vh.BtnTerima.setVisibility(View.VISIBLE);
                    Vh.BtnTerima.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            responsePojoCallTerima.enqueue(new Callback<ResponsePojo>() {
                                @Override
                                public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                                    if (response.body()!=null){
                                        if (response.body().getStatus()==200){
                                            Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                            Vh.BtnTerima.setVisibility(View.GONE);
                                            Vh.BtnTolak.setVisibility(View.GONE);
                                            Intent Ireload = new Intent(context.getApplicationContext(), RiwayatOperBerkasActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            Ireload.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            context.startActivity(Ireload);
                                        }else{
                                            Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponsePojo> call, Throwable t) {
                                    Toast.makeText(context, "Gagal, terjadi gangguan jaringan", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    Vh.BtnTolak.setVisibility(View.VISIBLE);
                    Vh.BtnTolak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            responsePojoCallTolak.enqueue(new Callback<ResponsePojo>() {
                                @Override
                                public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                                    if (response.body()!=null){
                                        if (response.body().getStatus()==200){
                                            Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                            Vh.BtnTolak.setVisibility(View.GONE);
                                            Vh.BtnTerima.setVisibility(View.GONE);
                                        }else{
                                            Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponsePojo> call, Throwable t) {
                                    Toast.makeText(context, "Gagal, terjadi gangguan jaringan", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }else if (result.getStatus()!=null && result.getStatus().equals("tolak")){
                    Vh.CvItem.setCardBackgroundColor(Color.argb(100,253,216,53));
                }else if (result.getStatus()!=null && result.getStatus().equals("done")){
                    Vh.CvItem.setCardBackgroundColor(Color.argb(100,76,175,80));
                }

//                movieVH.TxvNamaUsaha.setText(result.getNama_usaha());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent Idetail = new Intent(context.getApplicationContext(), DetailNasabahActivity.class);
//                        Idetail.putExtra("id_nasabah",result.getId_nasabah());
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
        return OperBerkasPojos == null ? 0 : OperBerkasPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == OperBerkasPojos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(OperBerkasPojo r) {
        OperBerkasPojos.add(r);
        notifyItemInserted(OperBerkasPojos.size() - 1);
    }

    public void addAll(@NonNull List<OperBerkasPojo> moveResults) {
        for (OperBerkasPojo result : moveResults) {
            add(result);
        }
    }

    public void remove(OperBerkasPojo r) {
        int position = OperBerkasPojos.indexOf(r);
        if (position > -1) {
            OperBerkasPojos.remove(position);
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
        add(new OperBerkasPojo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = OperBerkasPojos.size() - 1;
        OperBerkasPojo result = getItem(position);

        if (result != null) {
            OperBerkasPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public OperBerkasPojo getItem(int position) {
        return OperBerkasPojos.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class OperBerkasPojoVH extends RecyclerView.ViewHolder {
        private TextView TxvIdPinjaman,TxvTglOperBerkas,TxvStatus,TxvKolektorDari,TxvKolektorKe;
        private CardView CvItem;
        private Button BtnTerima,BtnTolak;
        public OperBerkasPojoVH(@NonNull View itemView) {
            super(itemView);
            TxvIdPinjaman =  itemView.findViewById(R.id.TxvIdPinjaman);
            TxvTglOperBerkas =  itemView.findViewById(R.id.TxvTglOperBerkas);
            TxvStatus =  itemView.findViewById(R.id.TxvStatus);
            TxvKolektorDari =  itemView.findViewById(R.id.TxvKolektorDari);
            TxvKolektorKe =  itemView.findViewById(R.id.TxvKolektorKe);
            CvItem =  itemView.findViewById(R.id.CvItem);
            BtnTerima =  itemView.findViewById(R.id.BtnTerima);
            BtnTolak =  itemView.findViewById(R.id.BtnTolak);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }


}

