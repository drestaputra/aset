package dresta.putra.aset.slider;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;
import dresta.putra.aset.R;
import dresta.putra.aset.WebActivity;

public class AdapterSlider extends PagerAdapter {

    private List<SliderPojo> models;
    private Context context;

    public AdapterSlider(List<SliderPojo> models, Context context) {
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
        View view = layoutInflater.inflate(R.layout.adapter_slider, container, false);

        ImageView imageView;
        TextView title, desc;

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        if (models.get(position).getImage_slider() != null && models.get(position).getImage_slider().length() > 0) {
                Picasso.with(context).load(models.get(position).getImage_slider()).placeholder(R.color.greycustom2).into(imageView);
            } else {
                Picasso.with(context).load(R.color.greycustom2).into(imageView);
            }
        title.setText(models.get(position).getJudul_slider());
        desc.setText(Html.fromHtml(models.get(position).getDeskripsi_slider()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentl= new Intent(context.getApplicationContext(), WebActivity.class);
                intentl.putExtra("url",models.get(position).getLink_slider());
                intentl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentl);
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
