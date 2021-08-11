package dresta.putra.aset.alamat;

import com.google.gson.annotations.SerializedName;

public class KecamatanPojo {
    @SerializedName("id")
    String id;
    @SerializedName("id_kabupaten")
    String id_kabupaten;
    @SerializedName("nama")
    String nama;

    public KecamatanPojo(String id, String id_kabupaten, String nama) {
        this.id = id;
        this.id_kabupaten = id_kabupaten;
        this.nama = nama;
    }

    public KecamatanPojo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_kabupaten() {
        return id_kabupaten;
    }

    public void setId_kabupaten(String id_kabupaten) {
        this.id_kabupaten = id_kabupaten;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
