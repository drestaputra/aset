package dresta.putra.aset.alamat;

import com.google.gson.annotations.SerializedName;

public class ProvinsiPojo {
    @SerializedName("id")
    String id;
    @SerializedName("nama")
    String nama;

    public ProvinsiPojo(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public ProvinsiPojo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
