package dresta.putra.aset.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import dresta.putra.aset.AboutActivity;
import dresta.putra.aset.PrefManager;
import dresta.putra.aset.R;
import dresta.putra.aset.kolektor.ProfilKolektorActivity;
import dresta.putra.aset.login.LoginActivity;
import dresta.putra.aset.nasabah.NasabahActivity;
import dresta.putra.aset.owner.ProfilKoperasiActivity;
import dresta.putra.aset.request.HistoryRequestActivity;

public class FragmentFive extends Fragment {


    public FragmentFive() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_five, container, false);
        LinearLayout LlLogout = view.findViewById(R.id.LlLogout);
        LinearLayout LlProfil = view.findViewById(R.id.LlProfil);
        LinearLayout LlProfilKoperasi = view.findViewById(R.id.LlProfilKoperasi);
        LinearLayout LlNasabah = view.findViewById(R.id.LlNasabah);
        LinearLayout LlLogin = view.findViewById(R.id.LlLogin);
        LinearLayout LlAbout = view.findViewById(R.id.LlAbout);
        LinearLayout LlRequest = view.findViewById(R.id.LlRequest);
        TextView TxvUsername = view.findViewById(R.id.TxvUsername);
        final PrefManager prefManager= new PrefManager(Objects.requireNonNull(getActivity()).getApplicationContext());
        LlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.logout();
            }
        });
//        final CoordinatorLayout ClParent = view.findViewById(R.id.ClParent);
        LlProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Inasabah = new Intent(getContext(), ProfilKolektorActivity.class);
                startActivity(Inasabah);
            }
        });
        LlNasabah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Inasabah = new Intent(getContext(), NasabahActivity.class);
                startActivity(Inasabah);
            }
        });
        LlLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ILogin = new Intent(getContext(), LoginActivity.class);
                startActivity(ILogin);
            }
        });
        LlAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ILogin = new Intent(getContext(), AboutActivity.class);
                startActivity(ILogin);
            }
        });
        LlProfilKoperasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IProfilKoperasi = new Intent(getContext(), ProfilKoperasiActivity.class);
                startActivity(IProfilKoperasi);
            }
        });
        LlRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IHistory = new Intent(getContext(), HistoryRequestActivity.class);
                startActivity(IHistory);
            }
        });
        if (!prefManager.isKolektorLoggedIn()){
            TxvUsername.setVisibility(View.GONE);
            LlLogout.setVisibility(View.GONE);
            LlProfil.setVisibility(View.GONE);
            LlProfilKoperasi.setVisibility(View.GONE);
            LlNasabah.setVisibility(View.GONE);
        }else{
            TxvUsername.setText(prefManager.LoggedInKolektorUsername());
            LlLogin.setVisibility(View.GONE);
        }
        return view;
    }







}
