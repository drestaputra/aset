package dresta.putra.aset.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.WebActivity;
import dresta.putra.aset.kontak.KontakPojo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class FragmentKontak extends Fragment {
    private TextView TxvAppName,TxvAppDescription, TxvAppDev, TxvAppDevLink, TxvAppAddress, TxvInstagram, TxvFacebook, TxvTwitter, TxvWhatsapp, TxvNoHp, TxvEmail;
    private PrefManager prefManager;
    private KontakPojo kontakPojo = new KontakPojo();
    public FragmentKontak() {
        // Required empty public constructor
    }
    interface APIFragmentKontak{
        @GET("api/about/index")
        Call<KontakPojo> getAbout();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_kontak, container, false);
        prefManager = new PrefManager(getContext());
        TxvAppName = view.findViewById(R.id.TxvAppName);
        TxvAppDescription = view.findViewById(R.id.TxvAppDescription);
        TxvAppDev = view.findViewById(R.id.TxvAppDev);
        TxvAppDevLink = view.findViewById(R.id.TxvAppDevLink);
        TxvAppAddress = view.findViewById(R.id.TxvAppAddress);
        TxvInstagram = view.findViewById(R.id.TxvInstagram);
        TxvFacebook = view.findViewById(R.id.TxvFacebook);
        TxvTwitter = view.findViewById(R.id.TxvTwitter);
        TxvWhatsapp = view.findViewById(R.id.TxvWhatsapp);
        TxvNoHp = view.findViewById(R.id.TxvNoHp);
        TxvEmail = view.findViewById(R.id.TxvEmail);
        APIFragmentKontak apiAbout = RetrofitClientInstance.getRetrofitInstance(Objects.requireNonNull(getActivity()).getApplicationContext()).create(APIFragmentKontak.class);
        Call<KontakPojo> aboutPojoResponseCall = apiAbout.getAbout();
        if (prefManager.getKontakPojo() != null){
            kontakPojo = prefManager.getKontakPojo();
            setView(kontakPojo, view);
        }else{
            aboutPojoResponseCall.enqueue(new Callback<KontakPojo>() {
                @Override
                public void onResponse(@NonNull Call<KontakPojo> call, @NonNull Response<KontakPojo> response) {
                    if (response.body()!=null){
                        kontakPojo = response.body();
                        prefManager.setKontakpojo(kontakPojo);
                        setView(kontakPojo, view);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<KontakPojo> call, @NonNull Throwable t) {

                }
            });
        }

        return view;
    }
    private  void setView(KontakPojo configPojos, View view){
        TxvAppName.setText(Html.fromHtml(configPojos.getApp_names() != null ? configPojos.getApp_names() : ""));
        TxvAppDescription.setText(Html.fromHtml(configPojos.getApp_description() != null ? configPojos.getApp_description() : ""));
        TxvAppDev.setText(Html.fromHtml(configPojos.getApp_dev() != null ? configPojos.getApp_dev() : ""));
        TxvAppDevLink.setText(Html.fromHtml(configPojos.getApp_dev_web() != null ? configPojos.getApp_dev_web() : ""));
        TxvAppAddress.setText(Html.fromHtml(configPojos.getApp_contact_address() != null ? configPojos.getApp_contact_address() : ""));
        TxvInstagram.setText(Html.fromHtml(configPojos.getApp_contact_ig() != null ? configPojos.getApp_contact_ig() : ""));
        TxvFacebook.setText(Html.fromHtml(configPojos.getApp_contact_fb() != null ? configPojos.getApp_contact_fb() : ""));
        TxvTwitter.setText(Html.fromHtml(configPojos.getApp_contact_twitter() != null ? configPojos.getApp_contact_twitter() : ""));
        TxvWhatsapp.setText(Html.fromHtml(configPojos.getApp_contact_wa() != null ? configPojos.getApp_contact_wa() : ""));
        TxvNoHp.setText(Html.fromHtml(configPojos.getApp_contact_phone() != null ? configPojos.getApp_contact_phone() : ""));
        TxvEmail.setText(Html.fromHtml(configPojos.getApp_contact_mail() != null ? configPojos.getApp_contact_mail() : ""));
        TxvAppDevLink.setOnClickListener(v -> {
            Intent intentl= new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), WebActivity.class);
            String url = configPojos.getApp_dev_web() != null ? configPojos.getApp_dev_web() : "";
            intentl.putExtra("url", url);
            startActivity(intentl);
        });
        LinearLayout LlTelp = view.findViewById(R.id.LlTelp);
        LlTelp.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            String no_telp = configPojos.getApp_contact_phone() != null ? configPojos.getApp_contact_phone() : "";
            intent.setData(Uri.parse("tel:"+no_telp));
            startActivity(intent);
        });
        LinearLayout LlFacebook = view.findViewById(R.id.LlFacebook);
        LlFacebook.setOnClickListener(v -> {
            Intent intentl= new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), WebActivity.class);
            String fb = configPojos.getApp_contact_fb() != null ? configPojos.getApp_contact_fb() : "";
            intentl.putExtra("url","https://web.facebook.com/"+fb);
            startActivity(intentl);
        });
        LinearLayout LlWhatsapp = view.findViewById(R.id.LlWhatsapp);
        LlWhatsapp.setOnClickListener(v -> {
            String wa = configPojos.getApp_contact_wa() != null ? configPojos.getApp_contact_wa() : "";
            String url = "https://api.whatsapp.com/send?phone="+wa;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        LinearLayout LlEmail = view.findViewById(R.id.LlEmail);
        LlEmail.setOnClickListener(v -> {
            String email = configPojos.getApp_contact_mail() != null ? configPojos.getApp_contact_mail() : "";
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",email, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Informasi Koperasi");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Halo saya ingin tanya-tanya tentang koperasi ...");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });
        LinearLayout LlInstagram = view.findViewById(R.id.LlInstagram);
        LlInstagram.setOnClickListener(v -> {
            String ig = configPojos.getApp_contact_ig() != null ? configPojos.getApp_contact_ig() : "";
            Uri uri = Uri.parse("https://instagram.com/_u/"+ig);
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

            likeIng.setPackage("com.instagram.android");

            try {
                startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://instagram.com/"+ig)));
            }
        });
        LinearLayout LlTwitter = view.findViewById(R.id.LlTwitter);
        LlTwitter.setOnClickListener(v -> {
            String twitter = configPojos.getApp_contact_twitter() != null ? configPojos.getApp_contact_twitter() : "";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/"+twitter));
            startActivity(intent);
        });
    }

}
