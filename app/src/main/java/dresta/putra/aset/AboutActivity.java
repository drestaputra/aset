package dresta.putra.aset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import dresta.putra.aset.kontak.KontakPojo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class AboutActivity extends AppCompatActivity {
    private TextView TxvAppName,TxvAppDescription, TxvAppDev, TxvAppDevLink, TxvAppAddress, TxvInstagram, TxvFacebook, TxvTwitter, TxvWhatsapp, TxvNoHp, TxvEmail;
    private PrefManager prefManager;
    private KontakPojo kontakPojo = new KontakPojo();

    interface APIFragmentThree{
        @GET("api/about/index")
        Call<KontakPojo> getAbout();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        prefManager = new PrefManager(AboutActivity.this);
        TxvAppName = findViewById(R.id.TxvAppName);
        TxvAppDescription = findViewById(R.id.TxvAppDescription);
        TxvAppDev = findViewById(R.id.TxvAppDev);
        TxvAppDevLink = findViewById(R.id.TxvAppDevLink);
        TxvAppAddress = findViewById(R.id.TxvAppAddress);
        TxvInstagram = findViewById(R.id.TxvInstagram);
        TxvFacebook = findViewById(R.id.TxvFacebook);
        TxvTwitter = findViewById(R.id.TxvTwitter);
        TxvWhatsapp = findViewById(R.id.TxvWhatsapp);
        TxvNoHp = findViewById(R.id.TxvNoHp);
        TxvEmail = findViewById(R.id.TxvEmail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (prefManager.getKontakPojo() != null){
            kontakPojo = prefManager.getKontakPojo();
            setView(kontakPojo);
        }else{
            APIFragmentThree apiAbout = RetrofitClientInstance.getRetrofitInstance(this).create(APIFragmentThree.class);
            Call<KontakPojo> aboutPojoResponseCall = apiAbout.getAbout();
            aboutPojoResponseCall.enqueue(new Callback<KontakPojo>() {
                @Override
                public void onResponse(@NonNull Call<KontakPojo> call, @NonNull Response<KontakPojo> response) {
                    if (response.body()!=null){
                        kontakPojo = response.body();
                        setView(kontakPojo);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<KontakPojo> call, @NonNull Throwable t) {

                }
            });
        }

    }
    private void setView(KontakPojo configPojos){
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
        TxvAppDevLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentl= new Intent(AboutActivity.this, WebActivity.class);
                String url = configPojos.getApp_dev_web() != null ? configPojos.getApp_dev_web() : "";
                intentl.putExtra("url", url);
                startActivity(intentl);
            }
        });
        LinearLayout LlTelp = findViewById(R.id.LlTelp);
        LlTelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String no_telp = configPojos.getApp_contact_phone() != null ? configPojos.getApp_contact_phone() : "";
                intent.setData(Uri.parse("tel:"+no_telp));
                startActivity(intent);
            }
        });
        LinearLayout LlFacebook = findViewById(R.id.LlFacebook);
        LlFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentl= new Intent(AboutActivity.this, WebActivity.class);
                String fb = configPojos.getApp_contact_fb() != null ? configPojos.getApp_contact_fb() : "";
                intentl.putExtra("url","https://web.facebook.com/"+fb);
                startActivity(intentl);
            }
        });
        LinearLayout LlWhatsapp = findViewById(R.id.LlWhatsapp);
        LlWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wa = configPojos.getApp_contact_wa() != null ? configPojos.getApp_contact_wa() : "";
                String url = "https://api.whatsapp.com/send?phone="+wa;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        LinearLayout LlEmail = findViewById(R.id.LlEmail);
        LlEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = configPojos.getApp_contact_mail() != null ? configPojos.getApp_contact_mail() : "";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Informasi Koperasi");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Halo saya ingin tanya-tanya tentang koperasi ...");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        LinearLayout LlInstagram = findViewById(R.id.LlInstagram);
        LlInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        LinearLayout LlTwitter = findViewById(R.id.LlTwitter);
        LlTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                String twitter = configPojos.getApp_contact_twitter() != null ? configPojos.getApp_contact_twitter() : "";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/"+twitter));
                startActivity(intent);
            }
        });
    }
    

}
