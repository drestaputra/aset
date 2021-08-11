package dresta.putra.aset.alamat;

import com.google.gson.annotations.SerializedName;

public class KabupatenPojo {
    @SerializedName("id")
    String id;
    @SerializedName("id_provinsi")
    String id_provinsi;
    @SerializedName("nama")
    String nama;

    public KabupatenPojo(String id, String id_provinsi, String nama) {
        this.id = id;
        this.id_provinsi = id_provinsi;
        this.nama = nama;
    }

    public KabupatenPojo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_provinsi() {
        return id_provinsi;
    }

    public void setId_provinsi(String id_provinsi) {
        this.id_provinsi = id_provinsi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
