package dresta.putra.aset.paket;

import com.google.gson.annotations.SerializedName;

public class PaketPojo {
    @SerializedName("id_paket")
    String id_paket;
    @SerializedName("nama_paket")
    String nama_paket;
    @SerializedName("durasi_paket")
    String durasi_paket;
    @SerializedName("deskripsi_paket")
    String deskripsi_paket;
    @SerializedName("harga_paket")
    String harga_paket;
    @SerializedName("status_paket")
    String status_paket;

    public PaketPojo(String id_paket, String nama_paket, String durasi_paket, String deskripsi_paket, String harga_paket, String status_paket) {
        this.id_paket = id_paket;
        this.nama_paket = nama_paket;
        this.durasi_paket = durasi_paket;
        this.deskripsi_paket = deskripsi_paket;
        this.harga_paket = harga_paket;
        this.status_paket = status_paket;
    }

    public PaketPojo() {
    }

    public String getId_paket() {
        return id_paket;
    }

    public void setId_paket(String id_paket) {
        this.id_paket = id_paket;
    }

    public String getNama_paket() {
        return nama_paket;
    }

    public void setNama_paket(String nama_paket) {
        this.nama_paket = nama_paket;
    }

    public String getDeskripsi_paket() {
        return deskripsi_paket;
    }

    public void setDeskripsi_paket(String deskripsi_paket) {
        this.deskripsi_paket = deskripsi_paket;
    }

    public String getHarga_paket() {
        return harga_paket;
    }

    public void setHarga_paket(String harga_paket) {
        this.harga_paket = harga_paket;
    }

    public String getStatus_paket() {
        return status_paket;
    }

    public void setStatus_paket(String status_paket) {
        this.status_paket = status_paket;
    }

    public String getDurasi_paket() {
        return durasi_paket;
    }

    public void setDurasi_paket(String durasi_paket) {
        this.durasi_paket = durasi_paket;
    }
}
