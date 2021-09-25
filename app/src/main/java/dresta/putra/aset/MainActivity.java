package dresta.putra.aset;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import dresta.putra.aset.fragment.FragmentFive;
import dresta.putra.aset.fragment.FragmentKontak;
import dresta.putra.aset.fragment.FragmentPeta;
import dresta.putra.aset.fragment.FragmentAset;
import dresta.putra.aset.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;


public class MainActivity extends AppCompatActivity {
    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new FragmentAset();
    final Fragment fragment3 = new FragmentPeta();
    final Fragment fragment4 = new FragmentKontak();
    final Fragment fragment5 = new FragmentFive();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    PrefManager prefManager;
    private String id_owner = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BottomNavigationView navigation = findViewById(R.id.navigation);
//        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        prefManager = new PrefManager(MainActivity.this);
        if (!prefManager.isUserLoggedIn()){
            Intent Iback= new Intent(MainActivity.this, LoginActivity.class);
            Iback.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(Iback);
            MainActivity.this.finish();
        }

        FloatingActionButton Fab= findViewById(R.id.Fab);
        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fm.beginTransaction().detach(active).attach(fragment3).commit();
                navigation.setSelectedItemId(R.id.navigation_peta);
                active = fragment3;
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();

        fm.beginTransaction().add(R.id.main_container, fragment5, "fragment5").detach(fragment5).commit();
        fm.beginTransaction().add(R.id.main_container, fragment4, "fragment4").detach(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "fragment3").detach(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "fragment2").detach(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "fragment1").detach(fragment1).commit();
        fm.beginTransaction().attach(fragment1).commit();
        getCurrentFirebaseToken();

        FirebaseMessaging.getInstance().subscribeToTopic("all");
    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().detach(active).attach(fragment1).commit();
                    active = fragment1;
                    return true;
                case R.id.navigation_aset:
                    fm.beginTransaction().detach(active).attach(fragment2).commit();
                    active = fragment2;
                    return true;
                case R.id.navigation_peta:
                    fm.beginTransaction().detach(active).attach(fragment3).commit();
                    active = fragment3;
                    return true;
                case R.id.navigation_kontak:
                    fm.beginTransaction().detach(active).attach(fragment4).commit();
                    active = fragment4;
                    return true;
                case R.id.navigation_lainnya:
                    fm.beginTransaction().detach(active).attach(fragment5).commit();
                    active = fragment5;
                    return true;
            }
            return false;
        }
    };
    private void getCurrentFirebaseToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult().getToken();


                    }
                });
    }


}