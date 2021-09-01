package dresta.putra.aset.peta;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

import java.util.List;

public class PetaMarkerPojo implements ClusterItem {
    private final String id_aset;
    private final LatLng position;
    private final String title;
    private final String snippet;

    public PetaMarkerPojo(String id_aset, double lat, double lng, String title, String snippet) {
        this.id_aset = id_aset;
        position = new LatLng(lat, lng);
        this.title = title;
        this.snippet = snippet;
    }

    public String getId_aset() {
        return id_aset;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}