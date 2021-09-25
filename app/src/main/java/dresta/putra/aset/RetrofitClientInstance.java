package dresta.putra.aset;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    public static Retrofit retrofit;
    public static final String BASE_URL = "https://siana.id/";
    public static PrefManager prefManager;

    public static Retrofit getRetrofitInstance(final Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(200, TimeUnit.SECONDS)
                    .readTimeout(200,TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            prefManager = new PrefManager(context);
                            String compactJws = Jwts.builder().claim("id_user",prefManager.LoggedInIdUser()).claim("username",prefManager.LoggedInUserUsername()).claim("token_expired",prefManager.getTokenExpiredTime()).claim("password",prefManager.getPassword())
                                    .signWith(SignatureAlgorithm.HS256, "453tt4n4h".getBytes())
                                    .compact();
                            Request original = chain.request();
//                            Log.d("tesdebug", compactJws);
                            Request request = original.newBuilder()
                                    .header("Content-Type", "application/json")
                                    .header("Authorization", compactJws)
                                    .method(original.method(), original.body())
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
