package dresta.putra.aset.berita;

import com.google.gson.annotations.SerializedName;

public class BeritaPojo {
    @SerializedName("id_berita")
    String id_berita;
    @SerializedName("judul_berita")
    String judul_berita;
    @SerializedName("deskripsi_berita")
    String deskripsi_berita;
    @SerializedName("tgl_berita")
    String tgl_berita;
    @SerializedName("status_berita")
    String status_berita;
    @SerializedName("foto_berita")
    String foto_berita;

    public BeritaPojo(String id_berita, String judul_berita, String deskripsi_berita, String tgl_berita, String status_berita, String foto_berita) {
        this.id_berita = id_berita;
        this.judul_berita = judul_berita;
        this.deskripsi_berita = deskripsi_berita;
        this.tgl_berita = tgl_berita;
        this.status_berita = status_berita;
        this.foto_berita = foto_berita;
    }

    public BeritaPojo() {
    }

    public String getId_berita() {
        return id_berita;
    }

    public void setId_berita(String id_berita) {
        this.id_berita = id_berita;
    }

    public String getJudul_berita() {
        return judul_berita;
    }

    public void setJudul_berita(String judul_berita) {
        this.judul_berita = judul_berita;
    }

    public String getDeskripsi_berita() {
        return deskripsi_berita;
    }

    public void setDeskripsi_berita(String deskripsi_berita) {
        this.deskripsi_berita = deskripsi_berita;
    }

    public String getTgl_berita() {
        return tgl_berita;
    }

    public void setTgl_berita(String tgl_berita) {
        this.tgl_berita = tgl_berita;
    }

    public String getStatus_berita() {
        return status_berita;
    }

    public void setStatus_berita(String status_berita) {
        this.status_berita = status_berita;
    }

    public String getFoto_berita() {
        return foto_berita;
    }

    public void setFoto_berita(String foto_berita) {
        this.foto_berita = foto_berita;
    }
}
