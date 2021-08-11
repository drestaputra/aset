package dresta.putra.aset.alamat;

import com.google.gson.annotations.SerializedName;

public class KelurahanPojo {
    @SerializedName("id")
    String id;
    @SerializedName("id_kecamatan")
    String id_kecamatan;
    @SerializedName("nama")
    String nama;

    public KelurahanPojo(String id, String id_kecamatan, String nama) {
        this.id = id;
        this.id_kecamatan = id_kecamatan;
        this.nama = nama;
    }

    public KelurahanPojo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_kecamatan() {
        return id_kecamatan;
    }

    public void setId_kecamatan(String id_kecamatan) {
        this.id_kecamatan = id_kecamatan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
