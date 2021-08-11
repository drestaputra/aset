package dresta.putra.aset.nasabah;

import com.google.gson.annotations.SerializedName;

public class RiwayatNasabahPojo {
    @SerializedName("id_riwayat")
    String id_riwayat;
    @SerializedName("id_transaksi")
    String id_transaksi;
    @SerializedName("jumlah_riwayat")
    String jumlah_riwayat;
    @SerializedName("tgl_riwayat")
    String tgl_riwayat;
    @SerializedName("keterangan_riwayat")
    String keterangan_riwayat;
    @SerializedName("tipe_transaksi")
    String tipe_transaksi;

    public RiwayatNasabahPojo() {
    }

    public RiwayatNasabahPojo(String id_riwayat, String id_transaksi, String jumlah_riwayat, String tgl_riwayat, String keterangan_riwayat, String tipe_transaksi) {
        this.id_riwayat = id_riwayat;
        this.id_transaksi = id_transaksi;
        this.jumlah_riwayat = jumlah_riwayat;
        this.tgl_riwayat = tgl_riwayat;
        this.keterangan_riwayat = keterangan_riwayat;
        this.tipe_transaksi = tipe_transaksi;
    }

    public String getId_riwayat() {
        return id_riwayat;
    }

    public void setId_riwayat(String id_riwayat) {
        this.id_riwayat = id_riwayat;
    }

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getJumlah_riwayat() {
        return jumlah_riwayat;
    }

    public void setJumlah_riwayat(String jumlah_riwayat) {
        this.jumlah_riwayat = jumlah_riwayat;
    }

    public String getTgl_riwayat() {
        return tgl_riwayat;
    }

    public void setTgl_riwayat(String tgl_riwayat) {
        this.tgl_riwayat = tgl_riwayat;
    }

    public String getKeterangan_riwayat() {
        return keterangan_riwayat;
    }

    public void setKeterangan_riwayat(String keterangan_riwayat) {
        this.keterangan_riwayat = keterangan_riwayat;
    }

    public String getTipe_transaksi() {
        return tipe_transaksi;
    }

    public void setTipe_transaksi(String tipe_transaksi) {
        this.tipe_transaksi = tipe_transaksi;
    }
}
