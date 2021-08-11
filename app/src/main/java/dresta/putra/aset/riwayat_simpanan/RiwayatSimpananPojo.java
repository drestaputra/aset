package dresta.putra.aset.riwayat_simpanan;

import com.google.gson.annotations.SerializedName;

public class RiwayatSimpananPojo {
    @SerializedName("id_riwayat_simpanan")
    String id_riwayat_simpanan;
    @SerializedName("id_simpanan")
    String id_simpanan;
    @SerializedName("jumlah_riwayat_simpanan")
    String jumlah_riwayat_simpanan;
    @SerializedName("tipe_riwayat")
    String tipe_riwayat;
    @SerializedName("input_oleh")
    String input_oleh;
    @SerializedName("input_oleh_id")
    String input_oleh_id;
    @SerializedName("tgl_riwayat_simpanan")
    String tgl_riwayat_simpanan;
    @SerializedName("keterangan_riwayat")
    String keterangan_riwayat;
    @SerializedName("jumlah_simpanan_sebelumnya")
    String jumlah_simpanan_sebelumnya;

    public RiwayatSimpananPojo(String id_riwayat_simpanan, String id_simpanan, String jumlah_riwayat_simpanan, String tipe_riwayat, String input_oleh, String input_oleh_id, String tgl_riwayat_simpanan, String keterangan_riwayat, String jumlah_simpanan_sebelumnya) {
        this.id_riwayat_simpanan = id_riwayat_simpanan;
        this.id_simpanan = id_simpanan;
        this.jumlah_riwayat_simpanan = jumlah_riwayat_simpanan;
        this.tipe_riwayat = tipe_riwayat;
        this.input_oleh = input_oleh;
        this.input_oleh_id = input_oleh_id;
        this.tgl_riwayat_simpanan = tgl_riwayat_simpanan;
        this.keterangan_riwayat = keterangan_riwayat;
        this.jumlah_simpanan_sebelumnya = jumlah_simpanan_sebelumnya;
    }

    public RiwayatSimpananPojo() {
    }

    public String getId_riwayat_simpanan() {
        return id_riwayat_simpanan;
    }

    public void setId_riwayat_simpanan(String id_riwayat_simpanan) {
        this.id_riwayat_simpanan = id_riwayat_simpanan;
    }

    public String getId_simpanan() {
        return id_simpanan;
    }

    public void setId_simpanan(String id_simpanan) {
        this.id_simpanan = id_simpanan;
    }

    public String getJumlah_riwayat_simpanan() {
        return jumlah_riwayat_simpanan;
    }

    public void setJumlah_riwayat_simpanan(String jumlah_riwayat_simpanan) {
        this.jumlah_riwayat_simpanan = jumlah_riwayat_simpanan;
    }

    public String getTipe_riwayat() {
        return tipe_riwayat;
    }

    public void setTipe_riwayat(String tipe_riwayat) {
        this.tipe_riwayat = tipe_riwayat;
    }

    public String getInput_oleh() {
        return input_oleh;
    }

    public void setInput_oleh(String input_oleh) {
        this.input_oleh = input_oleh;
    }

    public String getInput_oleh_id() {
        return input_oleh_id;
    }

    public void setInput_oleh_id(String input_oleh_id) {
        this.input_oleh_id = input_oleh_id;
    }

    public String getTgl_riwayat_simpanan() {
        return tgl_riwayat_simpanan;
    }

    public void setTgl_riwayat_simpanan(String tgl_riwayat_simpanan) {
        this.tgl_riwayat_simpanan = tgl_riwayat_simpanan;
    }

    public String getKeterangan_riwayat() {
        return keterangan_riwayat;
    }

    public void setKeterangan_riwayat(String keterangan_riwayat) {
        this.keterangan_riwayat = keterangan_riwayat;
    }

    public String getJumlah_simpanan_sebelumnya() {
        return jumlah_simpanan_sebelumnya;
    }

    public void setJumlah_simpanan_sebelumnya(String jumlah_simpanan_sebelumnya) {
        this.jumlah_simpanan_sebelumnya = jumlah_simpanan_sebelumnya;
    }
}
