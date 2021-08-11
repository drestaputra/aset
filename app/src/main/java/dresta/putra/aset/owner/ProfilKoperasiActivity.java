package dresta.putra.aset.owner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.annotations.SerializedName;

import dresta.putra.aset.R;
import dresta.putra.aset.RetrofitClientInstance;
import dresta.putra.aset.WebActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class ProfilKoperasiActivity extends AppCompatActivity {
    private TextView TxvFacebook, TxvTwitter, TxvInstagram, TxvEmail, TxvNamaKoperasi, TxvDeskripsiKoperasi, TxvWhatsapp, TxvNoHp, TxvVisi, TxvAlamatKoperasi, TxvMisi;
    interface APIProfilKoperasi{
        @GET("api/kolektor/profil_koperasi")
        Call<ResponsePojoProfilKoperasi> getProfilKoperasi();
    }
    private APIProfilKoperasi servicePojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_koperasi);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TxvFacebook = findViewById(R.id.TxvFacebook);
        TxvTwitter = findViewById(R.id.TxvTwitter);
        TxvInstagram = findViewById(R.id.TxvInstagram);
        TxvEmail = findViewById(R.id.TxvEmail);
        TxvNamaKoperasi = findViewById(R.id.TxvNamaKoperasi);
        TxvWhatsapp = findViewById(R.id.TxvWhatsapp);
        TxvNoHp = findViewById(R.id.TxvNoHp);
        TxvDeskripsiKoperasi = findViewById(R.id.TxvDeskripsiKoperasi);
        TxvVisi = findViewById(R.id.TxvVisi);
        TxvMisi = findViewById(R.id.TxvMisi);
        TxvAlamatKoperasi = findViewById(R.id.TxvAlamatKoperasi);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        AppBarLayout app_bar = findViewById(R.id.app_bar);
        final LinearLayout LlFoto = findViewById(R.id.LlFoto);
        final CollapsingToolbarLayout toolbar_layout = findViewById(R.id.toolbar_layout);
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
//                    collapsed
                    LlFoto.setVisibility(View.GONE);
                    toolbar_layout.setTitle("Profil Koperasi");
                    isShow = true;
                } else if(isShow) {
//                    expanded
                    toolbar_layout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                    LlFoto.setVisibility(View.VISIBLE);
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        servicePojo = RetrofitClientInstance.getRetrofitInstance(ProfilKoperasiActivity.this).create(APIProfilKoperasi.class);
        initProfil();
    }
    private void initProfil(){
        Call<ResponsePojoProfilKoperasi> responsePojoProfilKoperasiCall = servicePojo.getProfilKoperasi();
        responsePojoProfilKoperasiCall.enqueue(new Callback<ResponsePojoProfilKoperasi>() {
            @Override
            public void onResponse(Call<ResponsePojoProfilKoperasi> call, Response<ResponsePojoProfilKoperasi> response) {
                if (response.body()!=null){
                    if (response.body().getStatus()==200){
                        final ProfilKoperasiPojo profilKoperasiPojo = response.body().getData();
                        TxvFacebook.setText(profilKoperasiPojo.getFacebook());
                        TxvTwitter.setText(profilKoperasiPojo.getTwitter());
                        TxvInstagram.setText(profilKoperasiPojo.getInstagram());
                        TxvEmail.setText(profilKoperasiPojo.getEmail());
                        TxvNamaKoperasi.setText(profilKoperasiPojo.getNama_koperasi());
                        TxvNoHp.setText(profilKoperasiPojo.getNo_telp());
                        TxvWhatsapp.setText(profilKoperasiPojo.getNo_wa());
                        TxvAlamatKoperasi.setText(profilKoperasiPojo.getAlamat());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            TxvDeskripsiKoperasi.setText(HtmlCompat.fromHtml(profilKoperasiPojo.getDeskripsi_koperasi(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                            TxvVisi.setText(HtmlCompat.fromHtml(profilKoperasiPojo.getVisi(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                            TxvMisi.setText(HtmlCompat.fromHtml(profilKoperasiPojo.getMisi(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                        } else {
                            TxvDeskripsiKoperasi.setText(Html.fromHtml(profilKoperasiPojo.getDeskripsi_koperasi()));
                            TxvVisi.setText(Html.fromHtml(profilKoperasiPojo.getVisi()));
                            TxvMisi.setText(Html.fromHtml(profilKoperasiPojo.getMisi()));
                        }
                        LinearLayout LlTelp = findViewById(R.id.LlTelp);
                        LlTelp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+profilKoperasiPojo.getNo_telp()));
                                startActivity(intent);
                            }
                        });
                        LinearLayout LlFacebook = findViewById(R.id.LlFacebook);
                        LlFacebook.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intentl= new Intent(ProfilKoperasiActivity.this, WebActivity.class);
                                intentl.putExtra("url","https://web.facebook.com/"+profilKoperasiPojo.getFacebook());
                                startActivity(intentl);
                            }
                        });
                        LinearLayout LlWhatsapp = findViewById(R.id.LlWhatsapp);
                        LlWhatsapp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = "https://api.whatsapp.com/send?phone="+profilKoperasiPojo.getNo_wa();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });
                        LinearLayout LlEmail = findViewById(R.id.LlEmail);
                        LlEmail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto",profilKoperasiPojo.getEmail(), null));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Informasi Koperasi");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "Halo saya ingin tanya-tanya tentang koperasi ...");
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            }
                        });
                        LinearLayout LlInstagram = findViewById(R.id.LlInstagram);
                        LlInstagram.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri uri = Uri.parse("https://instagram.com/_u/"+profilKoperasiPojo.getInstagram());
                                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                                likeIng.setPackage("com.instagram.android");

                                try {
                                    startActivity(likeIng);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("https://instagram.com/"+profilKoperasiPojo.getInstagram())));
                                }
                            }
                        });
                        LinearLayout LlTwitter = findViewById(R.id.LlTwitter);
                        LlTwitter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = null;
                                String twitter = profilKoperasiPojo.getTwitter() != null ? profilKoperasiPojo.getTwitter() : "";
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/"+twitter));
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponsePojoProfilKoperasi> call, Throwable t) {
                Toast.makeText(ProfilKoperasiActivity.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    class ResponsePojoProfilKoperasi{
        @SerializedName("status")
        Integer status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        ProfilKoperasiPojo data;

        public ResponsePojoProfilKoperasi(Integer status, String msg, ProfilKoperasiPojo data) {
            this.status = status;
            this.msg = msg;
            this.data = data;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public ProfilKoperasiPojo getData() {
            return data;
        }

        public void setData(ProfilKoperasiPojo data) {
            this.data = data;
        }
    }

}
