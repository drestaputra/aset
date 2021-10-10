package dresta.putra.aset.peta;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import dresta.putra.aset.R;
import dresta.putra.aset.WebActivity;
import dresta.putra.aset.slider.AdapterSlider;

public class AdapterMarker extends PagerAdapter {
    private ViewPager viewPager;

    private List<PetaPojo> models;
    private Context context;
    private AdapterFotoMarker adapterFotoMarker;

    public AdapterMarker(List<PetaPojo> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.adapter_marker, container, false);
        viewPager = view.findViewById(R.id.viewPager);

        ImageView imageView;
        TextView title, desc;

        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);

//        if (models.get(position).getImage_slider() != null && models.get(position).getImage_slider().length() > 0) {
//            Picasso.with(context).load(models.get(position).getImage_slider()).placeholder(R.color.greycustom2).into(imageView);
//        } else {
//            Picasso.with(context).load(R.color.greycustom2).into(imageView);
//        }
        if (models.get(position).getFoto_aset() != null) {
            adapterFotoMarker = new AdapterFotoMarker(models.get(position).getFoto_aset(), Objects.requireNonNull(context));
            viewPager.setAdapter(adapterFotoMarker);
        }

        title.setText(models.get(position).getNama_aset());
        desc.setText(Html.fromHtml(String.valueOf(models.get(position).getKeterangan())));
        Button BtnDetail = view.findViewById(R.id.BtnDetail);
        BtnDetail.setOnClickListener(v -> {
            Intent intentl= new Intent(context.getApplicationContext(), DetailAsetActivity.class);
            intentl.putExtra("id_aset",models.get(position).getId_aset());
            intentl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentl);
        });
        ImageView BtnMap = view.findViewById(R.id.BtnMap);
        BtnMap.setOnClickListener(v->{
            Intent intentl= new Intent(context.getApplicationContext(), DetailPetaActivity.class);
            intentl.putExtra("id_aset",models.get(position).getId_aset());
            intentl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentl);
        });
        ImageView BtnStreetView = view.findViewById(R.id.BtnStreetView);
        BtnStreetView.setOnClickListener(v->{
            Intent intentl= new Intent(context.getApplicationContext(), StreetViewActivity.class);
            intentl.putExtra("id_aset",models.get(position).getId_aset());
            intentl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentl);
        });
        ImageView BtnRute = view.findViewById(R.id.BtnRute);
        BtnRute.setOnClickListener(v->{
            Intent intentl= new Intent(context.getApplicationContext(), RuteActivity.class);
            intentl.putExtra("id_aset",models.get(position).getId_aset());
            intentl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentl);
        });
        if ((models.get(position).getLatitude() != null && !models.get(position).getLatitude().equals("0")) && (models.get(position).getLongitude() != null && !models.get(position).getLongitude().equals("0"))){
            BtnMap.setVisibility(View.VISIBLE);
            BtnRute.setVisibility(View.VISIBLE);
            BtnStreetView.setVisibility(View.VISIBLE);
        }else{
            BtnMap.setVisibility(View.GONE);
            BtnRute.setVisibility(View.GONE);
            BtnStreetView.setVisibility(View.GONE);
        }


        container.addView(view, 0);
        //        slider
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
