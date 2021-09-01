package dresta.putra.aset.berkas;

import com.google.gson.annotations.SerializedName;

public class BerkasPojo {
    @SerializedName("id_berkas")
    String id_berkas;
    @SerializedName("nama_berkas")
    String nama_berkas;
    @SerializedName("deskripsi_berkas")
    String deskripsi_berkas;
    @SerializedName("berkas")
    String berkas;
    @SerializedName("status_berkas")
    String status_berkas;

    public BerkasPojo() {
    }

    public BerkasPojo(String id_berkas, String nama_berkas, String deskripsi_berkas, String berkas, String status_berkas) {
        this.id_berkas = id_berkas;
        this.nama_berkas = nama_berkas;
        this.deskripsi_berkas = deskripsi_berkas;
        this.berkas = berkas;
        this.status_berkas = status_berkas;
    }

    public String getId_berkas() {
        return id_berkas;
    }

    public void setId_berkas(String id_berkas) {
        this.id_berkas = id_berkas;
    }

    public String getNama_berkas() {
        return nama_berkas;
    }

    public void setNama_berkas(String nama_berkas) {
        this.nama_berkas = nama_berkas;
    }

    public String getDeskripsi_berkas() {
        return deskripsi_berkas;
    }

    public void setDeskripsi_berkas(String deskripsi_berkas) {
        this.deskripsi_berkas = deskripsi_berkas;
    }

    public String getBerkas() {
        return berkas;
    }

    public void setBerkas(String berkas) {
        this.berkas = berkas;
    }

    public String getStatus_berkas() {
        return status_berkas;
    }

    public void setStatus_berkas(String status_berkas) {
        this.status_berkas = status_berkas;
    }
}
