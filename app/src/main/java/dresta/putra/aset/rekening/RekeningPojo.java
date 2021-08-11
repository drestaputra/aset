package dresta.putra.aset.rekening;

import com.google.gson.annotations.SerializedName;

public class RekeningPojo {
    @SerializedName("id_rekening")
    String id_rekening;
    @SerializedName("no_rekening")
    String no_rekening;
    @SerializedName("nama_pemilik_rekening")
    String nama_pemilik_rekening;
    @SerializedName("nama_bank")
    String nama_bank;
    @SerializedName("gambar_bank")
    String gambar_bank;
    @SerializedName("status")
    String status;

    public RekeningPojo() {
    }

    public RekeningPojo(String id_rekening, String no_rekening, String nama_pemilik_rekening, String nama_bank, String gambar_bank, String status) {
        this.id_rekening = id_rekening;
        this.no_rekening = no_rekening;
        this.nama_pemilik_rekening = nama_pemilik_rekening;
        this.nama_bank = nama_bank;
        this.gambar_bank = gambar_bank;
        this.status = status;
    }

    public String getId_rekening() {
        return id_rekening;
    }

    public void setId_rekening(String id_rekening) {
        this.id_rekening = id_rekening;
    }

    public String getNo_rekening() {
        return no_rekening;
    }

    public void setNo_rekening(String no_rekening) {
        this.no_rekening = no_rekening;
    }

    public String getNama_pemilik_rekening() {
        return nama_pemilik_rekening;
    }

    public void setNama_pemilik_rekening(String nama_pemilik_rekening) {
        this.nama_pemilik_rekening = nama_pemilik_rekening;
    }

    public String getNama_bank() {
        return nama_bank;
    }

    public void setNama_bank(String nama_bank) {
        this.nama_bank = nama_bank;
    }

    public String getGambar_bank() {
        return gambar_bank;
    }

    public void setGambar_bank(String gambar_bank) {
        this.gambar_bank = gambar_bank;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
