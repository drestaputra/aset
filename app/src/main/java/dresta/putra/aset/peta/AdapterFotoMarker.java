package dresta.putra.aset.peta;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
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

public class AdapterFotoMarker extends PagerAdapter {
    private ViewPager viewPager;
    private List<FotoAsetPojo> models;
    private Context context;
    private AdapterFotoMarker adapterFotoMarker;

    public AdapterFotoMarker(List<FotoAsetPojo> models, Context context) {
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
        View view = layoutInflater.inflate(R.layout.adapter_foto_marker, container, false);
        ImageView image = view.findViewById(R.id.image);
        view.setPadding(0,0,10,0);


        if (models.get(position).getFoto_aset() != null && models.get(position).getFoto_aset().length() > 0) {
            Picasso.with(context).load(models.get(position).getFoto_aset()).placeholder(R.color.greycustom2).into(image);
        } else {
            Picasso.with(context).load(R.color.greycustom2).into(image);
        }
//        if (models.get(position).getFoto_aset() != null) {
//            adapterFotoMarker = new AdapterFotoMarker(models.get(position).getFoto_aset(), Objects.requireNonNull(context));
//            viewPager.setAdapter(adapter);
//            viewPager.setPadding(0, 0, 30, 0);
//        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentl= new Intent(context.getApplicationContext(), WebActivity.class);
//                intentl.putExtra("url",models.get(position).getLink_slider());
//                intentl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intentl);
            }
        });

        container.addView(view, 0);
        //        slider

        return view;
    }
    @Override
    public float getPageWidth(final int position) {
        return 0.9f;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
